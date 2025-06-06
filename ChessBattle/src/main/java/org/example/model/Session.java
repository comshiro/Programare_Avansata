package org.example.model;

public class Session {
    private static Player currentUser = null;

    public static void login(Player player) {
        currentUser = player;
    }

    public static void logout() {
        currentUser = null;
    }

    public static Player getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
