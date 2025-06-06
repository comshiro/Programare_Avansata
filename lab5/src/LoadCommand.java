import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class LoadCommand implements Command {
    private final Repository repo;
    private final List<String> options;

    public LoadCommand(Repository repo, List<String> options) {
        this.repo = repo;
        this.options = options;
    }

    @Override
    public String getCommandName() {
        return "load";
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
            System.out.println("Usage: load <file-path>");
            return -1;
        }

        String filePath = options.get(0);

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Repository loadedRepo = (Repository) ois.readObject();
            repo.getImages().clear();
            repo.getImages().addAll(loadedRepo.getImages());
            System.out.println("Repository loaded from " + filePath);
            if (repo.getImages().isEmpty()) {
                System.out.println("No images found in the loaded repository.");
            } else {
                System.out.println("Loaded images:");
                for (Image image : repo.getImages()) {
                    System.out.println(image);
                }
            }
            return 0;
        } catch (IOException | ClassNotFoundException e) {
            throw new InvalidRepositoryException("Error loading repository: " + e.getMessage());
        }
    }
}
