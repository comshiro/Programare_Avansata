import java.util.List;

public class RemoveCommand implements Command {
    private final Repository repo;
    private final List<String> options;

    public RemoveCommand(Repository repo, List<String> options, String arguments) {
        this.repo = repo;
        this.options = options;
    }

    @Override
    public String getCommandName() {
        return "remove";
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
    public int execute() throws ImageNotFoundException {
        if (options.isEmpty()) {
            System.out.println("Usage: remove <image-name>");
            return -1;
        }

        String name = options.get(0);
        List<Image> images = repo.getImages();

        boolean removed = images.removeIf(img -> img.name().equals(name));

        if (removed) {
            System.out.println("Image removed: " + name);
            return 0;
        } else {
            throw new ImageNotFoundException("Image not found: " + name);
        }
    }
}
