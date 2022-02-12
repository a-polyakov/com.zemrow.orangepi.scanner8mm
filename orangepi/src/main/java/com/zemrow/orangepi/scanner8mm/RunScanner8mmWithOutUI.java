package com.zemrow.orangepi.scanner8mm;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.zemrow.orangepi.scanner8mm.motor.StepperMotorTB6600;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2021.09.06
 */
public class RunScanner8mmWithOutUI {
    public static void main(String[] args) throws PlatformAlreadyAssignedException, IOException {
        PlatformManager.setPlatform(Platform.ORANGEPI);
        final GpioController gpio = GpioFactory.getInstance();

        final StepperMotorTB6600 stepperMotor = new StepperMotorTB6600(gpio);
        final IpCam ipCam = new IpCam();

        ipCam.prepare();
        stepperMotor.enable();

        for (int i = 0; true; i++) {
            final File file = ipCam.tackImage(i);
            stepperMotor.nextFrame();
            long time = System.currentTimeMillis();
            int offsetY = 0;
            final BufferedImage preview = ipCam.preview();
            final Perf perf = Perf.findPerf(preview);
            if (perf != null) {
                offsetY = IpCam.PREVIEW_HEIGHT / 2 - (perf.getY1() + (perf.getY2() - perf.getY1()) / 2);
                stepperMotor.step(offsetY);
            }

            System.out.println(time + " " + i + " offset " + offsetY + " write " + file.getAbsolutePath());
        }
    }
}
