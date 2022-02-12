package com.zemrow.scanner8mm.opencv.transformation.resize;

import com.zemrow.scanner8mm.opencv.ui.AbstractTransformPanel;
import com.zemrow.scanner8mm.opencv.ui.numberField.DoubleField;
import com.zemrow.scanner8mm.opencv.ui.numberField.DoubleModel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class TransformResizePanel extends AbstractTransformPanel<TransformResize> implements ChangeListener, ActionListener {
    private final DoubleField xNumField;
    private final JToggleButton equalsButton;
    private final DoubleField yNumField;

    public TransformResizePanel() {
        JPanel row = new JPanel();
        add(row);
        row.add(new JLabel("X"));
        xNumField = new DoubleField(new DoubleModel(1, 0.01, 10, 0.01));
        xNumField.setChangeListener(this);
        row.add(xNumField);

        equalsButton = new JToggleButton("=");
        equalsButton.addActionListener(this);
        add(equalsButton);

        row = new JPanel();
        add(row);
        row.add(new JLabel("Y"));
        yNumField = new DoubleField(new DoubleModel(1, 0.01, 10, 0.01));
        yNumField.setChangeListener(this);
        row.add(yNumField);
    }

    @Override
    protected void setFieldsFromTransform(TransformResize transform) {
        log.info("setFieldsFromTransform x:" + transform.getFx() + ", y:" + transform.getFy());
        xNumField.setValue(transform.getFx());
        yNumField.setValue(transform.getFy());
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (listenerEnable) {
            log.info("stateChanged");
            Object source = e.getSource();
            if (xNumField == source) {
                transform.setFx(xNumField.getValue());
                if (equalsButton.isSelected()) {
                    yNumField.setValue(xNumField.getValue());
                }
            } else if (yNumField == source) {
                transform.setFy(yNumField.getValue());
                if (equalsButton.isSelected()) {
                    xNumField.setValue(yNumField.getValue());
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (equalsButton.isSelected()) {
            yNumField.setValue(xNumField.getValue());
        }
    }
}
