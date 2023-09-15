package org.chrhall;

import lombok.Data;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@Data
public class InfoPanel {
    private JPanel infoPanel;
    private final ArrayList<JLabel> infoGrid;
    private final int velocity;

    public InfoPanel() {
        velocity = 127;

        // Info panel for displaying selected velocity and current BPM
        infoPanel = new JPanel(new MigLayout("", "[][25:25:25][align right, grow, fill]", ""));
        infoPanel.setSize(150, 100);

        JLabel velocityLabel = new JLabel("127");
        JLabel velocityLabel2 = new JLabel("127");
        JLabel noteLabel = new JLabel("C4");
        JLabel noteLabel2 = new JLabel("C4");
        JLabel pagesLabel = new JLabel("1/4");

        OP1KeysDisplay.GraphicsPanel g = drawCircle(12, 12, new Color(153, 102, 51));
        infoPanel.add(g, "cell 0 0, w 14!, h 14!, align center");

        OP1KeysDisplay.GraphicsPanel g2 = drawCircle(12, 12, new Color(51, 153, 102));
        infoPanel.add(g2, "cell 0 1, w 14!, h 14!, align center");

        infoPanel.add(velocityLabel, "cell 2 0");
        infoPanel.add(velocityLabel2, "cell 2 1");
        infoPanel.add(noteLabel, "cell 1 0, sg 1");
        infoPanel.add(noteLabel2, "cell 1 1, sg 1");
        infoPanel.add(pagesLabel, "cell 2 0, gap push");
        infoPanel.setVisible(true);

        // List for handling key bindings
        infoGrid = new ArrayList<>();
        infoGrid.add(velocityLabel);
        infoGrid.add(noteLabel);
        infoGrid.add(pagesLabel);
        infoGrid.add(noteLabel2);
        infoGrid.add(velocityLabel2);

    }

    static OP1KeysDisplay.GraphicsPanel drawCircle(int w, int h, Color color) {
        OP1KeysDisplay.GraphicsPanel g = new OP1KeysDisplay.GraphicsPanel(w, h);
        g.setForeground(color);
        return g;

    }
}
