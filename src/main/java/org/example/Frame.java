package org.example;

import lombok.Data;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

@Data
public class Frame {
    private static JFrame frame;

    public Frame(InfoPanel infoPanel, List<StepsPanel> stepsPanelList, OP1KeysDisplay op1KeysDisplay) {
        // Set up JFrame and add panels
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        // Add menu and info panel
        JPanel layoutPanel = new JPanel(new MigLayout("", "", ""));
        layoutPanel.add(infoPanel.getInfoPanel(), "cell 0 1, grow");
        layoutPanel.add(op1KeysDisplay.getOP1Keys(), "cell 0 3, align y bottom, gapbottom 1");

        // Add steps panels
        stepsPanelList.forEach(e -> layoutPanel.add(e.getStepsPanel(), "cell 2 1 4 3, hidemode 3"));

        frame.add(layoutPanel);
        frame.pack();

    }






}
