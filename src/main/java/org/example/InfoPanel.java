package org.example;

import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
@Getter
public class InfoPanel {
    private JPanel infoPanel;
    private ArrayList<JLabel> infoGrid;
    private int velocity;

    public InfoPanel(MIDISequencer Sequencer) {
        this.velocity = 127;

        // Info panel for displaying selected velocity and current BPM
        JPanel infoPanel = new JPanel(new MigLayout("wrap 1, alignx left, aligny bottom"));
        infoPanel.setSize(100, 100);

        JLabel bpmLabel = new JLabel(String.valueOf("BPM: " + Sequencer.getSequencer().getTempoInBPM() + "\n"));
        JLabel velocityLabel = new JLabel("VEL: " + this.velocity);
        JLabel noteLabel = new JLabel("NOTE: " );


        infoPanel.add(bpmLabel);
        infoPanel.add(velocityLabel);
        infoPanel.add(noteLabel);
        infoPanel.setVisible(true);

        // List for handling key bindings
        ArrayList <JLabel> infoGrid = new ArrayList<>();
        infoGrid.add(bpmLabel);
        infoGrid.add(velocityLabel);
        infoGrid.add(noteLabel);

        this.infoPanel = infoPanel;
        this.infoGrid = infoGrid;
    }
}
