package com.zemrow.scanner8mm.opencv.ui.numberField;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class IntegerModel extends AbstractNumberModel<Integer> {

    public IntegerModel(Integer value, Integer min, Integer max, Integer step) {
        super(value, min, max, step);
    }

    @Override
    public String format(Integer value) {
        return value.toString();
    }

    @Override
    public boolean setValue(String s) {
        boolean result = false;
        try {
            if (s != null && !s.isEmpty()) {
                result = setValue(Integer.parseInt(s));
            }
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Integer takeStep(int stepCount) {
        int result = value + step * stepCount;
        if (result > max) {
            result = max;
        } else if (result < min) {
            result = min;
        }
        return result;
    }
}
