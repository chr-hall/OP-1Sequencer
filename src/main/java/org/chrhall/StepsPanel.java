package org.chrhall;

import lombok.Data;
import net.miginfocom.swing.MigLayout;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;
import java.util.ArrayList;

@Data
public class StepsPanel {
    private JPanel stepsPanel;
    private ArrayList<Steps> stepsGrid;
    private JLabel stepVelocity;
    private JLabel stepVelocityText;
    private JLabel stepNote;
    private JLabel stepNoteText;

    public StepsPanel() throws InvalidMidiDataException {
        // Number of steps in sequence,
        int sequencerSteps = 16;

        // Panel for sequencer steps
        stepsPanel = new JPanel(new MigLayout("wrap 8"));
        stepsPanel.setSize(500, 150);

        // Create steps and add to grid and panel
        ArrayList<Steps> stepsGrid = new ArrayList<>();
        for (int i = 0; i < sequencerSteps; i++) {
            stepsGrid.add(new Steps(Integer.toString(i + 1)));
            stepsPanel.add(stepsGrid.get(i).getToggleButton(), "width 60, height 60");
        }

        this.stepsGrid = stepsGrid;

    }

}
