import java.io.IOException;

public interface RepositoryFormat {
    void save(Repository repo, String path) throws IOException;
    Repository load(String path) throws IOException, ClassNotFoundException;
}
