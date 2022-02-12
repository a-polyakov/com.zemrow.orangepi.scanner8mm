package com.zemrow.scanner8mm.opencv.ui.numberField;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class DoubleModel extends AbstractNumberModel<Double> {

    private static final DecimalFormat numberFormat;

    static {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormatSymbols.setGroupingSeparator(' ');
        numberFormat = new DecimalFormat("#,##0.###", decimalFormatSymbols);
    }

    public DoubleModel(double value, double min, double max, double step) {
        super(value, min, max, step);
    }

    @Override
    public String format(Double value) {
        return numberFormat.format(value);
    }

    @Override
    public boolean setValue(String s) {
        boolean result = false;
        try {
            if (s != null && !s.isEmpty()) {
                result = setValue(numberFormat.parse(s).doubleValue());
            }
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Double takeStep(int stepCount) {
        double result = value + step * stepCount;
        if (result > max) {
            result = max;
        } else if (result < min) {
            result = min;
        }
        return result;
    }
}
