package org.example.ui;

import org.example.model.Session;
import org.example.service.GameService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class ChessGameWindow extends Application {
    private final String opponent;
    private final List<String> moves = new ArrayList<>();
    private final GameService gameService = new GameService();

    public ChessGameWindow(String opponent) {
        this.opponent = opponent;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chess Game vs " + opponent);
        Label moveLabel = new Label("Enter move notation (e.g., e4, Nf3):");
        TextField moveField = new TextField();
        Button addMoveButton = new Button("Add Move");
        Label movesLabel = new Label("Moves: ");
        Button endGameButton = new Button("End Game");

        addMoveButton.setOnAction(e -> {
            String move = moveField.getText();
            if (!move.isEmpty()) {
                moves.add(move);
                movesLabel.setText("Moves: " + String.join(", ", moves));
                moveField.clear();
            }
        });

        endGameButton.setOnAction(e -> {
            // For demo, ask for result and duration
            String result = "draw"; // You can add a dialog to choose result
            int duration = moves.size(); // For demo, use move count as duration
            gameService.saveGame(Session.getCurrentUser().getUsername(), opponent, result, duration, String.join(",", moves));
            primaryStage.close();
        });

        VBox vbox = new VBox(10, moveLabel, moveField, addMoveButton, movesLabel, endGameButton);
        vbox.setPadding(new Insets(20));
        Scene scene = new Scene(vbox, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
