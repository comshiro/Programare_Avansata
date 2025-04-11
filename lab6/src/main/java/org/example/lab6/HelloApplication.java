package org.example.lab6;

import javax.swing.SwingUtilities;

public class HelloApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}
