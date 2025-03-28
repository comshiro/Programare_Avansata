import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Repository {
    private List<Image> images = new ArrayList<>();
    private String name;

    public Repository(String name) {
        this.name = name;
    }

    public List<Image> getImages() {
        return new ArrayList<>(images);
    }

    public void add(Image image) {
        images.add(image);
    }

    public void addAll(String folderPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".jpg") || path.toString().toLowerCase().endsWith(".png"))
                    .forEach(path -> images.add(new Image(path.getFileName().toString(), java.time.LocalDate.now(), path.toAbsolutePath().toString())));
        } catch (IOException e) {
            System.out.println("Error reading folder: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Repository: " + name + "\nImages:\n" + images;
    }
}
