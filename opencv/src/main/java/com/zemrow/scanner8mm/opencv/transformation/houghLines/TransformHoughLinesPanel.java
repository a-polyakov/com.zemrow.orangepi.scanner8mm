package com.zemrow.scanner8mm.opencv.transformation.houghLines;

import com.zemrow.scanner8mm.opencv.ui.AbstractTransformPanel;
import com.zemrow.scanner8mm.opencv.ui.numberField.DoubleField;
import com.zemrow.scanner8mm.opencv.ui.numberField.DoubleModel;
import com.zemrow.scanner8mm.opencv.ui.numberField.IntegerField;
import com.zemrow.scanner8mm.opencv.ui.numberField.IntegerModel;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.19
 */
public class TransformHoughLinesPanel extends AbstractTransformPanel<TransformHoughLines> implements ChangeListener, ActionListener {

    private final DoubleField rho;
    private final DoubleField theta;
    private final IntegerField threshold;
    private final DoubleField minLineLength;
    private final DoubleField maxLineGap;
    private final JButton colorButton;

    public TransformHoughLinesPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel row = new JPanel();
        add(row);
        JLabel label = new JLabel("rho");
        label.setToolTipText("Distance resolution of the accumulator in pixels.");
        row.add(label);
        //TODO limit
        this.rho = new DoubleField(new DoubleModel(1, 0, 4000, 1));
        this.rho.setChangeListener(this);
        row.add(this.rho);

        row = new JPanel();
        add(row);
        label = new JLabel("theta");
        label.setToolTipText("Angle resolution of the accumulator in radians.");
        row.add(label);
        //TODO limit
        this.theta = new DoubleField(new DoubleModel(0, 0, 4000, 1));
        this.theta.setChangeListener(this);
        row.add(this.theta);

        row = new JPanel();
        add(row);
        label = new JLabel("threshold");
        label.setToolTipText("Accumulator threshold parameter. Only those lines are returned that get enough votes (threshold).");
        row.add(label);
        //TODO limit
        this.threshold = new IntegerField(new IntegerModel(3, 0, 1000, 1));
        this.threshold.setChangeListener(this);
        row.add(this.threshold);

        row = new JPanel();
        add(row);
        label = new JLabel("minLineLength");
        label.setToolTipText("Minimum line length. Line segments shorter than that are rejected.");
        row.add(label);
        //TODO limit
        this.minLineLength = new DoubleField(new DoubleModel(0, 0, 4000, 1));
        this.minLineLength.setChangeListener(this);
        row.add(this.minLineLength);

        row = new JPanel();
        add(row);
        label = new JLabel("maxLineGap");
        label.setToolTipText("Maximum allowed gap between points on the same line to link them. SEE: LineSegmentDetector");
        row.add(label);
        //TODO limit
        this.maxLineGap = new DoubleField(new DoubleModel(0, 0, 4000, 1));
        this.maxLineGap.setChangeListener(this);
        row.add(this.maxLineGap);

        row = new JPanel();
        add(row);
        row.add(new JLabel("Color"));
        colorButton = new JButton("Select");
        colorButton.addActionListener(this);
        row.add(colorButton);
    }

    @Override
    protected void setFieldsFromTransform(TransformHoughLines transform) {
        log.info("setFieldsFromTransform "
                + transform.getRho()
                + ", " + transform.getTheta()
                + ", " + transform.getThreshold()
                + ", " + transform.getMinLineLength()
                + ", " + transform.getMaxLineGap()
                + ", " + transform.getColor()
        );
        rho.setValue(transform.getRho());
        theta.setValue(transform.getTheta());
        threshold.setValue(transform.getThreshold());
        minLineLength.setValue(transform.getMinLineLength());
        maxLineGap.setValue(transform.getMaxLineGap());
        colorButton.setBackground(transform.getColor());
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (listenerEnable) {
            log.info("stateChanged");
            Object source = e.getSource();
            if (rho == source) {
                transform.setRho(rho.getValue());
            } else if (theta == source) {
                transform.setTheta(theta.getValue());
            } else if (threshold == source) {
                transform.setThreshold(threshold.getValue());
            } else if (minLineLength == source) {
                transform.setMinLineLength(minLineLength.getValue());
            } else if (maxLineGap == source) {
                transform.setMaxLineGap(maxLineGap.getValue());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (listenerEnable) {
            log.info("actionPerformed");
            Object source = e.getSource();
            if (colorButton == source) {
                Color newColor = JColorChooser.showDialog(
                        this,
                        "Choose color",
                        colorButton.getBackground());
                if (newColor != null) {
                    colorButton.setBackground(newColor);
                    transform.setColor(newColor);
                }
            }
        }
    }
}
