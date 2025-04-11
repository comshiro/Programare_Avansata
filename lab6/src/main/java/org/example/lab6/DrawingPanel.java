package org.example.lab6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class DrawingPanel extends JPanel {
    private final MainFrame frame;
    private final List<Point> dots = new ArrayList<>();
    private final List<Line> lines = new ArrayList<>();
    private Point selectedDot = null;
    private boolean isPlayerOneTurn = true;

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

        // Draw lines
        for (Line line : lines) {
            g.setColor(line.color);
            g.drawLine(line.p1.x, line.p1.y, line.p2.x, line.p2.y);
        }

        // Draw dots
        g.setColor(Color.BLACK);
        for (Point p : dots) {
            g.fillOval(p.x - 5, p.y - 5, 10, 10);
        }

        // Highlight selected dot
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

    private static class Line implements Serializable {
        Point p1, p2;
        Color color;

        Line(Point p1, Point p2, Color color) {
            this.p1 = p1;
            this.p2 = p2;
            this.color = color;
        }
    }
}
