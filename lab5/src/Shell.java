import java.util.*;

public class Shell {
    private final Repository repo;
    private final Scanner scanner;

    public Shell(Repository repo) {
        this.repo = repo;
        this.scanner = new Scanner(System.in);
    }

    public void start() throws InvalidCommandException, InvalidRepositoryException {
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equals("exit")) {
                System.out.println("Exiting shell...");
                break;
            }

            processCommand(input);
        }
    }

    private void processCommand(String input) throws InvalidCommandException, InvalidRepositoryException {
        String[] parts = input.split("\\s+", 2);  // Split command from arguments
        String commandName = parts[0].toLowerCase();
        String arguments = (parts.length > 1) ? parts[1] : "";

        Command command = null;
        List<String> options = List.of(arguments.split("\\s+")); // Convert arguments to list

        switch (commandName) {
            case "add":
                if (!arguments.isEmpty()) {
                    command = new AddCommand(repo, options, arguments);
                } else {
                    System.out.println("Usage: add <image_path>");
                }
                break;

            case "remove":
                if (!arguments.isEmpty()) {
                    command = new RemoveCommand(repo, options, arguments);
                } else {
                    System.out.println("Usage: remove <image_name>");
                }
                break;

            case "update":
                if (options.size() == 3) { // update <image_name> <new_name> <new_date>
                    command = new UpdateCommand(repo, options);
                } else {
                    System.out.println("Usage: update <image_name> <new_name> <new_date>");
                }
                break;

            case "load":
                if (!arguments.isEmpty()) {
                    command = new LoadCommand(repo, List.of(arguments));
                } else {
                    System.out.println("Usage: load <file_path>");
                }
                break;

            case "save":
                if (!arguments.isEmpty()) {
                    command = new SaveCommand(repo, List.of(arguments));
                } else {
                    System.out.println("Usage: save <file_path>");
                }
                break;

            case "report":
                command = new ReportCommand(repo);
                break;

            default:
                System.out.println("Unknown command: " + commandName);
                return;
        }

        if (command != null) {
            int result = command.execute();
            if (result == 0) {
                System.out.println("Command executed successfully.");
            } else {
                System.out.println("Command failed.");
            }
        }
    }
}
