package PrvKolokvium;

import java.util.Scanner;

/**
 * I partial exam 2016
 */
class InvalidEvaluation extends Exception{
    InvalidEvaluation(){
        super("Invalid Evaluation");
    }
}
public class ApplicantEvaluationTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        int creditScore = scanner.nextInt();
        int employmentYears = scanner.nextInt();
        boolean hasCriminalRecord = scanner.nextBoolean();
        int choice = scanner.nextInt();
        Applicant applicant = new Applicant(name, creditScore, employmentYears, hasCriminalRecord);
        Evaluator1.TYPE type = Evaluator1.TYPE.values()[choice];
        Evaluator1 evaluator = null;
        try {
            evaluator = EvaluatorBuilder1.build(type);
            System.out.println("Applicant");
            System.out.println(applicant);
            System.out.println("Evaluation type: " + type.name());
            if (evaluator.evaluate(applicant)) {
                System.out.println("Applicant is ACCEPTED");
            } else {
                System.out.println("Applicant is REJECTED");
            }
        } catch (InvalidEvaluation invalidEvaluation) {
            System.out.println("Invalid evaluation");
        }
    }
}

class Applicant {
    private String name;

    private int creditScore;
    private int employmentYears;
    private boolean hasCriminalRecord;

    public Applicant(String name, int creditScore, int employmentYears, boolean hasCriminalRecord) {
        this.name = name;
        this.creditScore = creditScore;
        this.employmentYears = employmentYears;
        this.hasCriminalRecord = hasCriminalRecord;
    }

    public String getName() {
        return name;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public int getEmploymentYears() {
        return employmentYears;
    }

    public boolean hasCriminalRecord() {
        return hasCriminalRecord;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\nCredit score: %d\nExperience: %d\nCriminal record: %s\n",
                name, creditScore, employmentYears, hasCriminalRecord ? "Yes" : "No");
    }
}

interface Evaluator1 {
    enum TYPE {
        NO_CRIMINAL_RECORD,
        MORE_EXPERIENCE,
        MORE_CREDIT_SCORE,
        NO_CRIMINAL_RECORD_AND_MORE_EXPERIENCE,
        MORE_EXPERIENCE_AND_MORE_CREDIT_SCORE,
        NO_CRIMINAL_RECORD_AND_MORE_CREDIT_SCORE,
        INVALID // should throw exception
    }

    boolean evaluate(Applicant applicant);
}

class EvaluatorBuilder1 {
    public static Evaluator1 build(Evaluator1.TYPE type) throws InvalidEvaluation {
        if (type.equals(Evaluator1.TYPE.NO_CRIMINAL_RECORD)){
            return applicant -> !applicant.hasCriminalRecord();
        }else if (type.equals(Evaluator1.TYPE.MORE_EXPERIENCE)){
            return applicant -> applicant.getEmploymentYears()>=10;
        }else if (type.equals(Evaluator1.TYPE.MORE_CREDIT_SCORE)){
            return applicant -> applicant.getCreditScore()>=500;
        }else if (type.equals(Evaluator1.TYPE.NO_CRIMINAL_RECORD_AND_MORE_EXPERIENCE)){
            return applicant -> !applicant.hasCriminalRecord() && applicant.getEmploymentYears()>=10;
        }else if (type.equals(Evaluator1.TYPE.MORE_EXPERIENCE_AND_MORE_CREDIT_SCORE)){
            return applicant -> applicant.getEmploymentYears()>=10 && applicant.getCreditScore()>=500;
        }else if (type.equals(Evaluator1.TYPE.NO_CRIMINAL_RECORD_AND_MORE_CREDIT_SCORE)){
            return applicant -> !applicant.hasCriminalRecord() && applicant.getCreditScore()>=500;
        }else
            throw new InvalidEvaluation();
    }
}


// имплементација на евалуатори овде7

