package org.example;

import lombok.*;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
@Getter @Setter
public class Steps {
    private JToggleButton toggleButton;     //GUI button
    private ShortMessage shortMessage;      //Message containing note information
    private long timeStamp;     //Time stamp for tick when note is played
    private MidiEvent midiEvent;    //Combination of shortMessage and timeStamp
    private boolean isNoteOn;      //To keep track if step is active or not
    private int velocity;
    private int note;

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public Steps(long timeStamp, Track track, String text, int velocity) throws InvalidMidiDataException {
        // Toggle button for turning step on or off
        this.toggleButton = new JToggleButton(text);

        // Set up MIDI event for step
        this.velocity = velocity;
        this.shortMessage = new ShortMessage(ShortMessage.NOTE_OFF, 0, 60, 127);
        this.timeStamp = timeStamp;
        this.midiEvent = new MidiEvent(this.shortMessage, this.timeStamp);
        this.isNoteOn = false;

        // Add event to track
        track.add(this.midiEvent);

        // Remove default key bindings to prevent focus issues
        InputMap inputMap = this.toggleButton.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "none");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "none");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "none");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "none");
        // Toggle note on and off
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER");

        this.toggleButton.getActionMap().put("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectAction(track);
            }
        });

    }

    void selectAction(Track track) {
        // Turn on or off step
        if (!this.isNoteOn) {
            try {
                setVelocity(Main.getVelocity());
                setNote(Main.getNote());
                System.out.println(getVelocity());
                this.shortMessage.setMessage(ShortMessage.NOTE_ON, 0, getNote(), getVelocity());
                this.toggleButton.doClick();
            } catch (InvalidMidiDataException ex) {
                throw new RuntimeException(ex);
            }
            track.remove(this.midiEvent = new MidiEvent(this.shortMessage, this.timeStamp));
            this.isNoteOn = true;
        } else {
            try {
                setVelocity(Main.getVelocity());
                setNote(Main.getNote());
                this.shortMessage.setMessage(ShortMessage.NOTE_OFF, 0, getNote(), getVelocity());
                this.toggleButton.doClick();
            } catch (InvalidMidiDataException ex) {
                throw new RuntimeException(ex);
            }
            track.remove(this.midiEvent = new MidiEvent(this.shortMessage, this.timeStamp));
            this.isNoteOn = false;
        }
    }

    public JToggleButton getToggleButton() {
        return toggleButton;
    }

}
