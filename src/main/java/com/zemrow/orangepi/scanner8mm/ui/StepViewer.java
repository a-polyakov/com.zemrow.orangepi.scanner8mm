package com.zemrow.orangepi.scanner8mm.ui;

import com.zemrow.orangepi.scanner8mm.StepDto;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2021.09.04
 */
public interface StepViewer {
    public int getStepNumber();
    public void setStep(StepDto step);
}
