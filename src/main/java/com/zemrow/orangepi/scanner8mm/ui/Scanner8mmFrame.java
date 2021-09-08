package com.zemrow.orangepi.scanner8mm.ui;

import com.zemrow.orangepi.scanner8mm.IpCam;
import com.zemrow.orangepi.scanner8mm.StepDto;
import com.zemrow.orangepi.scanner8mm.motor.IStepperMotor;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2021.09.04
 */
public class Scanner8mmFrame extends JFrame implements ChangeListener,
        DocumentListener, ActionListener, WindowListener, StepViewer {

    private final JSlider positionSlider;
    private final PreviewPanel previewPanel;
    // TODO ipCam URL
    private final JTextField outDirTextField;
    private final JButton outDirButton;
    private final JTextField stepNumberTextField;
    private final JButton stopButton;
    private final JButton nextFrameButton;
    private final JButton startButton;

    private final IStepperMotor stepperMotor;
    private final IpCam ipCam;

    private final Scanner8mmWorker worker;

    private int stepNumber;

    public Scanner8mmFrame(IStepperMotor stepperMotor, IpCam ipCam) throws HeadlessException, IOException {
        super("Scanner8mm");
        positionSlider = new JSlider(JSlider.VERTICAL, -160, 160, 0);
        add(positionSlider, BorderLayout.WEST);
        positionSlider.setToolTipText("Fix position");
        positionSlider.addChangeListener(this);

        previewPanel = new PreviewPanel(ipCam);
        add(previewPanel, BorderLayout.CENTER);

        final JPanel footerPanel = new JPanel(new FlowLayout());
        add(footerPanel, BorderLayout.SOUTH);

        outDirTextField = new JTextField(IpCam.DEFAULT_OUT_DIR, 40);
        footerPanel.add(outDirTextField);
        outDirTextField.getDocument().addDocumentListener(this);
        outDirButton = new JButton("Path");
        footerPanel.add(outDirButton);
        outDirButton.addActionListener(this);
        stepNumberTextField = new JTextField(Integer.toString(stepNumber), 10);
        footerPanel.add(stepNumberTextField);
        stepNumberTextField.getDocument().addDocumentListener(this);
        stopButton = new JButton("\u23F8");
        footerPanel.add(stopButton);
        stopButton.addActionListener(this);
        nextFrameButton = new JButton("\u23EF");
        footerPanel.add(nextFrameButton);
        nextFrameButton.addActionListener(this);
        startButton = new JButton("\u23F5");
        footerPanel.add(startButton);
        startButton.addActionListener(this);

        this.stepperMotor = stepperMotor;
        this.ipCam = ipCam;

        worker = new Scanner8mmWorker(stepperMotor, ipCam, this);

        addWindowListener(this);
        setSize(1024, 768);
        setVisible(true);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (positionSlider == source) {
            final int value = positionSlider.getValue();
            if (value != 0) {
                if (!positionSlider.getValueIsAdjusting()) {
                    stepperMotor.step(value);
                    previewPanel.updatePreview();
                    positionSlider.setValue(0);
                }
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    @Override
    public void changedUpdate(DocumentEvent event) {
        final Document source = event.getDocument();
        if (outDirTextField.getDocument() == source) {
            ipCam.setOutputDirectory(outDirTextField.getText());
        } else if (stepNumberTextField.getDocument() == source) {
            try {
                stepNumber = Integer.parseInt(stepNumberTextField.getText());
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (outDirButton == source) {
            final JFileChooser fc = new JFileChooser(outDirTextField.getText());
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            final int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                outDirTextField.setText(file.getAbsolutePath());
            }
        } else if (stopButton == source) {
            worker.pause();
            nextFrameButton.setEnabled(true);
            positionSlider.setEnabled(true);
            stepNumberTextField.setEnabled(true);
        } else if (nextFrameButton == source) {
            worker.next();
            if (worker.getState() == SwingWorker.StateValue.PENDING) {
                worker.execute();
            }
        } else if (startButton == source) {
            positionSlider.setEnabled(false);
            stepNumberTextField.setEnabled(false);
            nextFrameButton.setEnabled(false);
            worker.resume();
            if (worker.getState() == SwingWorker.StateValue.PENDING) {
                worker.execute();
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        stepperMotor.disable();
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public int getStepNumber() {
        return stepNumber;
    }

    public void setStep(StepDto step) {
        this.stepNumber = step.getStepNumber();
        previewPanel.updatePreview(step);
        stepNumberTextField.getDocument().removeDocumentListener(this);
        stepNumberTextField.setText(Integer.toString(stepNumber));
        stepNumberTextField.getDocument().addDocumentListener(this);
    }
}
