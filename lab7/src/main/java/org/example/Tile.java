package org.example;

/**
 * @param letter made final since it shouldn't change
 * @param points made final since it shouldn't change
 */
public record Tile(char letter, int points) {
    public Tile(char letter, int points) {
        // Convert to lowercase for consistency
        this.letter = Character.toLowerCase(letter);
        this.points = points;
    }

    @Override
    public String toString() {
        // More compact representation that's useful for debugging
        return letter + "(" + points + ")";
    }

    @Override
    public int hashCode() {
        return 31 * letter + points;
    }
}