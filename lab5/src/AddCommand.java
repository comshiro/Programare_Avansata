import java.util.List;

public class AddCommand implements Command {
    private final String commandName = "add";
    private final List<String> options;
    private final String path;
    private final Repository repo;

    public AddCommand(Repository repo, List<String> options, String path) {
        this.repo = repo;
        this.options = options;
        this.path = path;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public List<String> getOptions() {
        return options;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public int execute() throws InvalidCommandException {
        if (path == null || path.trim().isEmpty()) {
            throw new InvalidCommandException("Invalid file path provided.");
        }

        Image image = new Image(path.substring(path.lastIndexOf("/") + 1), java.time.LocalDate.now(), path);
        repo.add(image);

        System.out.println("Image added: " + image);
        return 0;
    }
}
