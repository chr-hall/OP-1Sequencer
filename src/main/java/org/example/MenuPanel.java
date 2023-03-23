package org.example;

import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
public class MenuPanel {
    @Getter
    private JPanel menuPanel;
    @Getter
    private ArrayList<JButton> menuButtons;

    public MenuPanel() {
        // Menu panel and buttons
        JPanel menuPanel = new JPanel(new MigLayout("aligny top, alignx left, gap 10px"));
        menuPanel.setSize(500, 100);
        menuPanel.setFocusable(true);

        // List of buttons, for key bindings
        ArrayList<JButton> menuButtons = new ArrayList<>();

        JButton startStopButton = new JButton("STOP");
        menuPanel.add(startStopButton, "width 30, height 30, gap 10px, align center");

        JButton bpmButton = new JButton("BPM");
        menuPanel.add(bpmButton, "width 30, height 30, gap 10px, align center");

        JButton velocityButton = new JButton("VEL");
        menuPanel.add(velocityButton, "width 30, height 30, gap 10px, align center");

        JButton noteButton = new JButton("NOTE");
        menuPanel.add(noteButton, "width 30, height 30, gap 10px, align center");

        menuButtons.add(startStopButton);
        menuButtons.add(bpmButton);
        menuButtons.add(velocityButton);
        menuButtons.add(noteButton);

        this.menuPanel = menuPanel;
        this.menuButtons = menuButtons;

        JLabel menuLabel = new JLabel();
        menuLabel.setSize(300, 100);

        menuPanel.add(menuLabel);
        menuPanel.setVisible(false);
    }

}
