package com.zemrow.scanner8mm.opencv.ui.numberField;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public abstract class NumberField<T extends Number & Comparable> extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, DocumentListener, ChangeListener {

    private final JTextField field;
    private final JLabel label;
    private final Border normalBorder;
    private final Border errorBorder;

    private AbstractNumberModel<T> model;
    private int y;

    private ChangeListener changeListener;

    public NumberField(AbstractNumberModel<T> model) {
        super(new BorderLayout());
        field = new JTextField(10);
        field.addMouseWheelListener(this);
        field.getDocument().addDocumentListener(this);
        add(field, BorderLayout.CENTER);

        label = new JLabel("\u2195");
        label.addMouseListener(this);
        label.addMouseMotionListener(this);
        label.addMouseWheelListener(this);
        label.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
        add(label, BorderLayout.EAST);

        normalBorder = field.getBorder();
        errorBorder = new LineBorder(Color.RED, 1);

        this.model = model;
        model.setChangeListener(this);
    }

    public T getValue() {
        return model.getValue();
    }

    public void setValue(T value) {
        field.setText(model.format(value));
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        y = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int currentY = e.getY();
        setValue(model.takeStep(this.y - currentY));
        y = currentY;
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        setValue(model.takeStep(-e.getWheelRotation()));
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
        if (model.setValue(field.getText())) {
            field.setBorder(normalBorder);
        } else {
            field.setBorder(errorBorder);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (changeListener != null) {
            changeListener.stateChanged(new ChangeEvent(this));
        }
    }
}
