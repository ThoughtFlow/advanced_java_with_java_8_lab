package lab11;

import javax.swing.*;

@SuppressWarnings("serial")
public class View extends JFrame
{
    private DrawPanel panel;
    private final String method;

    public View(String method, boolean[][] generation, int bacteriaSize)
    {
        setSize(generation.length, generation.length);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new DrawPanel(generation, bacteriaSize, bacteriaSize * 30 / 2);
        add(panel);
        setSize(generation.length * bacteriaSize + (bacteriaSize * 30), generation.length * bacteriaSize + (bacteriaSize * 30));
        setVisible(true);
        this.method = method;
    }

    public void displayNextGeneration(boolean[][] generation, int count)
    {
        setTitle("Generation: " + count + "(" + method + ")");
        panel.setGeneration(generation);
        panel.setVisible(false);
        panel.setVisible(true);
    }
}