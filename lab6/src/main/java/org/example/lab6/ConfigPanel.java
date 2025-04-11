package org.example.lab6;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ConfigPanel extends JPanel {
    final MainFrame frame;
    JLabel label;
    JSpinner spinner;
    JButton createButton;

    public ConfigPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        label = new JLabel("Number of dots:");
        spinner = new JSpinner(new SpinnerNumberModel(10, 2, 100, 1));

        createButton = new JButton("New Game");
        createButton.addActionListener(this::createGame);

        add(label);
        add(spinner);
        add(createButton);
    }

    private void createGame(ActionEvent e) {
        int numDots = (Integer) spinner.getValue();
        frame.canvas.createDots(numDots);
    }
}
