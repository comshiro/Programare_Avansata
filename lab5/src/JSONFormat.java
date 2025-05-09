import java.io.File;
import java.io.IOException;


public class JSONFormat implements RepositoryFormat {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void save(Repository repo, String path) throws IOException {
        objectMapper.writeValue(new File(path), repo);
    }

    @Override
    public Repository load(String path) throws IOException {
        return objectMapper.readValue(new File(path), Repository.class);
    }
}
