package lab11;

import javax.swing.*;

import java.awt.*;
import java.util.stream.IntStream;

@SuppressWarnings("serial")
class DrawPanel extends JPanel {

    private final int bacteriaSize;
    private final int baseOffset;

    private boolean[][] generation;


    public DrawPanel(boolean[][] generation, int bacteriaSize, int baseOffset)
    {
        this.generation = generation;
        this.bacteriaSize = bacteriaSize;
        this.baseOffset = baseOffset;
        setSize(generation.length * bacteriaSize, generation.length * bacteriaSize);
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(generation, g);
    }

    public void setGeneration(boolean[][] generation)
    {
        this.generation = generation;
    }

    private void doDrawing(boolean[][] generation, Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        int offset = bacteriaSize - (bacteriaSize - 1) + baseOffset;

        IntStream.range(0, generation.length).
            forEach(y -> IntStream.range(0, generation.length).
                filter(x -> generation[y][x]).forEach(x -> g2d.drawOval(x * bacteriaSize + offset, y * bacteriaSize + offset, bacteriaSize, bacteriaSize)));
    }
}

