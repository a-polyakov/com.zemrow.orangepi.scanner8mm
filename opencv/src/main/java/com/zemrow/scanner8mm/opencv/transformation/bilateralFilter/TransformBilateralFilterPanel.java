package com.zemrow.scanner8mm.opencv.transformation.bilateralFilter;

import com.zemrow.scanner8mm.opencv.ui.AbstractTransformPanel;
import com.zemrow.scanner8mm.opencv.ui.numberField.DoubleField;
import com.zemrow.scanner8mm.opencv.ui.numberField.DoubleModel;
import com.zemrow.scanner8mm.opencv.ui.numberField.IntegerField;
import com.zemrow.scanner8mm.opencv.ui.numberField.IntegerModel;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
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
public class TransformBilateralFilterPanel extends AbstractTransformPanel<TransformBilateralFilter> implements ChangeListener, ActionListener {
    private final IntegerField d;
    private final DoubleField sigmaColor;
    private final DoubleField sigmaSpace;
    private final JComboBox borderType;

    public TransformBilateralFilterPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel row = new JPanel();
        add(row);
        row.add(new JLabel("D"));
        //TODO
        d = new IntegerField(new IntegerModel(1, 1, 2000, 1));
        d.setChangeListener(this);
        row.add(d);

        row = new JPanel();
        add(row);
        row.add(new JLabel("sigmaColor"));
        //TODO
        sigmaColor = new DoubleField(new DoubleModel(1, 1, 2000, 1));
        sigmaColor.setChangeListener(this);
        row.add(sigmaColor);

        row = new JPanel();
        add(row);
        row.add(new JLabel("sigmaSpace"));
        //TODO
        sigmaSpace = new DoubleField(new DoubleModel(1, 1, 2000, 1));
        sigmaSpace.setChangeListener(this);
        row.add(sigmaSpace);

        row = new JPanel();
        add(row);
        row.add(new JLabel("borderType"));
        borderType = new JComboBox(BorderTypesEnum.values());
        borderType.addActionListener(this);
        row.add(borderType);
    }


    @Override
    protected void setFieldsFromTransform(TransformBilateralFilter transform) {
        log.info("setFieldsFromTransform " + transform.getD()
                + ", " + transform.getSigmaColor()
                + ", " + transform.getSigmaSpace()
                + ", " + transform.getBorderType()
        );
        d.setValue(transform.getD());
        sigmaColor.setValue(transform.getSigmaColor());
        sigmaSpace.setValue(transform.getSigmaSpace());
        borderType.setSelectedItem(transform.getBorderType());
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (listenerEnable) {
            log.info("stateChanged");
            Object source = e.getSource();
            if (d == source) {
                transform.setD(d.getValue());
            } else if (sigmaColor == source) {
                transform.setSigmaColor(sigmaColor.getValue());
            } else if (sigmaSpace == source) {
                transform.setSigmaSpace(sigmaSpace.getValue());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        log.info("actionPerformed");
        Object source = event.getSource();
        if (borderType == source) {
            BorderTypesEnum select = (BorderTypesEnum) borderType.getSelectedItem();
            transform.setBorderType(select);
        }
    }
}
