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
import org.example.model.Session;
import org.example.service.LoginService;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginUser extends Application implements Initializable {
    private final LoginService loginService = new LoginService();

    @FXML private TextField userField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label messageLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/login-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 350, 180);
        primaryStage.setTitle("ChessBattle - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // No-op
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String user = userField.getText();
        String password = passwordField.getText();
        if (user.isEmpty() || password.isEmpty()) {
            messageLabel.setText("All fields are required.");
            return;
        }
        try {
            Player player = loginService.authenticate(user, password);
            if (player != null) {
                Session.login(player);
                messageLabel.setText("Welcome, " + player.getUsername() + "!");
                ((Stage)loginButton.getScene().getWindow()).close();
                // Open main app window
                MainApp mainApp = new MainApp();
                try {
                    mainApp.start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                messageLabel.setText("Invalid credentials.");
            }
        } catch (Exception ex) {
            messageLabel.setText("Error: " + ex.getMessage());
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            RegisterUser registerUser = new RegisterUser();
            registerUser.start(new Stage());
            ((Stage)registerButton.getScene().getWindow()).close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
