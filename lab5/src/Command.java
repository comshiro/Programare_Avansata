import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public interface Command {
    String getCommandName();  // Instead of a field, use a getter method
    List<String> getOptions();
    String getPath();
    int execute() throws InvalidCommandException, ImageNotFoundException, InvalidRepositoryException;;
}
