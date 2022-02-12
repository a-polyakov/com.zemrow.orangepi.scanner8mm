package com.zemrow.scanner8mm.opencv.transformation.inrange;

import com.zemrow.scanner8mm.opencv.ui.numberField.DoubleField;
import com.zemrow.scanner8mm.opencv.ui.numberField.DoubleModel;
import com.zemrow.scanner8mm.opencv.ui.AbstractTransformPanel;
import com.zemrow.scanner8mm.opencv.ui.numberField.NumberField;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class TransformInRangePanel extends AbstractTransformPanel<TransformInRange> implements ChangeListener {
    private final DoubleField lowerA;
    private final DoubleField lowerB;
    private final DoubleField lowerC;
    private final DoubleField upperA;
    private final DoubleField upperB;
    private final DoubleField upperC;

    //TODO
    // slider = new JSlider(0, 200, 60);
    // slider.setPaintTicks(true);
    // slider.setMinorTickSpacing(10);
    // slider.setMajorTickSpacing(50);
    // slider.setPaintLabels(true);
    // slider.addChangeListener(this);
    // panel.add(slider);

    public TransformInRangePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addRow("lowerA", lowerA = new DoubleField(new DoubleModel(0, 0, 255, 1)));
        addRow("lowerB", lowerB = new DoubleField(new DoubleModel(0, 0, 255, 1)));
        addRow("lowerC", lowerC = new DoubleField(new DoubleModel(0, 0, 255, 1)));
        addRow("upperA", upperA = new DoubleField(new DoubleModel(0, 0, 255, 1)));
        addRow("upperB", upperB = new DoubleField(new DoubleModel(0, 0, 255, 1)));
        addRow("upperC", upperC = new DoubleField(new DoubleModel(0, 0, 255, 1)));
    }

    private void addRow(String label, NumberField field) {
        JPanel row = new JPanel();
        add(row);
        row.add(new JLabel(label));
        field.setChangeListener(this);
        row.add(field);
    }

    @Override
    protected void setFieldsFromTransform(TransformInRange transform) {
        log.info("setFieldsFromTransform " + lowerA.getValue() + ", " + lowerB.getValue() + ", " + lowerC.getValue()
                + ", " + upperA.getValue() + ", " + upperB.getValue() + ", " + upperC.getValue());
        lowerA.setValue(transform.getLowerA());
        lowerB.setValue(transform.getLowerB());
        lowerC.setValue(transform.getLowerC());
        upperA.setValue(transform.getUpperA());
        upperB.setValue(transform.getUpperB());
        upperC.setValue(transform.getUpperC());
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (listenerEnable) {
            log.info("stateChanged");
            Object source = e.getSource();
            if (lowerA == source) {
                transform.setLowerA(lowerA.getValue());
            } else if (lowerB == source) {
                transform.setLowerB(lowerB.getValue());
            } else if (lowerC == source) {
                transform.setLowerC(lowerC.getValue());
            } else if (upperA == source) {
                transform.setUpperA(upperA.getValue());
            } else if (upperB == source) {
                transform.setUpperB(upperB.getValue());
            } else if (upperC == source) {
                transform.setUpperC(upperC.getValue());
            }
        }
    }
}
