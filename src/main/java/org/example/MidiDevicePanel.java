package org.example;

import lombok.Data;
import net.miginfocom.swing.MigLayout;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

@Data
public class MidiDevicePanel {
    private JPanel midiDevicePanel;


    public MidiDevicePanel() {
        JPanel midiDevicePanel = new JPanel(new MigLayout("align center"));
        midiDevicePanel.setSize(500, 150);

        DefaultListModel<Object> listModel = new DefaultListModel<>();
        MidiDevice.Info[] midiDevices = MidiSystem.getMidiDeviceInfo();
        Arrays.stream(midiDevices).sequential().forEach(listModel::addElement);

        JList<Object> midiDeviceList = new JList<>(listModel);

        midiDeviceList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        midiDeviceList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        midiDeviceList.setVisibleRowCount(-1);

        midiDevicePanel.add(midiDeviceList);

        keyBindings(midiDeviceList, midiDevices);



        this.midiDevicePanel = midiDevicePanel;
    }

    public void keyBindings(JList midiDeviceList, MidiDevice.Info[] midiDevices) {
        InputMap inputMap = midiDeviceList.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "SELECT");

        midiDeviceList.getActionMap().put("SELECT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MidiDevice midiDevice;
                try {
                    MIDISequencer.deviceSetup(midiDeviceList.getSelectedIndex(), midiDevices);
                } catch (MidiUnavailableException ex) {
                    throw new RuntimeException(ex);
                } catch (InvalidMidiDataException ex) {
                    throw new RuntimeException(ex);
                }
                midiDevicePanel.setVisible(false);

            }
        });

    }
}
