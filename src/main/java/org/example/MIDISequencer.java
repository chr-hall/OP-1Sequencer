package org.example;

import lombok.Data;
import lombok.Getter;

import javax.sound.midi.*;
import javax.sound.midi.MidiUnavailableException;
import java.util.Arrays;
import java.util.List;

@Data
public class MIDISequencer {
    private Sequencer sequencer;
    private Sequence sequence;
    private Track track;
    private int BPM;
    private int PPQ;
    private Receiver OP1Receiver;
    private Transmitter OP1Transmitter;
    private int pulseCount;
    private int step;
    private boolean isRunning;

    public MIDISequencer() throws MidiUnavailableException, InvalidMidiDataException {
        int BPM = 120;
        this.PPQ = 768;
        // Print available MIDI devices
//        Arrays.stream(midiDevices).forEach(e -> System.out.println(e.getName() + " " + e.getDescription()));

        // Init sequencer
        Sequencer sequencer = MidiSystem.getSequencer();
        Transmitter sequencerTransmitter = sequencer.getTransmitter();

        // Init OP1
        MidiDevice.Info[] midiDevices = MidiSystem.getMidiDeviceInfo();
        MidiDevice OP1Input = MidiSystem.getMidiDevice(midiDevices[4]);
        MidiDevice OP1Output = MidiSystem.getMidiDevice(midiDevices[5]);
        OP1Input.open();
        OP1Output.open();

        // OP1 MIDI IN
        OP1Receiver = OP1Input.getReceiver();
        // OP1 MIDI OUT
        OP1Transmitter = OP1Output.getTransmitter();

        // Handle MIDI from OP1 (clock pulse etc.)
        OP1Transmitter.setReceiver(new MidiHandler(0, 0, false));

        // Send MIDI from sequencer to OP1
//        sequencerTransmitter.setReceiver(OP1Receiver);

        sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        sequencer.open();

        // Init sequence and track, start sequencer
        Sequence sequence = new Sequence(Sequence.PPQ, this.PPQ);
        this.track = sequence.createTrack();
        sequencer.setSequence(sequence);

        this.sequencer = sequencer;
        this.BPM = BPM;
    }


    static void deviceSetup(int selectedDevice, MidiDevice.Info[] midiDevices) throws MidiUnavailableException, InvalidMidiDataException {
        System.out.println("Success");
//        MidiDevice midiDevice = MidiSystem.getMidiDevice(midiDevices[selectedDevice]);
//        Receiver receiver = midiDevice.getReceiver();
//
//        int BPM = 120;
//        int PPQ = 768;
//
//        // Init sequencer
//        Sequencer sequencer = MidiSystem.getSequencer();
//        sequencer.getTransmitter().setReceiver(receiver);
//        sequencer.setTempoInBPM(BPM);
//        sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
//        sequencer.open();
//
//
//        // Init sequence and track, start sequencer
//        Sequence sequence = new Sequence(Sequence.PPQ, PPQ);
//        Track track = sequence.createTrack();
//        sequencer.setSequence(sequence);
//        sequencer.start();
//
//        midiDevice.open();


    }

    @Data
    public class MidiHandler implements Receiver {
        int pulseCount;
        int step;
        boolean isRunning;

        public MidiHandler(int pulseCount, int step, boolean isRunning) {
        }

        @Override
        public void send(MidiMessage message, long timeStamp) {
            // 248 - clock, 250 - start, 252 - stop
            int status = message.getStatus();

            if (status == 250) {
                isRunning = true;
            } else if (status == 252) {
                pulseCount = 0;
                step = 0;
                isRunning = false;
            }

            if (status == 248 && isRunning) {
                if (pulseCount == 0) {
                    if (step > 15) {
                        step = 0;
                    }
                    OP1Receiver.send(getTrack().get(step).getMessage(), -1);
                    pulseCount++;
                    step++;
                } else if (pulseCount == 5) {
                    pulseCount = 0;
                } else {
                    pulseCount++;
                }
            }

        }

        @Override
        public void close() {

        }
    }
}

