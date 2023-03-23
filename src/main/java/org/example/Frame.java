package org.example;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
@Data
public class Frame {
    @Getter
    private static JFrame frame;

    public Frame(MenuPanel menuPanel, InfoPanel infoPanel, StepsPanel stepsPanel) throws UnsupportedLookAndFeelException {
        // Set up JFrame and add panels
        JFrame frame = new JFrame();
        frame.setSize(800, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        frame.add(midiDevicePanel.getMidiDevicePanel());
        frame.add(menuPanel.getMenuPanel());
        frame.add(infoPanel.getInfoPanel());
        frame.add(stepsPanel.getStepsInfoPanel());
        frame.add(stepsPanel.getStepsPanel());

        frame.setVisible(true);
        frame.validate();

        this.frame = frame;
    }

    public void addToFrame(JPanel jPanel) {
        frame.add(jPanel);
    }





}
