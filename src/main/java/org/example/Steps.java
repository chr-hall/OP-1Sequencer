package org.example;

import lombok.*;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
@Data
public class Steps {
    private JToggleButton toggleButton;     //GUI button
    private ShortMessage shortMessage;      //Message containing note information
    private ShortMessage shortMessageNoteOff;
    private boolean isNoteOn;      //To keep track if step is active or not
    private int velocity;
    private int note;

    public Steps(String text) throws InvalidMidiDataException {
        // Toggle button for turning step on or off
        this.toggleButton = new JToggleButton(text);

        // Set up MIDI event for step
        this.shortMessage = new ShortMessage(ShortMessage.NOTE_OFF, 0, this.note, OP1SeqMain.getVelocity());
        this.shortMessageNoteOff = new ShortMessage(ShortMessage.NOTE_OFF, 0, 60, OP1SeqMain.getVelocity());
        this.isNoteOn = false;

        stepKeyBind();

    }

    void stepKeyBind() {
        // Remove default key bindings to prevent focus issues
        InputMap inputMap = this.toggleButton.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "none");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "none");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "none");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "none");
        // Toggle step on and off
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");

        this.toggleButton.getActionMap().put("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectAction();
                OP1SeqMain.focusedButton(OP1SeqMain.getStepsPanelList().get(OP1SeqMain.activeStepsPanel[0]));
            }
        });
    }

    void selectAction() {
        // Turn on or off step
        if (!this.isNoteOn) {
            try {
                this.toggleButton.doClick();
                this.isNoteOn = true;
                this.velocity = OP1SeqMain.getVelocity();
                this.note = OP1SeqMain.getNote();
                this.shortMessage.setMessage(ShortMessage.NOTE_ON, 0, this.note, this.velocity);
                this.shortMessageNoteOff.setMessage(ShortMessage.NOTE_OFF, 0, this.note, this.velocity);
            } catch (InvalidMidiDataException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            try {
                this.toggleButton.doClick();
                this.isNoteOn = false;
                this.velocity = 0;
                this.note = 0;
                this.shortMessage.setMessage(ShortMessage.NOTE_OFF, 0, this.note, this.velocity);
            } catch (InvalidMidiDataException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
}
