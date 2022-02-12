package com.zemrow.scanner8mm.opencv.ui.numberField;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public abstract class AbstractNumberModel<T extends Number & Comparable> {

    protected T value;
    protected T min;
    protected T max;
    protected T step;

    private ChangeListener changeListener;

    public AbstractNumberModel(T value, T min, T max, T step) {
        this.value = value;
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public T getValue() {
        return value;
    }

    public boolean setValue(T value) {
        boolean result = true;
        if (!this.value.equals(value)) {
            result = isVerify(value);
            if (result) {
                this.value = value;
                if (changeListener != null) {
                    changeListener.stateChanged(new ChangeEvent(this));
                }
            }
        }
        return result;
    }

    public boolean isVerify(T value) {
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }

    public abstract boolean setValue(String s);

    public abstract T takeStep(int stepCount);

    public T getMin() {
        return min;
    }

    public void setMin(T min) {
        if (min.compareTo(max) < 0) {
            this.min = min;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public T getMax() {
        return max;
    }

    public void setMax(T max) {
        if (max.compareTo(min) > 0) {
            this.max = max;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public T getStep() {
        return step;
    }

    public void setStep(T step) {
        this.step = step;
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public abstract String format(T value);
}
