package com.zemrow.scanner8mm.opencv.transformation.canny;

import com.zemrow.scanner8mm.opencv.ui.AbstractTransformPanel;
import com.zemrow.scanner8mm.opencv.ui.numberField.DoubleField;
import com.zemrow.scanner8mm.opencv.ui.numberField.DoubleModel;
import com.zemrow.scanner8mm.opencv.ui.numberField.IntegerField;
import com.zemrow.scanner8mm.opencv.ui.numberField.UnevenIntegerModel;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class TransformCannyPanel extends AbstractTransformPanel<TransformCanny> implements ChangeListener, ActionListener {
    private final DoubleField threshold1;
    private final DoubleField threshold2;
    private final IntegerField apertureSize;
    private final JCheckBox l2gradient;

    public TransformCannyPanel() {
        JPanel row = new JPanel();
        add(row);
        row.add(new JLabel("threshold1"));
        //TODO limit
        threshold1 = new DoubleField(new DoubleModel(0, 0, 2000, 1));
        threshold1.setChangeListener(this);
        row.add(threshold1);

        new JPanel();
        add(row);
        row.add(new JLabel("threshold2"));
        //TODO limit
        threshold2 = new DoubleField(new DoubleModel(0, 0, 2000, 1));
        threshold2.setChangeListener(this);
        row.add(threshold2);

        new JPanel();
        add(row);
        row.add(new JLabel("apertureSize"));
        apertureSize = new IntegerField(new UnevenIntegerModel(3, 3, 7, 1));
        apertureSize.setChangeListener(this);
        row.add(apertureSize);

        new JPanel();
        add(row);
        row.add(new JLabel("l2gradient"));
        l2gradient = new JCheckBox("l2gradient");
        l2gradient.addActionListener(this);
        row.add(l2gradient);
    }

    @Override
    protected void setFieldsFromTransform(TransformCanny transform) {
        log.info("setFieldsFromTransform " + transform.getThreshold1()
                + ", " + transform.getThreshold2()
                + ", " + transform.getApertureSize()
                + ", " + transform.isL2gradient()
        );
        threshold1.setValue(transform.getThreshold1());
        threshold2.setValue(transform.getThreshold2());
        apertureSize.setValue(transform.getApertureSize());
        l2gradient.setSelected(transform.isL2gradient());
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (listenerEnable) {
            log.info("stateChanged");
            Object source = e.getSource();
            if (threshold1 == source) {
                transform.setThreshold1(threshold1.getValue());
            } else if (threshold2 == source) {
                transform.setThreshold2(threshold2.getValue());
            } else if (apertureSize == source) {
                transform.setApertureSize(apertureSize.getValue());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (listenerEnable) {
            log.info("actionPerformed");
            Object source = e.getSource();
            if (l2gradient == source) {
                transform.setL2gradient(l2gradient.isSelected());
            }
        }
    }
}
