package org.example;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.Data;
import lombok.Getter;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

@Data
public class Main {
    static ArrayList<Steps> stepsGrid;
    static ArrayList<JButton> menuButtons;
    static ArrayList<JLabel> infoGrid;
    static final int[] focusedButton = {0};
    static final int[] focusedMenuButton = {0};
    static int BPM;      // Tempo of sequencer
//    static Sequencer sequencer;
    static JFrame frame;
    static int velocity;
    static int note;
    static JLabel stepVelocity;
    @Getter
    static Receiver OP1Receiver;

    public static int getVelocity() {
        return velocity;
    }

    public static void setVelocity(int velocity) {
        Main.velocity = velocity;
    }

    public static int getNote() {
        return note;
    }

    public static void setNote(int note) {
        Main.note = note;
    }

    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, UnsupportedLookAndFeelException {
        velocity = 127;
        note = 60;

        FlatLightLaf.setup();
        UIManager.setLookAndFeel(new FlatDarculaLaf());
        UIManager.put("Button.arc", 0);


        MIDISequencer MIDISequencer = new MIDISequencer();
//        MidiDevicePanel midiDevicePanel = new MidiDevicePanel();

        InfoPanel infoPanel = new InfoPanel(MIDISequencer);
        StepsPanel stepsPanel = new StepsPanel(MIDISequencer);
        MenuPanel menuPanel = new MenuPanel();

        Frame frame = new Frame(menuPanel, infoPanel, stepsPanel);

        // Set up key bindings
        menuKeyBindings(menuPanel, infoPanel, MIDISequencer, stepsPanel);
        stepGridKeyBindings(stepsPanel, menuPanel);

    }

    static void stepGridKeyBindings(StepsPanel stepsPanel, MenuPanel menuPanel) {

        InputMap inputMap = stepsPanel.getStepsPanel().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "UP");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "DOWN");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "LEFT");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RIGHT");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "OPEN_MENU");

        stepsPanel.getStepsPanel().getActionMap().put("OPEN_MENU", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!menuPanel.getMenuPanel().isVisible()) {
                    stepsPanel.getStepsGrid().get(2).getToggleButton().requestFocus();      // Move focus away from menu area before opening, to prevent glitches
                    menuPanel.getMenuPanel().setVisible(true);
                    menuPanel.getMenuButtons().get(focusedMenuButton[0]).requestFocus();
                }
            }
        });

        stepsPanel.getStepsPanel().getActionMap().put("UP", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusedButton[0] > 7) {
                    focusedButton[0] -= 8;
                    stepsPanel.getStepsGrid().get(focusedButton[0]).getToggleButton().requestFocus();
                    stepsPanel.getStepVelocity().setText(Integer.toString(stepsPanel.getStepsGrid().get(focusedButton[0]).getVelocity()));
                }
            }
        });

        stepsPanel.getStepsPanel().getActionMap().put("DOWN", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusedButton[0] < 7) {
                    focusedButton[0] += 8;
                    stepsPanel.getStepsGrid().get(focusedButton[0]).getToggleButton().requestFocus();
                    stepsPanel.getStepVelocity().setText(Integer.toString(stepsPanel.getStepsGrid().get(focusedButton[0]).getVelocity()));
                }
            }
        });

        stepsPanel.getStepsPanel().getActionMap().put("LEFT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusedButton[0] > 0) {
                    focusedButton[0] -= 1;
                    stepsPanel.getStepsGrid().get(focusedButton[0]).getToggleButton().requestFocus();
                    stepsPanel.getStepVelocity().setText(Integer.toString(stepsPanel.getStepsGrid().get(focusedButton[0]).getVelocity()));
                }
            }
        });

        stepsPanel.getStepsPanel().getActionMap().put("RIGHT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusedButton[0] < 15) {
                    focusedButton[0] += 1;
                    stepsPanel.getStepsGrid().get(focusedButton[0]).getToggleButton().requestFocus();
                    stepsPanel.getStepVelocity().setText(Integer.toString(stepsPanel.getStepsGrid().get(focusedButton[0]).getVelocity()));
                }
            }
        });

    }

    static void menuKeyBindings(MenuPanel menuPanel, InfoPanel infoPanel, MIDISequencer Sequencer, StepsPanel stepsPanel) {

        InputMap inputMap = menuPanel.getMenuPanel().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "CLOSE_MENU");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "NAV_LEFT");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "NAV_RIGHT");

        InputMap startStopMap = menuPanel.getMenuButtons().get(0).getInputMap(JComponent.WHEN_FOCUSED);
        startStopMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "START_STOP");

        InputMap bpmMap = menuPanel.getMenuButtons().get(1).getInputMap(JComponent.WHEN_FOCUSED);
        bpmMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "INCREASE_BPM");
        bpmMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "DECREASE_BPM");

        InputMap velocityMap = menuPanel.getMenuButtons().get(2).getInputMap(JComponent.WHEN_FOCUSED);
        velocityMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "INCREASE_VELOCITY");
        velocityMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "DECREASE_VELOCITY");

        InputMap noteMap = menuPanel.getMenuButtons().get(3).getInputMap(JComponent.WHEN_FOCUSED);
        noteMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "NOTE_UP");
        noteMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "NOTE_DOWN");

        menuPanel.getMenuPanel().getActionMap().put("NAV_LEFT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusedMenuButton[0] > 0) {
                    menuPanel.getMenuButtons().get(focusedMenuButton[0] - 1).requestFocus();
                    focusedMenuButton[0] -= 1;
                }
            }
        });

        menuPanel.getMenuPanel().getActionMap().put("NAV_RIGHT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusedMenuButton[0] < menuPanel.getMenuButtons().size() - 1) {
                    menuPanel.getMenuButtons().get(focusedMenuButton[0] + 1).requestFocus();
                    focusedMenuButton[0] += 1;
                }
            }
        });

        menuPanel.getMenuButtons().get(0).getActionMap().put("START_STOP", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Sequencer.getSequencer().isRunning()) {
                    Sequencer.getSequencer().stop();
                    menuPanel.getMenuButtons().get(0).setText("START");
                } else {
                    Sequencer.getSequencer().start();
                    menuPanel.getMenuButtons().get(0).setText("STOP");
                }
            }
        });

        menuPanel.getMenuPanel().getActionMap().put("CLOSE_MENU", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPanel.getMenuPanel().setVisible(false);
                stepsPanel.getStepsGrid().get(focusedButton[0]).getToggleButton().requestFocus();
            }
        });

        menuPanel.getMenuButtons().get(1).getActionMap().put("INCREASE_BPM", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sequencer.getSequencer().setTempoInBPM(Sequencer.getSequencer().getTempoInBPM() + 1);
                infoPanel.getInfoGrid().get(0).setText("BPM: " + Sequencer.getSequencer().getTempoInBPM() + "\n");
            }
        });

        menuPanel.getMenuButtons().get(1).getActionMap().put("DECREASE_BPM", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sequencer.getSequencer().setTempoInBPM(Sequencer.getSequencer().getTempoInBPM() + 1);
                infoPanel.getInfoGrid().get(0).setText("BPM: " + Sequencer.getSequencer().getTempoInBPM() + "\n");
            }
        });

        menuPanel.getMenuButtons().get(2).getActionMap().put("INCREASE_VELOCITY", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (velocity < 127) {
                    setVelocity(velocity + 1);
                    infoPanel.getInfoGrid().get(1).setText("VEL: " + getVelocity() + "\n");
                }
            }
        });

        menuPanel.getMenuButtons().get(2).getActionMap().put("DECREASE_VELOCITY", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (velocity > 0) {
                    setVelocity(velocity - 1);
                    infoPanel.getInfoGrid().get(1).setText("VEL: " + getVelocity() + "\n");
                }
            }
        });

        menuPanel.getMenuButtons().get(3).getActionMap().put("NOTE_UP", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (note < 127) {
                    setNote(note + 1);
                    infoPanel.getInfoGrid().get(2).setText("NOTE: " + getNote() + "\n");
                }
            }
        });

        menuPanel.getMenuButtons().get(3).getActionMap().put("NOTE_DOWN", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (note > 0) {
                    setNote(note - 1);
                    infoPanel.getInfoGrid().get(2).setText("NOTE: " + getNote() + "\n");
                }
            }
        });

    }

}





