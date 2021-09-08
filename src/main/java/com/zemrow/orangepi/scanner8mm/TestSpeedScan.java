package com.zemrow.orangepi.scanner8mm;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.zemrow.orangepi.scanner8mm.motor.StepperMotorTB6600;

/**
 * TODO
 * Подобрать скорость протяжки так чтобы совпали частоты обновления камеры и новый кадр на той-же позиции
 *
 * @author Alexandr Polyakov on 2021.09.06
 */
public class TestSpeedScan {
    public static void main(String[] args) throws PlatformAlreadyAssignedException {
        PlatformManager.setPlatform(Platform.ORANGEPI);
        final GpioController gpio = GpioFactory.getInstance();

        final StepperMotorTB6600 stepperMotor = new StepperMotorTB6600(gpio);
        stepperMotor.enable();
        for (int d = 60; d > 50; d -= 2) {
            stepperMotor.step(-10 * 20 * 320, d);
            System.out.println("delay:" + d);
        }
        stepperMotor.disable();
        gpio.shutdown();
    }
}
