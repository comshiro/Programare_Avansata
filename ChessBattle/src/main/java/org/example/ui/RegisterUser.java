package org.example.ui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.fxml.Initializable;
import org.example.model.Player;
import org.example.service.RegistrationService;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterUser extends Application implements Initializable {
    private final RegistrationService registrationService = new RegistrationService();

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button registerButton;
    @FXML private Label messageLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/register-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 350, 220);
        primaryStage.setTitle("ChessBattle - User Registration");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // No-op
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("All fields are required.");
            return;
        }
        Player player = new Player(username, email, null);
        try {
            if (registrationService.registerPlayer(player, password)) {
                messageLabel.setText("Registration successful! Please log in.");
                // Close registration and open login window
                ((Stage)registerButton.getScene().getWindow()).close();
                new LoginUser().start(new Stage());
            } else {
                messageLabel.setText("Username or email already exists.");
            }
        } catch (Exception ex) {
            messageLabel.setText("Error: " + ex.getMessage());
        }
    }
}
