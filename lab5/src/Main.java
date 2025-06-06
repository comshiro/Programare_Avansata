import java.time.LocalDate;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InvalidCommandException, InvalidRepositoryException, IOException {
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

        Repository rep = new Repository("My Image Repository");
        Service serv = new Service();
        serv.save(rep, "repository_new");
        // Simulate adding some images to the repository
        repo.add(new Image("image1.jpg", java.time.LocalDate.now(), "/images/img1.png"));
        repo.add(new Image("image2.jpg", java.time.LocalDate.now(), "/images/img2.png"));

        Shell shell = new Shell(rep);
        shell.start();
    }
}
