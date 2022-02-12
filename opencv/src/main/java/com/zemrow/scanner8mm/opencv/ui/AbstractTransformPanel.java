package com.zemrow.scanner8mm.opencv.ui;

import com.zemrow.scanner8mm.opencv.transformation.AbstractTransform;

import javax.swing.JPanel;
import java.util.logging.Logger;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public abstract class AbstractTransformPanel<T extends AbstractTransform> extends JPanel implements ITransformPanel<T>{

    protected final Logger log = Logger.getLogger(getClass().getName());

    protected T transform;

    protected boolean listenerEnable;

    @Override
    public final void setTransform(T transform) {
        this.transform=transform;
        listenerEnable=false;
        setFieldsFromTransform(transform);
        listenerEnable=true;
    }

    protected abstract void setFieldsFromTransform(T transform);
}
