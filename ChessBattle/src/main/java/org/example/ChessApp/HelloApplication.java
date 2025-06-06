package org.example.ChessApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private String playerId;
    private BoardController boardController;

    public HelloApplication() {
        this.playerId = null;
    }
    public HelloApplication(String playerId) {
        this.playerId = playerId;
    }

    public BoardController getBoardController() {
        return boardController;
    }

    @Override
    public void start(Stage stage) throws IOException {
        if (playerId != null) {
            BoardController.setPlayerId(playerId);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/chess-app/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 720);
        scene.getStylesheets().add(getClass().getResource("/chess-app/board.css").toExternalForm());
        boardController = fxmlLoader.getController();
        stage.setTitle("Chess Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}