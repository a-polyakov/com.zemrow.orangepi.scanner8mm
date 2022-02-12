package com.zemrow.scanner8mm.opencv.transformation.open;

import com.zemrow.scanner8mm.opencv.ui.AbstractTransformPanel;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class TransformOpenPanel extends AbstractTransformPanel<TransformOpen> implements DocumentListener, ActionListener {

    private final JTextField fileNameTextField;
    private final JButton choiceButton;
    private final JFileChooser fileChooser;

    public TransformOpenPanel() {
        add(new JLabel("File"));
        fileNameTextField = new JTextField(25);
        fileNameTextField.getDocument().addDocumentListener(this);
        add(fileNameTextField);
        choiceButton = new JButton("\uD83D\uDDC0");
        choiceButton.addActionListener(this);
        add(choiceButton);
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
    }

    @Override
    protected void setFieldsFromTransform(TransformOpen transform) {
        log.info("setFieldsFromTransform file:" + transform.getFile());
        fileNameTextField.setText(transform.getFile());
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        if (listenerEnable) {
            log.info("insertUpdate file:" + fileNameTextField.getText());
            changedUpdate(e);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        if (listenerEnable) {
            log.info("removeUpdate file:" + fileNameTextField.getText());
            changedUpdate(e);
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        if (listenerEnable) {
            log.info("changedUpdate file:" + fileNameTextField.getText());
            transform.setFile(fileNameTextField.getText());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fileNameTextField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }
}
