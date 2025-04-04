import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class SaveCommand implements Command {
    private final Repository repo;
    private final List<String> options;

    public SaveCommand(Repository repo, List<String> options) {
        this.repo = repo;
        this.options = options;
    }

    @Override
    public String getCommandName() {
        return "save";
    }

    @Override
    public List<String> getOptions() {
        return options;
    }

    @Override
    public String getPath() {
        return "";
    }

    @Override
    public int execute() throws InvalidRepositoryException {
        if (options.isEmpty()) {
            System.out.println("Usage: save <file-path>");
            return -1;
        }

        String filePath = options.get(0);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(repo);
            System.out.println("Repository saved to " + filePath);
            return 0;
        } catch (IOException e) {
            throw new InvalidRepositoryException("Error saving repository: " + e.getMessage());
        }
    }
}
