package VtorKolokvium;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

class DeadlineNotValidException extends Exception {
    public DeadlineNotValidException(String message) {
        super(message);
    }
}

class Task {
    private String category;
    private String name;
    private String description;
    private LocalDateTime deadline;
    private Integer priority;

    public Task(String category, String name, String description, LocalDateTime deadline, Integer priority) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public Integer getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "Task{" +
                "category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", priority=" + priority +
                '}';
    }
}

class TaskManager {
    private static final LocalDateTime MAX_DEADLINE = LocalDateTime.of(2020, 6, 24, 0, 0, 0);
    private List<Task> tasks;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    public void readTasks(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length < 3) continue; // Skip invalid lines

            String category = parts[0].trim();
            String name = parts[1].trim();
            String description = parts[2].trim();
            LocalDateTime deadline = null;
            Integer priority = null;

            // Parse deadline
            if (parts.length > 3 && !parts[3].isEmpty()) {
                try {
                    deadline = LocalDateTime.parse(parts[3].trim(), formatter);
                    if (deadline.isAfter(MAX_DEADLINE)) {
                        throw new DeadlineNotValidException("The deadline " + deadline + " has already passed");
                    }
                } catch (DateTimeParseException e) {
                    System.err.println("Invalid date format for task: " + name);
                    continue;
                } catch (DeadlineNotValidException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
            }

            // Parse priority
            if (parts.length > 4 && !parts[4].isEmpty()) {
                try {
                    priority = Integer.parseInt(parts[4].trim());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid priority format for task: " + name);
                }
            }

            tasks.add(new Task(category, name, description, deadline, priority));
        }
    }

    public void printTasks(PrintStream out, boolean includePriority, boolean includeCategory) {
        PrintWriter writer = new PrintWriter(out);

        List<Task> sortedTasks = tasks.stream()
                .sorted((t1, t2) -> {
                    if (includePriority) {
                        if (t1.getPriority() != null && t2.getPriority() != null) {
                            return Integer.compare(t1.getPriority(), t2.getPriority());
                        } else if (t1.getPriority() != null) {
                            return -1;
                        } else if (t2.getPriority() != null) {
                            return 1;
                        }
                    }
                    if (t1.getDeadline() != null && t2.getDeadline() != null) {
                        return t1.getDeadline().compareTo(t2.getDeadline());
                    } else if (t1.getDeadline() != null) {
                        return -1;
                    } else if (t2.getDeadline() != null) {
                        return 1;
                    }
                    return 0;
                })
                .collect(Collectors.toList());

        if (includeCategory) {
            Map<String, List<Task>> groupedByCategory = sortedTasks.stream()
                    .collect(Collectors.groupingBy(Task::getCategory, LinkedHashMap::new, Collectors.toList()));

            groupedByCategory.forEach((category, taskList) -> {
                writer.println("Category: " + category);
                taskList.forEach(writer::println);
                writer.println();
            });
        } else {
            sortedTasks.forEach(writer::println);
        }
        writer.flush();
    }
}

public class TasksManagerTest {

    public static void main(String[] args) throws DeadlineNotValidException, IOException {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}
