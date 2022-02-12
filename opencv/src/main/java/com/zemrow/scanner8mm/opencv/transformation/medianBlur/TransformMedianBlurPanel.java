package com.zemrow.scanner8mm.opencv.transformation.medianBlur;

import com.zemrow.scanner8mm.opencv.ui.numberField.IntegerField;
import com.zemrow.scanner8mm.opencv.ui.numberField.UnevenIntegerModel;
import com.zemrow.scanner8mm.opencv.ui.AbstractTransformPanel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class TransformMedianBlurPanel extends AbstractTransformPanel<TransformMedianBlur> implements ChangeListener {
    private final IntegerField size;

    public TransformMedianBlurPanel() {
        JPanel row = new JPanel();
        add(row);
        row.add(new JLabel("Size"));
        size = new IntegerField(new UnevenIntegerModel(1, 1, 2000, 2));
        size.setChangeListener(this);
        row.add(size);
    }

    @Override
    protected void setFieldsFromTransform(TransformMedianBlur transform) {
        log.info("setFieldsFromTransform " + transform.getSize());
        size.setValue(transform.getSize());
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (listenerEnable) {
            log.info("stateChanged");
            Object source = e.getSource();
            if (size == source) {
                transform.setSize(size.getValue());
            }
        }
    }
}
