package org.example.lab6;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

public class DrawingPanel extends JPanel {
    private final MainFrame frame;
    private final List<Point> dots = new ArrayList<>();
    private final List<Line> lines = new ArrayList<>();
    private Point selectedDot = null;
    private boolean isPlayerOneTurn = true;

    private boolean isPlayerOneAI = false;
    private boolean isPlayerTwoAI = false;
    private String aiDifficulty = "Medium";

    public void startGame(boolean isPlayerOneAI, boolean isPlayerTwoAI, String aiDifficulty) {
        this.isPlayerOneAI = isPlayerOneAI;
        this.isPlayerTwoAI = isPlayerTwoAI;
        this.aiDifficulty = aiDifficulty;

        // Use the number of dots selected in the ConfigPanel spinner
        int numDots = (Integer) frame.configPanel.spinner.getValue();
        createDots(numDots);
    }

    public void aiMove() {
        if (isPlayerOneAI && isPlayerOneTurn) {
            makeAIMove(aiDifficulty, 0);
            isPlayerOneTurn = false;
        } else if (isPlayerTwoAI && !isPlayerOneTurn) {
            makeAIMove(aiDifficulty, 1);
            isPlayerOneTurn = true;
        }
        repaint();
    }

    private void makeAIMove(String difficulty, int player) {
        List<Point> aiPoints = new ArrayList<>(dots);
        List<List<MST.Edge>> spanningTrees = MST.generateSpanningTrees(aiPoints, 5);

        List<MST.Edge> selectedTree;

        if (difficulty.equals("Easy")) {
            selectedTree = MST.getWorstSpanningTree(spanningTrees, aiPoints);
        } else if (difficulty.equals("Medium")) {
            selectedTree = MST.getBestSpanningTree(spanningTrees, aiPoints);
        } else {
            selectedTree = MST.getBestSpanningTree(spanningTrees, aiPoints);
        }

        for (MST.Edge edge : selectedTree) {
            lines.add(new Line(dots.get(edge.u), dots.get(edge.v), isPlayerOneTurn ? Color.BLUE : Color.RED));
        }

        isPlayerOneTurn = !isPlayerOneTurn;
        repaint();
    }

    public DrawingPanel(MainFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(600, 400));
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point clicked = getDotAt(e.getX(), e.getY());
                if (clicked != null) {
                    if (selectedDot == null) {
                        selectedDot = clicked;
                    } else {
                        if (!selectedDot.equals(clicked)) {
                            lines.add(new Line(selectedDot, clicked, isPlayerOneTurn ? Color.BLUE : Color.RED));
                            isPlayerOneTurn = !isPlayerOneTurn;
                            repaint();

                            aiMove();
                        }
                        selectedDot = null;
                        repaint();
                    }
                }
            }
        });
    }

    public void createDots(int count) {
        dots.clear();
        lines.clear();
        selectedDot = null;

        Random random = new Random();
        int margin = 20;
        int width = getWidth() > 0 ? getWidth() : getPreferredSize().width;
        int height = getHeight() > 0 ? getHeight() : getPreferredSize().height;

        for (int i = 0; i < count; i++) {
            int x = random.nextInt(width - 2 * margin) + margin;
            int y = random.nextInt(height - 2 * margin) + margin;
            dots.add(new Point(x, y));
        }

        repaint();
    }

    private Point getDotAt(int x, int y) {
        for (Point p : dots) {
            if (p.distance(x, y) < 10) return p;
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Line line : lines) {
            g.setColor(line.color);
            g.drawLine(line.p1.x, line.p1.y, line.p2.x, line.p2.y);
        }

        g.setColor(Color.BLACK);
        for (Point p : dots) {
            g.fillOval(p.x - 5, p.y - 5, 10, 10);
        }

        if (selectedDot != null) {
            g.setColor(Color.GREEN);
            g.drawOval(selectedDot.x - 7, selectedDot.y - 7, 14, 14);
        }
    }

    public void saveToStream(ObjectOutputStream out) throws IOException {
        out.writeObject(dots);
        out.writeObject(lines);
    }

    public void loadFromStream(ObjectInputStream in) throws IOException, ClassNotFoundException {
        dots.clear();
        lines.clear();
        dots.addAll((List<Point>) in.readObject());
        lines.addAll((List<Line>) in.readObject());
        repaint();
    }

    public void exportToPNG() {
        int width = getWidth();
        int height = getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        paintComponent(g2d);

        g2d.dispose();

        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Image as PNG");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Images", "png"));

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                ImageIO.write(image, "PNG", fileToSave);
                JOptionPane.showMessageDialog(this, "Game board exported successfully!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving the game board.");
        }
    }

    public double getPlayerScore(Color color) {
        return lines.stream()
                .filter(line -> line.color.equals(color))
                .mapToDouble(Line::length)
                .sum();
    }

    public double getBestPossibleScore() {
        return MST.calculateMST(dots);
    }

    public String compareScores() {
        double bestScore = getBestPossibleScore();
        double blueScore = getPlayerScore(Color.BLUE);
        double redScore = getPlayerScore(Color.RED);

        String result = "Best Score: " + bestScore + "\n";
        result += "Blue Score: " + blueScore + " (Difference: " + Math.abs(blueScore - bestScore) + ")\n";
        result += "Red Score: " + redScore + " (Difference: " + Math.abs(redScore - bestScore) + ")";
        return result;
    }

    private static class Line implements Serializable {
        Point p1, p2;
        Color color;

        Line(Point p1, Point p2, Color color) {
            this.p1 = p1;
            this.p2 = p2;
            this.color = color;
        }

        double length() {
            return p1.distance(p2);
        }
    }
}
