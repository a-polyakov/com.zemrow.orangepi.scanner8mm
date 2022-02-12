package com.zemrow.orangepi.scanner8mm;

import com.zemrow.orangepi.scanner8mm.motor.StepperMotorDummy;
import com.zemrow.orangepi.scanner8mm.ui.Scanner8mmFrame;

import java.io.IOException;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2021.09.06
 */
public class TestWithOutGpio {
    public static void main(String[] args) throws IOException {

        final StepperMotorDummy stepperMotor = new StepperMotorDummy();
        final IpCam ipCam = new IpCam();
        ipCam.prepare();

        new Scanner8mmFrame(stepperMotor, ipCam);
    }
}
