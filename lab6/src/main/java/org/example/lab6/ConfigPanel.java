package org.example.lab6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ConfigPanel extends JPanel {
    final MainFrame frame;
    JLabel label;
    JSpinner spinner;
    JButton createButton;
    JRadioButton humanPlayerOneButton;
    JRadioButton aiPlayerOneButton;
    JRadioButton humanPlayerTwoButton;
    JRadioButton aiPlayerTwoButton;
    JComboBox<String> aiDifficultyComboBox;

    public ConfigPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        label = new JLabel("Number of dots:");
        spinner = new JSpinner(new SpinnerNumberModel(10, 2, 100, 1));

        createButton = new JButton("New Game");
        createButton.addActionListener(this::createGame);

        humanPlayerOneButton = new JRadioButton("Human Player 1");
        aiPlayerOneButton = new JRadioButton("AI Player 1");
        humanPlayerTwoButton = new JRadioButton("Human Player 2");
        aiPlayerTwoButton = new JRadioButton("AI Player 2");

        ButtonGroup groupPlayerOne = new ButtonGroup();
        groupPlayerOne.add(humanPlayerOneButton);
        groupPlayerOne.add(aiPlayerOneButton);

        ButtonGroup groupPlayerTwo = new ButtonGroup();
        groupPlayerTwo.add(humanPlayerTwoButton);
        groupPlayerTwo.add(aiPlayerTwoButton);

        humanPlayerOneButton.setSelected(true);
        humanPlayerTwoButton.setSelected(true);

        JLabel aiDifficultyLabel = new JLabel("AI Difficulty:");
        aiDifficultyComboBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});

        setLayout(new GridLayout(6, 1, 10, 10)); // Grid layout pentru un layout clar și ordonat

        add(label);
        add(spinner);
        add(createButton);
        add(humanPlayerOneButton);
        add(aiPlayerOneButton);
        add(humanPlayerTwoButton);
        add(aiPlayerTwoButton);
        add(aiDifficultyLabel);
        add(aiDifficultyComboBox);
    }


    private void createGame(ActionEvent e) {
        int numDots = (Integer) spinner.getValue();
        frame.canvas.createDots(numDots);

        // Set up the game with AI or human players based on selected options
        boolean isPlayerOneAI = aiPlayerOneButton.isSelected();
        boolean isPlayerTwoAI = aiPlayerTwoButton.isSelected();
        String aiDifficulty = (String) aiDifficultyComboBox.getSelectedItem();

        // Pass the configuration to the MainFrame to start the game with the selected settings
        frame.startNewGame(isPlayerOneAI, isPlayerTwoAI, aiDifficulty);
    }
}
