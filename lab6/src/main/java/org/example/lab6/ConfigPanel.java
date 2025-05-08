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

        // Player One Options
        humanPlayerOneButton = new JRadioButton("Human Player 1");
        aiPlayerOneButton = new JRadioButton("AI Player 1");
        ButtonGroup groupPlayerOne = new ButtonGroup();
        groupPlayerOne.add(humanPlayerOneButton);
        groupPlayerOne.add(aiPlayerOneButton);
        humanPlayerOneButton.setSelected(true);

        JPanel playerOnePanel = new JPanel(new GridLayout(0, 1));
        playerOnePanel.setBorder(BorderFactory.createTitledBorder("Player 1"));
        playerOnePanel.add(humanPlayerOneButton);
        playerOnePanel.add(aiPlayerOneButton);

        // Player Two Options
        humanPlayerTwoButton = new JRadioButton("Human Player 2");
        aiPlayerTwoButton = new JRadioButton("AI Player 2");
        ButtonGroup groupPlayerTwo = new ButtonGroup();
        groupPlayerTwo.add(humanPlayerTwoButton);
        groupPlayerTwo.add(aiPlayerTwoButton);
        humanPlayerTwoButton.setSelected(true);

        JPanel playerTwoPanel = new JPanel(new GridLayout(0, 1));
        playerTwoPanel.setBorder(BorderFactory.createTitledBorder("Player 2"));
        playerTwoPanel.add(humanPlayerTwoButton);
        playerTwoPanel.add(aiPlayerTwoButton);

        // AI Difficulty
        JLabel aiDifficultyLabel = new JLabel("AI Difficulty:");
        aiDifficultyComboBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        aiDifficultyComboBox.setToolTipText("Affects AI decision-making quality");

        // Layout this panel
        setLayout(new GridLayout(0, 1, 10, 10));  // Flexible rows

        add(label);
        add(spinner);
        add(createButton);
        add(playerOnePanel);
        add(playerTwoPanel);
        add(aiDifficultyLabel);
        add(aiDifficultyComboBox);
    }

    private void createGame(ActionEvent e) {
        int numDots = (Integer) spinner.getValue();
        frame.canvas.createDots(numDots);

        boolean isPlayerOneAI = aiPlayerOneButton.isSelected();
        boolean isPlayerTwoAI = aiPlayerTwoButton.isSelected();
        String aiDifficulty = (String) aiDifficultyComboBox.getSelectedItem();

        frame.startNewGame(isPlayerOneAI, isPlayerTwoAI, aiDifficulty);
    }
}
