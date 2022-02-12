package com.zemrow.scanner8mm.opencv.transformation.bilateralFilter;

import org.opencv.core.Core;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public enum BorderTypesEnum {
    CONSTANT(Core.BORDER_CONSTANT),
    REPLICATE(Core.BORDER_REPLICATE),
    REFLECT(Core.BORDER_REFLECT),
    WRAP(Core.BORDER_WRAP),
    REFLECT_101(Core.BORDER_REFLECT_101),
    TRANSPARENT(Core.BORDER_TRANSPARENT),
    REFLECT101(Core.BORDER_REFLECT101),
    DEFAULT(Core.BORDER_DEFAULT),
    ISOLATED(Core.BORDER_ISOLATED);

    private final int borderType;

    BorderTypesEnum(int borderType) {
        this.borderType = borderType;
    }

    public int getBorderType() {
        return borderType;
    }
}
