package com.zemrow.scanner8mm.opencv.ui;

import com.zemrow.scanner8mm.opencv.transformation.AbstractTransform;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public interface ITransformPanel<T extends AbstractTransform> {
    public void setTransform(T transform);
}
