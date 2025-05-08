package org.example.lab6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class ControlPanel extends JPanel {
    final MainFrame frame;
    JButton loadBtn = new JButton("Load");
    JButton saveBtn = new JButton("Save");
    JButton resetBtn = new JButton("Reset");
    JButton exitBtn = new JButton("Exit");
    JButton compareScoresBtn = new JButton("Compare Scores");
    JButton exportBtn = new JButton("Export to PNG");

    public ControlPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        setLayout(new GridLayout(1, 6));

        add(loadBtn);
        add(saveBtn);
        add(resetBtn);
        add(exitBtn);
        add(compareScoresBtn);
        add(exportBtn);


        exitBtn.addActionListener(this::exitGame);
        resetBtn.addActionListener(this::resetGame);
        loadBtn.addActionListener(this::loadGame);
        saveBtn.addActionListener(this::saveGame);
        compareScoresBtn.addActionListener(this::compareScores);
        exportBtn.addActionListener(this::exportGame);
    }

    private void exportGame(ActionEvent actionEvent) {
        frame.canvas.exportToPNG();
    }

    private void compareScores(ActionEvent e) {
        String comparison = frame.canvas.compareScores();
        JOptionPane.showMessageDialog(frame, comparison);
    }

    private void exitGame(ActionEvent e) {
        int choice = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?");
        if (choice == JOptionPane.YES_OPTION) {
            frame.dispose();
        }
    }

    private void resetGame(ActionEvent e) {
        int numDots = (Integer) frame.configPanel.spinner.getValue();
        frame.canvas.createDots(numDots);
    }

    private void saveGame(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(chooser.getSelectedFile()))) {
                frame.canvas.saveToStream(out);
                JOptionPane.showMessageDialog(frame, "Game saved!");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to save game.");
            }
        }
    }

    private void loadGame(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(chooser.getSelectedFile()))) {
                frame.canvas.loadFromStream(in);
                JOptionPane.showMessageDialog(frame, "Game loaded!");
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Failed to load game.");
            }
        }
    }
}
