package com.zemrow.scanner8mm.opencv.ui.numberField;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class UnevenIntegerModel extends IntegerModel {
    public UnevenIntegerModel(Integer value, Integer min, Integer max, Integer step) {
        super(value, min, max, step);
    }

    @Override
    public boolean isVerify(Integer value) {
        boolean verify = super.isVerify(value) && value % 2 == 1;
        return verify;
    }
}
