package com.zemrow.scanner8mm.opencv.transformation.cvtcolor;

import com.zemrow.scanner8mm.opencv.ui.AbstractTransformPanel;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class TransformCvtColorPanel extends AbstractTransformPanel<TransformCvtColor> implements ActionListener {

    private final JComboBox codeComboBox;

    public TransformCvtColorPanel() {
        add(new JLabel("Code"));
        codeComboBox = new JComboBox(CvtColorEnum.values());
        codeComboBox.addActionListener(this);
        add(codeComboBox);
    }

    @Override
    protected void setFieldsFromTransform(TransformCvtColor transform) {
        log.info("setFieldsFromTransform code:" + transform.getCode());
        codeComboBox.setSelectedItem(transform.getCode());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (listenerEnable) {
            log.info("actionPerformed");
            transform.setCode((CvtColorEnum) codeComboBox.getSelectedItem());
        }
    }
}
