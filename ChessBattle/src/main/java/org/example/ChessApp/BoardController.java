package org.example.ChessApp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.io.InputStream;

public class BoardController {
    @FXML private Label turnLabel;
    @FXML private GridPane chessBoard;
    @FXML private Label statusLabel;
    @FXML private TextField gameIdField;

    private Image[][] pieceImages = new Image[2][6];
    private int[][] boardState = new int[8][8];
    private String playerId;
    private String gameId;
    private String playerColor;
    private int selectedRow = -1, selectedCol = -1;
    private ChessClient chessClient;
    private boolean isWhiteTurn = true;
    private boolean gameStarted = false;

    private static String staticPlayerId = null;

    public static void setPlayerId(String playerId) {
        staticPlayerId = playerId;
    }

    @FXML
    public void initialize() {
        loadPieceImages();
        setupConnection();
        initializeEmptyBoard();
        drawBoard();
    }

    private void updateTurnIndicator(String turn) {
        turnLabel.setText("Turn: " + turn);
        turnLabel.getStyleClass().removeAll("your-turn", "opponent-turn");

        if (turn.equals(playerColor)) {
            turnLabel.getStyleClass().add("your-turn");
        } else {
            turnLabel.getStyleClass().add("opponent-turn");
        }
    }

    public void setPlayerColor(String color) {
        playerColor = color;
        statusLabel.setText("You are playing as " + color);
        gameStarted = true;
        updateTurnIndicator("WHITE"); // White always starts first
        System.out.println("Player color set to: " + color);
    }

    private void loadPieceImages() {
        String[] pieces = {"pawn", "rook", "knight", "bishop", "queen", "king"};

        for (int i = 0; i < pieces.length; i++) {
            String whitePath = "/pieces/white-" + pieces[i] + ".png";
            String blackPath = "/pieces/black-" + pieces[i] + ".png";

            try (InputStream whiteStream = getClass().getResourceAsStream(whitePath);
                 InputStream blackStream = getClass().getResourceAsStream(blackPath)) {

                if (whiteStream != null) {
                    pieceImages[0][i] = new Image(whiteStream);
                    System.out.println("Loaded from stream: " + whitePath);
                } else {
                    System.err.println("Stream not found: " + whitePath);
                }

                if (blackStream != null) {
                    pieceImages[1][i] = new Image(blackStream);
                    System.out.println("Loaded from stream: " + blackPath);
                } else {
                    System.err.println("Stream not found: " + blackPath);
                }
            } catch (IOException e) {
                System.err.println("Error loading images: " + e.getMessage());
            }
        }
    }

    private void setupConnection() {
        // Use staticPlayerId if set, otherwise fallback
        playerId = staticPlayerId != null ? staticPlayerId : ("PLAYER" + System.currentTimeMillis());
        chessClient = new ChessClient(this);
        chessClient.connectToServer();
        chessClient.sendMessage("CONNECT:" + playerId);
    }

    public void createNewGame() {
        chessClient.sendMessage("CREATE_GAME");
    }

    public void createNewGame(String gameId) {
        this.gameId = gameId;
        chessClient.sendMessage("CREATE_GAME_WITH_ID:" + gameId);
    }

    public void joinGame(String gameId) {
        this.gameId = gameId;
        chessClient.sendMessage("JOIN_GAME:" + gameId);
    }

    private void initializeEmptyBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardState[i][j] = 0;
            }
        }
    }

    public void updateBoard(String boardData) {
        System.out.println("Updating board with data: " + boardData);
        String[] values = boardData.split(",");
        int index = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (index < values.length) {
                    boardState[i][j] = Integer.parseInt(values[index].trim());
                    index++;
                }
            }
        }
        drawBoard();
        System.out.println("Board updated successfully");

        // FIX: Enable game when board is updated (indicates game has started)
        if (!gameStarted && gameId != null) {
            gameStarted = true;
            playerColor = "WHITE"; // Default to white for single player testing
            statusLabel.setText("Game started! You are playing as WHITE");
            System.out.println("Game automatically started for single player mode");
        }
    }

    public void updateTurn(String turn) {
        System.out.println("Turn updated to: " + turn);
        isWhiteTurn = turn.equals("WHITE");
        updateTurnIndicator(turn);
    }

    private void drawBoard() {
        chessBoard.getChildren().clear();

        // Clear any existing constraints
        chessBoard.getRowConstraints().clear();
        chessBoard.getColumnConstraints().clear();

        // Create equal constraints for all rows and columns
        for (int i = 0; i < 8; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / 8);
            chessBoard.getRowConstraints().add(row);

            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / 8);
            chessBoard.getColumnConstraints().add(col);
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                StackPane square = createSquare(row, col);
                chessBoard.add(square, col, row);
            }
        }
    }

    private StackPane createSquare(int row, int col) {
        StackPane square = new StackPane();
        square.getStyleClass().add((row + col) % 2 == 0 ? "light-square" : "dark-square");

        int piece = boardState[row][col];
        if (piece != 0) {
            ImageView pieceView = createPieceView(piece);
            square.getChildren().add(pieceView);
        }

        square.setOnMouseClicked(this::handleSquareClick);
        return square;
    }

    private ImageView createPieceView(int pieceCode) {
        int colorIndex = (pieceCode > 6) ? 1 : 0;
        int pieceType = (pieceCode - 1) % 6;
        Image image = pieceImages[colorIndex][pieceType];

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);
        return imageView;
    }

    private void handleSquareClick(MouseEvent event) {
        // FIX: Improved game start check
        if (!gameStarted || gameId == null) {
            System.out.println("Game not started - cannot move");
            showMessage("Game not started yet");
            return;
        }

        // For single player mode, allow moves if no playerColor is set
        if (playerColor == null) {
            playerColor = "WHITE";
            statusLabel.setText("Single player mode - You are WHITE");
        }

        // Check if it's the player's turn
        String currentTurn = isWhiteTurn ? "WHITE" : "BLACK";
        if (!currentTurn.equals(playerColor)) {
            System.out.println("Not your turn - current: " + currentTurn + ", you are: " + playerColor);
            showMessage("It's not your turn!");
            return;
        }

        StackPane square = (StackPane) event.getSource();
        Integer col = GridPane.getColumnIndex(square);
        Integer row = GridPane.getRowIndex(square);

        // Invert row number to match server's coordinate system
        int serverRow = 7 - row;
        int serverCol = col;

        System.out.printf("Clicked: client row=%d, col=%d → server row=%d, col=%d, piece=%d%n",
                row, col, serverRow, serverCol, boardState[row][col]);

        if (selectedRow == -1) {
            // First click - select piece
            if (boardState[row][col] != 0) {
                // Check if the piece belongs to the current player
                int piece = boardState[row][col];
                boolean isPieceWhite = piece <= 6;
                boolean isPlayerWhite = playerColor.equals("WHITE");

                if (isPieceWhite == isPlayerWhite) {
                    selectedRow = row;
                    selectedCol = col;
                    square.getStyleClass().add("selected");
                    System.out.printf("Selected piece at (client: %d,%d) → (server: %d,%d)%n",
                            row, col, serverRow, serverCol);
                } else {
                    showMessage("You can only select your own pieces!");
                }
            }
        } else {
            // Second click - move piece
            int fromServerRow = 7 - selectedRow;
            int fromServerCol = selectedCol;
            int toServerRow = 7 - row;
            int toServerCol = col;

            System.out.printf("Attempting move from (client: %d,%d)→(%d,%d) → server (%d,%d)→(%d,%d)%n",
                    selectedRow, selectedCol, row, col,
                    fromServerRow, fromServerCol, toServerRow, toServerCol);

            chessClient.sendMessage(
                    "MOVE:" + fromServerRow + ":" + fromServerCol + ":" +
                            toServerRow + ":" + toServerCol
            );

            // Clear selection
            chessBoard.getChildren().forEach(node ->
                    node.getStyleClass().remove("selected"));
            selectedRow = -1;
            selectedCol = -1;
        }
    }

    public void showMessage(String message) {
        statusLabel.setText(message);
        System.out.println("Status: " + message);
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
        statusLabel.setText("Game created: " + gameId + " - Single player mode enabled");

        // FIX: Enable single player mode immediately when game is created
        if (!gameStarted) {
            gameStarted = true;
            playerColor = "WHITE";
            statusLabel.setText("Game created: " + gameId + " - You are WHITE (Single Player)");
        }
    }

    @FXML
    private void handleCreateGame() {
        createNewGame();
    }

    @FXML
    private void handleJoinGame() {
        String gameId = gameIdField.getText().trim();
        if (!gameId.isEmpty()) {
            joinGame(gameId);
        } else {
            statusLabel.setText("Please enter a game ID");
        }
    }

    @FXML
    private void handleSurrender() {
        if (gameId != null && chessClient != null) {
            chessClient.sendMessage("SURRENDER:" + gameId);
            showMessage("You surrendered. Game over.");
        } else {
            showMessage("Cannot surrender: not in a game.");
        }
    }
}