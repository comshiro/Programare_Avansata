import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONFormat implements RepositoryFormat {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void save(Repository repo, String path) throws IOException {

    }

    @Override
    public Repository load(String path) throws IOException, ClassNotFoundException {
        return null;
    }

}
