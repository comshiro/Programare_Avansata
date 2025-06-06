package org.example.ui;

import org.example.model.Session;
import org.example.network.MatchmakingClient;
import org.example.service.GameService;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import org.example.model.GameHistory;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainApp extends Application implements Initializable {
    private MatchmakingClient matchmakingClient;
    private final GameService gameService = new GameService();
    private ObservableList<String> playerList = FXCollections.observableArrayList();
    private ObservableList<String> openGamesList = FXCollections.observableArrayList();

    @FXML private Label welcomeLabel;
    @FXML private ListView<String> playerListView;
    @FXML private ListView<String> openGamesListView;
    @FXML private Button challengeButton;
    @FXML private Button logoutButton;
    @FXML private Button createGameButton;
    @FXML private Button joinGameButton;
    @FXML private Button bestOpponentButton;
    @FXML private Button showHistoryButton;
    @FXML private Button showLeaderboardButton; // Add leaderboard button

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/lobby-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 350, 300);
        primaryStage.setTitle("ChessBattle - Lobby");
        primaryStage.setScene(scene);
        // Add window close handler to disconnect
        primaryStage.setOnCloseRequest(event -> {
            if (matchmakingClient != null) {
                matchmakingClient.logout();
            }
        });
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Session.isLoggedIn()) {
            welcomeLabel.setText("Welcome, " + Session.getCurrentUser().getUsername() + "!");
        } else {
            welcomeLabel.setText("Welcome, guest!");
        }
        playerListView.setItems(playerList);
        playerListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        openGamesListView.setItems(openGamesList);
        openGamesListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        matchmakingClient = new MatchmakingClient();
        try {
            matchmakingClient.connect(Session.getCurrentUser().getUsername(), message -> {
                if (message.startsWith("ONLINE_USERS:")) {
                    String[] users = message.substring(13).split(",");
                    javafx.application.Platform.runLater(() -> {
                        playerList.clear();
                        for (String user : users) {
                            if (!user.equals(Session.getCurrentUser().getUsername()) && !user.isEmpty()) {
                                playerList.add(user);
                            }
                        }
                    });
                } else if (message.startsWith("CHALLENGE_FROM:")) {
                    String challenger = message.substring(15);
                    javafx.application.Platform.runLater(() -> {
                        showAlert("You have been challenged by " + challenger + "!");
                        // Start ChessApp board for challenged
                        try {
                            org.example.ChessApp.HelloApplication chessApp = new org.example.ChessApp.HelloApplication(Session.getCurrentUser().getUsername());
                            chessApp.start(new Stage());
                            // Trigger game creation immediately after window launch
                            org.example.ChessApp.BoardController controller = chessApp.getBoardController();
                            if (controller != null) {
                                controller.createNewGame();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                } else if (message.equals("USER_OFFLINE")) {
                    javafx.application.Platform.runLater(() -> showAlert("User is offline."));
                } else if (message.startsWith("OPEN_GAMES:")) {
                    // Update open games list from server
                    String[] games = message.substring(11).split(",");
                    javafx.application.Platform.runLater(() -> {
                        openGamesList.clear();
                        for (String game : games) {
                            if (!game.isEmpty()) openGamesList.add(game);
                        }
                    });
                } else if (message.startsWith("JOINED_GAME:")) {
                    // Launch chess window for joined game
                    String gameId = message.substring(12);
                    javafx.application.Platform.runLater(() -> {
                        try {
                            org.example.ChessApp.HelloApplication chessApp = new org.example.ChessApp.HelloApplication(Session.getCurrentUser().getUsername());
                            chessApp.start(new Stage());
                            org.example.ChessApp.BoardController controller = chessApp.getBoardController();
                            if (controller != null) {
                                // If this user is the creator, create the game on the chess server
                                boolean isCreator = false;
                                for (String openGame : openGamesList) {
                                    if (openGame.startsWith(gameId + " ")) {
                                        if (openGame.contains(Session.getCurrentUser().getUsername())) {
                                            isCreator = true;
                                            break;
                                        }
                                    }
                                }
                                if (isCreator) {
                                    controller.createNewGame(gameId);
                                } else {
                                    // Wait 500ms before joining to allow game creation
                                    new Thread(() -> {
                                        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
                                        javafx.application.Platform.runLater(() -> controller.joinGame(gameId));
                                    }).start();
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            });
        } catch (Exception e) {
            showAlert("Could not connect to matchmaking server.");
        }
    }

    @FXML
    private void handleChallenge(ActionEvent event) {
        String selected = playerListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a player to challenge.");
            return;
        }
        matchmakingClient.sendChallenge(selected);
        // Start ChessApp board for challenger
        try {
            org.example.ChessApp.HelloApplication chessApp = new org.example.ChessApp.HelloApplication(Session.getCurrentUser().getUsername());
            chessApp.start(new Stage());
            // Trigger game creation immediately after window launch
            org.example.ChessApp.BoardController controller = chessApp.getBoardController();
            if (controller != null) {
                controller.createNewGame();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        matchmakingClient.logout();
        Session.logout();
        ((Stage)logoutButton.getScene().getWindow()).close();
        LoginUser loginUser = new LoginUser();
        try {
            loginUser.start(new Stage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleCreateGame(ActionEvent event) {
        // Send a request to the chess server to create a new open game
        // Use the chess client to send CREATE_GAME_LOBBY (distinct from challenge flow)
        // Assume a singleton or static method to get the chess client for the lobby user
        // For demo, just send a message to the matchmaking server (or chess server if available)
        matchmakingClient.sendCreateGameLobby();
    }

    @FXML
    private void handleJoinGame(ActionEvent event) {
        String selected = openGamesListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a game to join.");
            return;
        }
        String gameId = selected.split(" ")[0];
        matchmakingClient.sendJoinGameLobby(gameId);
    }

    @FXML
    private void handleFindBestOpponent(ActionEvent event) {
        String username = Session.getCurrentUser().getUsername();
        String bestOpponent = gameService.getBestOpponent(username);
        if (bestOpponent != null) {
            showAlert("Your best opponent: " + bestOpponent);
        } else {
            showAlert("No best opponent found.");
        }
    }

    @FXML
    private void handleShowHistory(ActionEvent event) {
        String username = Session.getCurrentUser().getUsername();
        java.util.List<GameHistory> historyList = gameService.getGameHistoryList(username);
        if (historyList == null || historyList.isEmpty()) {
            showAlert("No game history found.");
            return;
        }
        // Create TableView for GameHistory
        // Remove the movesCol from the TableView
        TableView<GameHistory> table = new TableView<>();
        TableColumn<GameHistory, Integer> idCol = new TableColumn<>("Game #");
        idCol.setCellValueFactory(new PropertyValueFactory<>("gameId"));
        TableColumn<GameHistory, String> p1Col = new TableColumn<>("Player 1");
        p1Col.setCellValueFactory(new PropertyValueFactory<>("player1"));
        TableColumn<GameHistory, String> p2Col = new TableColumn<>("Player 2");
        p2Col.setCellValueFactory(new PropertyValueFactory<>("player2"));
        TableColumn<GameHistory, String> resultCol = new TableColumn<>("Result");
        resultCol.setCellValueFactory(new PropertyValueFactory<>("result"));
        TableColumn<GameHistory, Integer> durCol = new TableColumn<>("Duration");
        durCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        table.getColumns().addAll(idCol, p1Col, p2Col, resultCol, durCol);
        table.getItems().addAll(historyList);
        table.setPrefWidth(800);
        table.setPrefHeight(400);
        VBox vbox = new VBox(table);
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Game History");
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    @FXML
    private void handleShowLeaderboard(ActionEvent event) {
        // Call the procedure to recalculate the leaderboard before displaying
        try {
            org.example.service.GameService gs = new org.example.service.GameService();
            java.lang.reflect.Field urlField = gs.getClass().getDeclaredField("DB_URL");
            java.lang.reflect.Field userField = gs.getClass().getDeclaredField("DB_USER");
            java.lang.reflect.Field passField = gs.getClass().getDeclaredField("DB_PASS");
            urlField.setAccessible(true);
            userField.setAccessible(true);
            passField.setAccessible(true);
            String url = (String) urlField.get(gs);
            String user = (String) userField.get(gs);
            String pass = (String) passField.get(gs);
            java.sql.Connection conn = java.sql.DriverManager.getConnection(url, user, pass);
            java.sql.CallableStatement stmt = conn.prepareCall("{call calculateLeaderboard}");
            stmt.execute();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            showAlert("Error updating leaderboard: " + e.getMessage());
            return;
        }
        java.util.List<org.example.model.LeaderboardEntry> leaderboard = gameService.getLeaderboard();
        if (leaderboard == null || leaderboard.isEmpty()) {
            showAlert("No leaderboard data found.");
            return;
        }
        TableView<org.example.model.LeaderboardEntry> table = new TableView<>();
        TableColumn<org.example.model.LeaderboardEntry, String> userCol = new TableColumn<>("Username");
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<org.example.model.LeaderboardEntry, Integer> pointsCol = new TableColumn<>("Points");
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));
        TableColumn<org.example.model.LeaderboardEntry, Integer> gamesCol = new TableColumn<>("Games");
        gamesCol.setCellValueFactory(new PropertyValueFactory<>("gamesPlayed"));
        TableColumn<org.example.model.LeaderboardEntry, Integer> winsCol = new TableColumn<>("Wins");
        winsCol.setCellValueFactory(new PropertyValueFactory<>("wins"));
        TableColumn<org.example.model.LeaderboardEntry, Integer> lossesCol = new TableColumn<>("Losses");
        lossesCol.setCellValueFactory(new PropertyValueFactory<>("losses"));
        table.getColumns().addAll(userCol, pointsCol, gamesCol, winsCol, lossesCol);
        table.getItems().addAll(leaderboard);
        table.setPrefWidth(600);
        table.setPrefHeight(400);
        VBox vbox = new VBox(table);
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Leaderboard");
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    @FXML
    private void handleShowWinrate(ActionEvent event) {
        String username = Session.getCurrentUser().getUsername();
        double winrate = 0.0;
        try {
            // Use static getters for DB connection info
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                GameService.getDbUrl(),
                GameService.getDbUser(),
                GameService.getDbPass()
            );
            int playerId = -1;
            try (java.sql.PreparedStatement stmt = conn.prepareStatement("SELECT id FROM Players WHERE username = ?")) {
                stmt.setString(1, username);
                try (java.sql.ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) playerId = rs.getInt(1);
                }
            }
            if (playerId != -1) {
                try (java.sql.CallableStatement stmt = conn.prepareCall("{? = call getWinRate(?)}")) {
                    stmt.registerOutParameter(1, java.sql.Types.DOUBLE);
                    stmt.setInt(2, playerId);
                    stmt.execute();
                    winrate = stmt.getDouble(1);
                }
            }
            conn.close();
        } catch (Exception e) {
            showAlert("Error fetching winrate: " + e.getMessage());
            return;
        }
        showAlert("Your winrate: " + String.format("%.2f", winrate * 100) + "%");
    }

    @FXML
    private void handleShowWinStreak(ActionEvent event) {
        String username = Session.getCurrentUser().getUsername();
        int winStreak = 0;
        try {
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                GameService.getDbUrl(),
                GameService.getDbUser(),
                GameService.getDbPass()
            );
            int playerId = -1;
            try (java.sql.PreparedStatement stmt = conn.prepareStatement("SELECT id FROM Players WHERE username = ?")) {
                stmt.setString(1, username);
                try (java.sql.ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) playerId = rs.getInt(1);
                }
            }
            if (playerId != -1) {
                try (java.sql.CallableStatement stmt = conn.prepareCall("{? = call getLongestWinStreak(?)}")) {
                    stmt.registerOutParameter(1, java.sql.Types.INTEGER);
                    stmt.setInt(2, playerId);
                    stmt.execute();
                    winStreak = stmt.getInt(1);
                }
            }
            conn.close();
        } catch (Exception e) {
            showAlert("Error fetching win streak: " + e.getMessage());
            return;
        }
        showAlert("Your longest win streak: " + winStreak);
    }

    @FXML
    private void handleShowOpening(ActionEvent event) {
        String opening = null;
        try {
            java.sql.Connection conn = java.sql.DriverManager.getConnection(
                GameService.getDbUrl(),
                GameService.getDbUser(),
                GameService.getDbPass()
            );
            try (java.sql.CallableStatement stmt = conn.prepareCall("{? = call getMostCommonOpening}")) {
                stmt.registerOutParameter(1, java.sql.Types.VARCHAR);
                stmt.execute();
                opening = stmt.getString(1);
            }
            conn.close();
        } catch (Exception e) {//sql exc needed
            showAlert("Error fetching most common opening: " + e.getMessage());
            return;
        }
        if (opening == null || opening.isEmpty()) {
            showAlert("No opening move data found.");
        } else {
            showAlert("Most common opening move: " + opening);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
