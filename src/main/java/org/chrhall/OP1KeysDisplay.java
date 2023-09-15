package org.chrhall;

import lombok.Data;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

@Data
public class OP1KeysDisplay {
    JPanel OP1Keys;
    HashMap<Integer, Component> keyMap;
    JPanel whiteKeys;
    JPanel blackKeys;

    public OP1KeysDisplay() {
        OP1Keys = new JPanel(new MigLayout("", "", "[][]"));
        OP1Keys.setSize(400, 100);

        whiteKeys = new JPanel(new MigLayout("ins 0", "", ""));
        blackKeys = new JPanel(new MigLayout("ins 0", "", ""));

        keyMap = new HashMap<>();

        int key = 53;
        int b = 0;
        for (int i = 0; i < 24; i++) {
            GraphicsPanel g = new GraphicsPanel(12, 12);
            if (OP1SeqMain.midiToNoteName.noteNumberToName(key).length() == 3) {
                g.setForeground(Color.BLACK);
                switch (b) {
                    case 0, 3, 5, 8 -> blackKeys.add(g, "w 14!, h 14!, gapleft 9");
                    case 2, 4, 7, 9 -> blackKeys.add(g, "w 14!, h 14!, gapright 9");
                    default -> blackKeys.add(g, "w 14!, h 14!");
                }
                b++;
            } else {
                g.setForeground(Color.LIGHT_GRAY);
                whiteKeys.add(g, "w 14!, h 14!");
            }
            keyMap.put(key, g);
            key++;
        }

        OP1Keys.add(blackKeys, "cell 0 0");
        OP1Keys.add(whiteKeys, "cell 0 1");


    }


    static class GraphicsPanel extends JPanel {
        int width;
        int height;

        public GraphicsPanel(int w, int h) {
            this.width = w;
            this.height = h;
        }

        private void doDrawing(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            int top = (getHeight() - this.height) / 2;
            int bottom = (getWidth() - this.width) / 2;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.fillOval(top, bottom, width, height);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            doDrawing(g);
        }
    }
}
