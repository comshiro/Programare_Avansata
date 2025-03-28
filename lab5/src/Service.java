import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Service {

    public void save(Repository repo, String path) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(repo);
        }
    }

    public Repository load(String path) throws InvalidRepositoryException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return (Repository) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new InvalidRepositoryException("Error loading repository: " + e.getMessage());
        }
    }

    public void view(Image img) {
        try {
            File file = new File(img.path());
            if (file.exists()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
            } else {
                System.out.println("Image file not found: " + img.path());
            }
        } catch (IOException e) {
            System.out.println("Error opening image: " + e.getMessage());
        }
    }
}
