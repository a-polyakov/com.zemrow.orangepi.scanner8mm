package com.zemrow.orangepi.scanner8mm;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.OrangePiPin;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2021.08.26
 */
public class RunScanner8mm {

    private static final int SLEEP = 500;

    public static void main(String[] args) throws PlatformAlreadyAssignedException, InterruptedException {
        PlatformManager.setPlatform(Platform.ORANGEPI);
        final GpioController gpio = GpioFactory.getInstance();

        final StepperMotor stepperMotor = new StepperMotor(gpio,
                OrangePiPin.GPIO_00,
                OrangePiPin.GPIO_01,
                OrangePiPin.GPIO_02,
                OrangePiPin.GPIO_03);

//        int i = 0;

        long time = System.currentTimeMillis();
        while (true) {
//            System.out.println(time + " " + i++);
            stepperMotor.step(20);
            stepperMotor.disable();
            long duration = SLEEP - System.currentTimeMillis() + time;
            if (duration > 0) {
                Thread.sleep(duration);
                time += SLEEP;
            } else {
                time = System.currentTimeMillis();
            }
        }
//        gpio.shutdown();
    }
}
