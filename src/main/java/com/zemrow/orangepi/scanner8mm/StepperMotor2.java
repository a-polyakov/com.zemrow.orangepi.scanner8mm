package com.zemrow.orangepi.scanner8mm;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

/**
 * Bipolar stepper motor. Half-stepping
 * https://3d-diy.ru/wiki/arduino-moduli/drayver-dvigatelya-l298n/
 * https://github.com/arduino-libraries/Stepper/blob/master/src/Stepper.cpp
 *
 * @author Alexandr Polyakov on 2021.08.31
 */
public class StepperMotor2 extends StepperMotor {

    private static final int PERIOD = 8;

    /**
     * delay between steps, in ms, based on speed
     */
    private static final long STEP_DELAY = 4000;

    public StepperMotor2(GpioController gpio, Pin pin0, Pin pin1, Pin pin2, Pin pin3) {
        this(gpio.provisionDigitalOutputPin(pin0, PinState.LOW),
                gpio.provisionDigitalOutputPin(pin1, PinState.LOW),
                gpio.provisionDigitalOutputPin(pin2, PinState.LOW),
                gpio.provisionDigitalOutputPin(pin3, PinState.LOW));
    }

    public StepperMotor2(GpioPinDigitalOutput motor1pin0,
                         GpioPinDigitalOutput motor1pin1,
                         GpioPinDigitalOutput motor2pin2,
                         GpioPinDigitalOutput motor2pin3) {
        super(motor1pin0, motor1pin1, motor2pin2, motor2pin3, PERIOD, STEP_DELAY);
    }

    /*
     * Moves the motor forward or backwards.
     */
    @Override
    protected void stepMotor(int step_number) {
        switch (step_number) {
            case 0:
                motor1pin1.low();
                motor2pin2.low();
                motor2pin3.low();
                motor1pin0.high();
                break;
            case 1:
                motor1pin1.low();
                motor2pin3.low();
                motor1pin0.high();
                motor2pin2.high();
                break;
            case 2:
                motor1pin0.low();
                motor1pin1.low();
                motor2pin3.low();
                motor2pin2.high();
                break;
            case 3:
                motor1pin0.low();
                motor2pin3.low();
                motor1pin1.high();
                motor2pin2.high();
                break;
            case 4:
                motor1pin0.low();
                motor2pin2.low();
                motor2pin3.low();
                motor1pin1.high();
                break;
            case 5:
                motor1pin0.low();
                motor2pin2.low();
                motor1pin1.high();
                motor2pin3.high();
                break;
            case 6:
                motor1pin0.low();
                motor1pin1.low();
                motor2pin2.low();
                motor2pin3.high();
                break;
            case 7:
                motor1pin1.low();
                motor2pin2.low();
                motor1pin0.high();
                motor2pin3.high();
                break;
        }
    }
}
