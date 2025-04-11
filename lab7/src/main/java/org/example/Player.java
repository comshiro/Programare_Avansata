package org.example;

public class Player {
    private String name;
    private Game game;
    private boolean running;
    public Player(String name) { this.name = name; }
    private boolean submitWord() {
        List<Tile> extracted = game.getBag().extractTiles(7);
        if (extracted.isEmpty()) {
            return false;
        }
        create a word with all the extracted tiles;
        game.getBoard().addWord(this, word);
        make the player sleep 500ms;
        return true;
    }
}
