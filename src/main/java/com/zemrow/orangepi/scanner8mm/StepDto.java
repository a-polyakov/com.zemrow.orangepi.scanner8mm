package com.zemrow.orangepi.scanner8mm;

import java.awt.image.BufferedImage;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2021.09.08
 */
public class StepDto {
    private final int stepNumber;
    private final BufferedImage preview;
    private final Perf perf;

    public StepDto(int stepNumber, BufferedImage preview, Perf perf) {
        this.stepNumber = stepNumber;
        this.preview = preview;
        this.perf = perf;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public BufferedImage getPreview() {
        return preview;
    }

    public Perf getPerf() {
        return perf;
    }
}
