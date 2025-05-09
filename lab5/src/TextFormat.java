import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TextFormat implements RepositoryFormat {

    @Override
    public void save(Repository repo, String path) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (Image image : repo.getImages()) {
                writer.write(image.name() + " | " + image.date() + " | " + image.path());
                writer.newLine();
            }
        }
    }

    @Override
    public Repository load(String path) throws IOException, ClassNotFoundException {
        Repository repo = new Repository();
        return repo;
    }
}
