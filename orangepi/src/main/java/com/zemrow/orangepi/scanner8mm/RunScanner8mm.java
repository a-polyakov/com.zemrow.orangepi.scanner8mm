package com.zemrow.orangepi.scanner8mm;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.zemrow.orangepi.scanner8mm.motor.StepperMotorTB6600;
import com.zemrow.orangepi.scanner8mm.ui.Scanner8mmFrame;

import java.io.IOException;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2021.08.26
 */
public class RunScanner8mm {

    public static void main(String[] args) throws PlatformAlreadyAssignedException, InterruptedException, IOException {
        PlatformManager.setPlatform(Platform.ORANGEPI);
        final GpioController gpio = GpioFactory.getInstance();

        final StepperMotorTB6600 stepperMotor = new StepperMotorTB6600(gpio);
        final IpCam ipCam = new IpCam();

        ipCam.prepare();
        stepperMotor.enable();

        new Scanner8mmFrame(stepperMotor, ipCam);

//        tb6600.disable();
//        gpio.shutdown();
    }
}
