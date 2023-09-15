package org.chrhall;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class OP1SeqMain {
    static final int[] focusedButton = {0, 0};
    @Getter
    @Setter
    static int velocity;
    @Getter
    @Setter
    static int note;
    @Getter
    @Setter
    static List<StepsPanel> stepsPanelList;
    @Getter
    @Setter
    static OP1KeysDisplay op1KeysDisplay;
    @Getter @Setter
    static InfoPanel infoPanel;
    static final int[] activeStepsPanel = {0};
    static Frame frame;
    @Getter
    static boolean velocityHeld;
    static boolean noteHeld;
    static boolean pageHeld;


    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, UnsupportedLookAndFeelException {
        velocity = 127;
        note = 60;

        FlatLightLaf.setup();
        UIManager.setLookAndFeel(new FlatDarculaLaf());
        UIManager.put("Button.arc", 0);

        stepsPanelList = new ArrayList<>();

        stepsPanelList.add(new StepsPanel());
        stepsPanelList.add(new StepsPanel());
        stepsPanelList.add(new StepsPanel());

        stepsPanelList.forEach(e -> e.getStepsPanel().setVisible(false));

        stepsPanelList.add(0, new StepsPanel());

        new MIDISequencer(stepsPanelList);

        infoPanel = new InfoPanel();
        op1KeysDisplay = new OP1KeysDisplay();

        frame = new Frame(infoPanel, stepsPanelList, op1KeysDisplay);
        stepsPanelList.forEach(e -> stepGridKeyBindings(e, infoPanel));

        focusedButton(stepsPanelList.get(activeStepsPanel[0]));

    }


    static void stepGridKeyBindings(StepsPanel stepsPanel, InfoPanel infoPanel) {

        InputMap inputMap = stepsPanel.getStepsPanel().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "UP");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "DOWN");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "LEFT");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RIGHT");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "HOLD_VELOCITY");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "RELEASE_VELOCITY");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "HOLD_NOTE");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "RELEASE_NOTE");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "HOLD_PAGE");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "RELEASE_PAGE");

        stepsPanel.getStepsPanel().getActionMap().put("HOLD_PAGE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageHeld = true;
            }
        });
        stepsPanel.getStepsPanel().getActionMap().put("RELEASE_PAGE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pageHeld = false;
            }
        });
        stepsPanel.getStepsPanel().getActionMap().put("HOLD_VELOCITY", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                velocityHeld = true;
            }
        });

        stepsPanel.getStepsPanel().getActionMap().put("RELEASE_VELOCITY", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                velocityHeld = false;
            }
        });

        stepsPanel.getStepsPanel().getActionMap().put("HOLD_NOTE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noteHeld = true;
            }
        });

        stepsPanel.getStepsPanel().getActionMap().put("RELEASE_NOTE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noteHeld = false;
            }
        });

        stepsPanel.getStepsPanel().getActionMap().put("UP", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (velocityHeld) {
                    int velocity = stepsPanel.getStepsGrid().get(focusedButton[0]).getVelocity();
                    if (velocity < 127 && velocity > 0) {
                        stepsPanel.getStepsGrid().get(focusedButton[0]).setVelocity(velocity + 1);
                        focusedButton(stepsPanel);
                    } else if (getVelocity() > 0 && getVelocity() < 127) {
                        setVelocity(getVelocity() + 1);
                        infoPanel.getInfoGrid().get(0).setText("" + getVelocity());
                    }
                } else if (noteHeld) {
                    int note = stepsPanel.getStepsGrid().get(focusedButton[0]).getNote();
                    if (note < 127 && note > 0) {
                        stepsPanel.getStepsGrid().get(focusedButton[0]).setNote(note + 1);
                        focusedButton(stepsPanel);
                    } else if (getNote() > 0 && getNote() < 127) {
                        setNote(getNote() + 1);
                        infoPanel.getInfoGrid().get(1).setText("" + midiToNoteName.noteNumberToName(getNote()));
                        focusedButton(stepsPanel);
                    }
                } else if (pageHeld) {
                    pageUp(infoPanel);
                    focusedButton(stepsPanelList.get(activeStepsPanel[0]));
                }else {
                    if (focusedButton[0] > 7) {
                        focusedButton[1] = focusedButton[0];
                        focusedButton[0] -= 8;
                        focusedButton(stepsPanel);
                    }
                }
            }
        });

        stepsPanel.getStepsPanel().getActionMap().put("DOWN", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (velocityHeld) {
                    int velocity = stepsPanel.getStepsGrid().get(focusedButton[0]).getVelocity();
                    if (velocity > 0) {
                        stepsPanel.getStepsGrid().get(focusedButton[0]).setVelocity(velocity - 1);
                        focusedButton(stepsPanel);
                    } else if (getVelocity() > 0) {
                        setVelocity(getVelocity() - 1);
                        infoPanel.getInfoGrid().get(0).setText("" + getVelocity());
                    }
                } else if (noteHeld) {
                    int note = stepsPanel.getStepsGrid().get(focusedButton[0]).getNote();
                    if (note > 0) {
                        stepsPanel.getStepsGrid().get(focusedButton[0]).setNote(note - 1);
                        focusedButton(stepsPanel);
                    } else if (getNote() > 0) {
                        setNote(getNote() - 1);
                        infoPanel.getInfoGrid().get(1).setText("" + midiToNoteName.noteNumberToName(getNote()));
                        focusedButton(stepsPanel);
                    }
                } else if (pageHeld) {
                    pageDown(infoPanel);
                    focusedButton(stepsPanelList.get(activeStepsPanel[0]));
                }else {
                    if (focusedButton[0] <= 7) {
                        focusedButton[1] = focusedButton[0];
                        focusedButton[0] += 8;
                        focusedButton(stepsPanel);
                    }
                }
            }
        });

        stepsPanel.getStepsPanel().getActionMap().put("LEFT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusedButton[0] > 0) {
                    focusedButton[1] = focusedButton[0];
                    focusedButton[0] -= 1;
                    focusedButton(stepsPanel);
                }
            }
        });

        stepsPanel.getStepsPanel().getActionMap().put("RIGHT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusedButton[0] < 15) {
                    focusedButton[1] = focusedButton[0];
                    focusedButton[0] += 1;
                    focusedButton(stepsPanel);
                }
            }
        });
    }

    static void pageUp(InfoPanel infoPanel) {
        if (activeStepsPanel[0] < stepsPanelList.size() - 1) {
            stepsPanelList.get(activeStepsPanel[0]).getStepsPanel().setVisible(false);
            activeStepsPanel[0] += 1;
            stepsPanelList.get(activeStepsPanel[0]).getStepsPanel().setVisible(true);
            infoPanel.getInfoGrid().get(2).setText((activeStepsPanel[0] + 1) + "/4");
        }
    }
    static void pageDown(InfoPanel infoPanel) {
        if (activeStepsPanel[0] > 0) {
            stepsPanelList.get(activeStepsPanel[0]).getStepsPanel().setVisible(false);
            activeStepsPanel[0] -= 1;
            stepsPanelList.get(activeStepsPanel[0]).getStepsPanel().setVisible(true);
            infoPanel.getInfoGrid().get(2).setText((activeStepsPanel[0] + 1) + "/4");
        }
    }

    static void focusedButton(StepsPanel stepsPanel) {
        // Focus button and display velocity and note in StepInfoPanel
        int velocity = stepsPanel.getStepsGrid().get(focusedButton[0]).getVelocity();
        int note = stepsPanel.getStepsGrid().get(focusedButton[0]).getNote();
        String noteName = midiToNoteName.noteNumberToName(note);

        paintKeys(note, new Color(51, 153, 102));

        if (velocity == 0) {
            infoPanel.getInfoGrid().get(4).setText("");
        } else {
            infoPanel.getInfoGrid().get(4).setText("" + velocity);
        }

        infoPanel.getInfoGrid().get(3).setText(noteName);

        stepsPanel.getStepsGrid().get(focusedButton[0]).getToggleButton().requestFocus();

    }

    static void paintKeys(int note, Color color) {
        for (Component c : op1KeysDisplay.getBlackKeys().getComponents()) {
            c.setForeground(Color.BLACK);
        }

        for (Component c : op1KeysDisplay.getWhiteKeys().getComponents()) {
            c.setForeground(Color.LIGHT_GRAY);
        }

        Component key = op1KeysDisplay.keyMap.get(note);
        Component selectedNote = op1KeysDisplay.keyMap.get(getNote());
        if (selectedNote != null) {
            selectedNote.setForeground(new Color(153, 102, 51));
        }
        if (key != null) {
            key.setForeground(color);
        }
    }

    public static class midiToNoteName {
        static List<String> numList = Arrays.asList("C",
                "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");

        public static String noteNumberToName(int number) {
            if (number == 0) {
                return "";
            }
            int octave = (number / 12) - 1;
            int noteIdx = number % 12;
            return (numList.get(noteIdx)).toUpperCase() + octave;
        }
    }


}








