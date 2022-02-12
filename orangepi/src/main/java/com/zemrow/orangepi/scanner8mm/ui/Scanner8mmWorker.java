package com.zemrow.orangepi.scanner8mm.ui;

import com.zemrow.orangepi.scanner8mm.IpCam;
import com.zemrow.orangepi.scanner8mm.Perf;
import com.zemrow.orangepi.scanner8mm.StepDto;
import com.zemrow.orangepi.scanner8mm.motor.IStepperMotor;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2021.09.04
 */
public class Scanner8mmWorker extends SwingWorker<Integer, StepDto> {
    private final IStepperMotor stepperMotor;
    private final IpCam ipCam;
    private final StepViewer stepViewer;

    private boolean pause;
    private boolean next;

    public Scanner8mmWorker(IStepperMotor stepperMotor, IpCam ipCam, StepViewer stepViewer) {
        this.stepperMotor = stepperMotor;
        this.ipCam = ipCam;
        this.stepViewer = stepViewer;
        pause = false;
        next = false;
    }

    @Override
    protected Integer doInBackground() {
        while (!isCancelled()) {
            try {
                if (pause) {
                    Thread.sleep(500);
                } else {
                    final long time = System.currentTimeMillis();
                    stepperMotor.nextFrame();
                    final int stepNumber = this.stepViewer.getStepNumber();
                    final BufferedImage preview = ipCam.preview();
                    final Perf perf = Perf.findPerf(preview);
                    //TODO
                    if (perf != null) {
                        stepperMotor.step(IpCam.PREVIEW_HEIGHT / 2 - (perf.getY1() + (perf.getY2() - perf.getY1()) / 2));
                    }
                    final File file = ipCam.tackImage(stepNumber);
                    System.out.println((System.currentTimeMillis() - time) + " " + stepNumber + " write " + file.getAbsolutePath());
                    publish(new StepDto(stepNumber + 1, preview, perf));
                    if (next) {
                        next = false;
                        pause = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                pause();
            }
        }
        return 0;
    }

    public void pause() {
        pause = true;
    }

    public void next() {
        next = true;
        pause = false;
    }

    public void resume() {
        pause = false;
    }

    @Override
    protected void process(List<StepDto> chunks) {
        stepViewer.setStep(chunks.get(chunks.size() - 1));
    }
}
