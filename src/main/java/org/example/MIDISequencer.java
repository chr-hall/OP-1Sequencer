package org.example;

import lombok.Data;

import javax.sound.midi.*;
import javax.sound.midi.MidiUnavailableException;
import java.util.List;

@Data
public class MIDISequencer {
    private Receiver OP1Receiver;
    private Transmitter OP1Transmitter;
    private List<StepsPanel> stepsPanelList;

    public MIDISequencer(List<StepsPanel> stepsPanelList) throws MidiUnavailableException {
        this.stepsPanelList = stepsPanelList;

        // Find OP1 in connected MIDI devices
        MidiDevice.Info[] midiDevices = MidiSystem.getMidiDeviceInfo();
//        MidiDevice OP1Input = MidiSystem.getMidiDevice(Arrays.stream(midiDevices)
//                .filter(e -> Objects.equals(e.getName(), "OP-1") &&
//                        Objects.equals(e.getDescription(), "External MIDI Port"))
//                .findFirst().get());
//
//        MidiDevice OP1Output = MidiSystem.getMidiDevice(Arrays.stream(midiDevices)
//                .filter(e -> Objects.equals(e.getName(), "OP-1")
//                        && Objects.equals(e.getDescription(), "No details available"))
//                .findFirst().get());

        // Grab default devices (for debugging)
        MidiDevice OP1Input = MidiSystem.getMidiDevice(midiDevices[0]);
        MidiDevice OP1Output = MidiSystem.getMidiDevice(midiDevices[1]);

        OP1Input.open();
        OP1Output.open();

        // OP1 MIDI IN and OUT
        OP1Receiver = OP1Input.getReceiver();
        OP1Transmitter = OP1Output.getTransmitter();

        // Handle MIDI from OP1 (clock pulse etc.)
        OP1Transmitter.setReceiver(new MidiHandler());

    }

    @Data
    public class MidiHandler implements Receiver {
        int pulseCount = 0;
        int step = 0;
        boolean isRunning = false;

        @Override
        public void send(MidiMessage message, long timeStamp) {
            int status = message.getStatus();

            // 250 = START
            if (status == 250) {
                isRunning = true;
                // 252 = STOP
            } else if (status == 252) {
                stepsPanelList.get(step).getStepNote().setText("" + step);
                pulseCount = 0;
                step = 0;
                isRunning = false;
            }

            // 248 = CLOCK SIGNAL
            if (status == 248 && isRunning) {
                if (pulseCount == 0) {
                    if (step > 15) {
                        step = 0;
                    }
                    // NOTE ON and NOTE OFF
                    stepsPanelList.forEach(e -> {
                        OP1Receiver.send(e.getStepsGrid().get(step).getShortMessage(), -1);
                        OP1Receiver.send(e.getStepsGrid().get(step).getShortMessageNoteOff(), -1);
                    });
                    // Underline step number (and remove line from previous step)
                    stepsPanelList.get(step).getStepNote().setText("<HTML><U>" + (step + 1) + "</U></HTML>");
                    if (step == 0) {
                        stepsPanelList.get(15).getStepNote().setText("" + (step + 1));
                    } else {
                        stepsPanelList.get(step - 1).getStepNote().setText("" + (step));
                    }

                    pulseCount++;
                    step++;
                }
            } else if (pulseCount == 5) {
                pulseCount = 0;
            } else {
                pulseCount++;
            }
        }
    @Override
    public void close() {

    }
}
}

