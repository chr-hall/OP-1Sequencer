package org.example;

import lombok.Getter;
import lombok.Setter;
import net.miginfocom.swing.MigLayout;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;
import java.util.ArrayList;

@Getter
@Setter
public class StepsPanel {
    private JPanel stepsPanel;
    private ArrayList<Steps> stepsGrid;
    private JPanel stepsInfoPanel;
    private JLabel stepVelocity;

    public StepsPanel(MIDISequencer Sequencer) throws InvalidMidiDataException {
        // Number of steps in sequence, might be useful for multiple pages in future
        int sequencerSteps = 16;

        // Panel for sequencer steps
        JPanel stepsPanel = new JPanel(new MigLayout("wrap 8, alignx right"));
        stepsPanel.setSize(500, 150);

        // Create steps and add to grid and panel
        ArrayList<Steps> stepsGrid = new ArrayList<>();
        for (int i = 0; i < sequencerSteps; i++) {
            stepsGrid.add(new Steps((long) i * (Sequencer.getPPQ() / sequencerSteps), Sequencer.getTrack(), Integer.toString(i + 1), 127));
            stepsPanel.add(stepsGrid.get(i).getToggleButton(), "width 60, height 60, align center, ");
        }

        // Request focus on start up
        stepsGrid.get(0).getToggleButton().requestFocus();

        // Show info of selected step
        JPanel stepInfoPanel = new JPanel(new MigLayout("aligny bottom, alignx right"));
        stepInfoPanel.setSize(200, 100);

        stepVelocity = new JLabel();
        stepInfoPanel.add(stepVelocity);

        this.stepsPanel = stepsPanel;
        this.stepsGrid = stepsGrid;
        this.stepsInfoPanel = stepInfoPanel;
    }

}
