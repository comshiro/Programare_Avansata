import java.time.LocalDate;
import java.util.List;

public class UpdateCommand implements Command {
    private final Repository repo;
    private final List<String> options;

    public UpdateCommand(Repository repo, List<String> options) {
        this.repo = repo;
        this.options = options;
    }

    @Override
    public String getCommandName() {
        return "update";
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
    public int execute() {
        if (options.size() < 2) {
            System.out.println("Usage: update <image-name> <new-date> <new-path>");
            return -1;
        }

        String name = options.get(0);
        LocalDate newDate = LocalDate.parse(options.get(1));
        String newPath = options.size() > 2 ? options.get(2) : "";

        for (Image img : repo.getImages()) {
            if (img.name().equals(name)) {
                repo.getImages().remove(img);
                repo.getImages().add(new Image(name, newDate, newPath));
                System.out.println("Image updated: " + name);
                return 0;
            }
        }

        throw new ImageNotFoundException("Image not found: " + name);
    }
}
