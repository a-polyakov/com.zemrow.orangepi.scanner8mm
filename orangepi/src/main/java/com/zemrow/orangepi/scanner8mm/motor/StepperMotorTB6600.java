package com.zemrow.orangepi.scanner8mm.motor;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.OrangePiPin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.Gpio;

/**
 * Bipolar stepper motor. TODO
 *
 * @author Alexandr Polyakov on 2021.09.04
 */
public class StepperMotorTB6600 implements IStepperMotor{
    private final int FRAME_STEEP = 320;
    private final int DELAY_MIN = 100;
    private final int DELAY_MAX = 500;

    private final int array[];

    private final GpioPinDigitalOutput ena;
    private final GpioPinDigitalOutput dir;
    private final GpioPinDigitalOutput put;

    public StepperMotorTB6600(final GpioController gpio) {
        array = new int[FRAME_STEEP];
        final double d = Math.PI / (FRAME_STEEP / 2);
        double a = 0;
        for (int i = 0; i < FRAME_STEEP; i++) {
            // min+(max-min)*(1+Math.cos(a))/2;
            array[i] = (int) (DELAY_MIN / 2 + DELAY_MAX / 2 + (DELAY_MAX - DELAY_MIN) / 2 * Math.cos(a));
            a += d;
        }

        ena = gpio.provisionDigitalOutputPin(OrangePiPin.GPIO_04, PinState.HIGH);
        dir = gpio.provisionDigitalOutputPin(OrangePiPin.GPIO_05, PinState.LOW);
        put = gpio.provisionDigitalOutputPin(OrangePiPin.GPIO_06, PinState.HIGH);
    }

    @Override
    public void enable() {
        ena.low();
    }

    @Override
    public void disable() {
        ena.high();
    }

    @Override
    public void nextFrame() {
        dir.low();
        for (int delay : array) {
            put.low();
            Gpio.delayMicroseconds(delay);
            put.high();
            Gpio.delayMicroseconds(delay);
        }
    }

    @Override
    public void step(int steps) {
        step(steps, DELAY_MAX);
    }

    public void step(int steps, int delay) {
        dir.setState(steps>0);
        for (int i=Math.abs(steps);i>0;i--){
            put.low();
            Gpio.delayMicroseconds(delay);
            put.high();
            Gpio.delayMicroseconds(delay);
        }
    }
}
