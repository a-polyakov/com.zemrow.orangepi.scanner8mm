package com.zemrow.scanner8mm.opencv.transformation.gaussianBlur;

import com.zemrow.scanner8mm.opencv.ui.AbstractTransformPanel;
import com.zemrow.scanner8mm.opencv.ui.numberField.IntegerField;
import com.zemrow.scanner8mm.opencv.ui.numberField.UnevenIntegerModel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class TransformGaussianBlurPanel extends AbstractTransformPanel<TransformGaussianBlur> implements ChangeListener {
    private final IntegerField sizeWidth;
    private final IntegerField sizeHeight;
    // TODO double sigmaX, double sigmaY, int borderType

    public TransformGaussianBlurPanel() {
        JPanel row = new JPanel();
        add(row);
        row.add(new JLabel("Size Width"));
        //TODO limit
        sizeWidth = new IntegerField(new UnevenIntegerModel(1, 1, 2000, 2));
        sizeWidth.setChangeListener(this);
        row.add(sizeWidth);

        row = new JPanel();
        add(row);
        row.add(new JLabel("Size Height"));
        //TODO limit
        sizeHeight = new IntegerField(new UnevenIntegerModel(1, 1, 2000, 2));
        sizeHeight.setChangeListener(this);
        row.add(sizeHeight);
    }

    @Override
    protected void setFieldsFromTransform(TransformGaussianBlur transform) {
        log.info("setFieldsFromTransform " + transform.getSizeWidth() + ", " + transform.getSizeHeight());
        sizeWidth.setValue(transform.getSizeWidth());
        sizeHeight.setValue(transform.getSizeHeight());
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (listenerEnable) {
            log.info("stateChanged");
            Object source = e.getSource();
            if (sizeWidth == source) {
                transform.setSizeWidth(sizeWidth.getValue());
            } else if (sizeHeight == source) {
                transform.setSizeHeight(sizeHeight.getValue());
            }
        }
    }
}
