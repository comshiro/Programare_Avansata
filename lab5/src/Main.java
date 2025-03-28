import java.time.LocalDate;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Repository repo = new Repository("Test");

        Image img1 = new Image("img.png", LocalDate.now(), "images/img.png");
        repo.add(img1);

        Service service = new Service();
        service.view(img1);
        System.out.println(repo);

        try {
            service.save(repo, "repository");
        } catch (IOException e) {
            System.out.println("Error saving repository: " + e.getMessage());
        }

    }
}
