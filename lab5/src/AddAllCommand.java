import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;

public class AddAllCommand implements Command {
    private final Repository repo;
    private final List<String> options;

    public AddAllCommand(Repository repo, List<String> options) {
        this.repo = repo;
        this.options = options;
    }

    @Override
    public String getCommandName() {
        return "addAll";
    }

    @Override
    public List<String> getOptions() {
        return options;
    }

    @Override
    public String getPath() {
        return options.isEmpty() ? "" : options.get(0);
    }

    @Override
    public int execute() throws InvalidCommandException {
        if (options.isEmpty()) {
            System.out.println("Usage: addAll <directory-path>");
            return -1;
        }

        String directoryPath = options.get(0);
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Invalid directory path: " + directoryPath);
            return -1;
        }

        try {
            // Traverse the directory and its subdirectories
            Files.walk(Paths.get(directoryPath))
                    .filter(Files::isRegularFile)
                    .filter(path -> isImageFile(path.toFile()))
                    .forEach(path -> {
                        try {
                            // Add the image to the repository
                            String name = path.getFileName().toString();
                            String pathString = path.toString();
                            Image image = new Image(name, java.time.LocalDate.now(), pathString);
                            repo.add(image);
                            System.out.println("Image added: " + image);
                        } catch (Exception e) {
                            System.out.println("Error adding image: " + e.getMessage());
                        }
                    });

            return 0;
        } catch (IOException e) {
            System.out.println("Error reading directory: " + e.getMessage());
            return -1;
        }
    }

    private boolean isImageFile(File file) {
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif", "bmp", "tiff"};
        String fileName = file.getName().toLowerCase();

        for (String ext : imageExtensions) {
            if (fileName.endsWith("." + ext)) {
                return true;
            }
        }
        return false;
    }
}
