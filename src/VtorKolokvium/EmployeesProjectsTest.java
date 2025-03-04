package VtorKolokvium;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

class Employee{
    private String id;
    private String name;
    private Set<String> skills;

    Employee(String id, String name, Set<String> skills) {
        this.id = id;
        this.name = name;
        this.skills = skills;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getSkills() {
        return skills;
    }
}

class Project{
    private String id;
    private String name;
    private Set<String> requiredSkills;
    private LocalDate startDate;
    private LocalDate endDate;

    Project(String id, String name, Set<String> requiredSkills, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.requiredSkills = requiredSkills;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getRequiredSkills() {
        return requiredSkills;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
class ProjectManagementSystem{
    private Map<String,Employee> employees;
    private Map<String,Project> projects;
    private Map<String,List<Project>> employeesProjects;
    ProjectManagementSystem(){
        employees=new HashMap<>();
        projects=new HashMap<>();
        employeesProjects=new HashMap<>();
    }

    public void addEmployee(String id, String name, Set<String> skills) {
        employees.put(id,new Employee(id,name,skills));
    }
    public void addProject(String id, String name, Set<String> requiredSkills, LocalDate startDate, LocalDate endDate){
        projects.put(id,new Project(id,name,requiredSkills,startDate,endDate));
    }
    public boolean assignEmployeeToProject(String employeeId, String projectId){
        Employee employee=employees.get(employeeId);
        Project project=projects.get(projectId);
        if (employee==null || project==null){
            return false;
        }
        if (!employee.getSkills().containsAll(project.getRequiredSkills())){
            return false;
        }
        List<Project> assignedProjects=employeesProjects.getOrDefault(employeeId,new ArrayList<>());
        boolean hasOverLapping=assignedProjects.stream()
                .anyMatch(i->
                        !(i.getStartDate().isAfter(project.getEndDate()) ||
                                i.getEndDate().isBefore(project.getStartDate())));

        if (hasOverLapping){
            return false;
        }
        assignedProjects.add(project);
        employeesProjects.put(employeeId,assignedProjects);
        return true;
    }


    public String findBestEmployeeForProject(String projectId) {
        if (!projects.containsKey(projectId)){
            return "The project does not exits";
        }
        Project project=projects.get(projectId);

        List<Employee> employeeWithSkills=employees.values().stream()
                .filter(i->i.getSkills().containsAll(project.getRequiredSkills()))
                .toList();
        if (employeeWithSkills.isEmpty()){
            return null;
        }
        int min=Integer.MAX_VALUE;
        Employee maxEmployee=null;

        for (Employee e :employeeWithSkills){
            List<Project> projectPerEmployee=employeesProjects.getOrDefault(e.getId(),new ArrayList<>());
            if (projectPerEmployee.size()<min){
                min=projectPerEmployee.size();
                maxEmployee=e;
            }
        }
        return maxEmployee.getId() !=null ? maxEmployee.getId() : null;
    }

    public void printEmployeeSchedule(String employeeId) {
        System.out.println("Schedule for employee "+employeeId+":");
        employeesProjects.get(employeeId)
                .forEach(p-> System.out.println("Project: "+p.getName()+", Dates: "+p.getStartDate()+" to "+p.getEndDate()));
    }
}
public class EmployeesProjectsTest {
    public static void main(String[] args) {
        ProjectManagementSystem system = new ProjectManagementSystem();

        // Adding employees
        system.addEmployee("E001", "Ana", Set.of("Java", "Spring", "SQL"));
        system.addEmployee("E002", "Marko", Set.of("Python", "Django", "ML"));
        system.addEmployee("E003", "Elena", Set.of("Java", "Spring", "SQL", "Docker"));

        // Adding projects
        system.addProject("P001", "Web App", Set.of("Java", "Spring", "SQL"), LocalDate.of(2024, 3, 1), LocalDate.of(2024, 6, 1));
        system.addProject("P002", "AI Model", Set.of("Python", "ML"), LocalDate.of(2024, 4, 1), LocalDate.of(2024, 7, 1));

        // Assigning employees to projects
        system.assignEmployeeToProject("E001", "P001");
        system.assignEmployeeToProject("E002", "P002");

        // Finding the best employee for a project
        String bestEmployeeId = system.findBestEmployeeForProject("P001");
        System.out.println("Best employee for project P001: " + bestEmployeeId);

        // Printing schedule of an employee
        system.printEmployeeSchedule("E001");
    }
}
