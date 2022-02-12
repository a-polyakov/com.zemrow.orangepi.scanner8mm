package com.zemrow.orangepi.scanner8mm.motor;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

/**
 * Bipolar stepper motor. TODO
 * https://3d-diy.ru/wiki/arduino-moduli/drayver-dvigatelya-l298n/
 * https://github.com/arduino-libraries/Stepper/blob/master/src/Stepper.cpp
 *
 * @author Alexandr Polyakov on 2021.08.31
 */
public class StepperMotorL298N4 extends StepperMotorL298N {

    private static final int PERIOD = 16;

    /**
     * delay between steps, in ms, based on speed
     */
    private static long STEP_DELAY = 2000;

    /**
     * motor pin
     */
    protected final GpioPinPwmOutput motor1pinPwm;
    protected final GpioPinPwmOutput motor2pinPwm;

    public StepperMotorL298N4(GpioController gpio, Pin pin0, Pin pin1, Pin pin2, Pin pin3, Pin pin4, Pin pin5) {
        this(gpio.provisionDigitalOutputPin(pin0, PinState.LOW),
                gpio.provisionDigitalOutputPin(pin1, PinState.LOW),
                gpio.provisionDigitalOutputPin(pin2, PinState.LOW),
                gpio.provisionDigitalOutputPin(pin3, PinState.LOW),
                gpio.provisionSoftPwmOutputPin(pin4),
                gpio.provisionSoftPwmOutputPin(pin5));
    }

    public StepperMotorL298N4(GpioPinDigitalOutput motor1pin0, GpioPinDigitalOutput motor1pin1, GpioPinDigitalOutput motor2pin2, GpioPinDigitalOutput motor2pin3, GpioPinPwmOutput motor1pinPwm, GpioPinPwmOutput motor2pinPwm) {
        super(motor1pin0, motor1pin1, motor2pin2, motor2pin3, PERIOD, STEP_DELAY);
        this.motor1pinPwm = motor1pinPwm;
        this.motor2pinPwm = motor2pinPwm;
        motor1pinPwm.setPwmRange(100);
        motor2pinPwm.setPwmRange(100);
    }

    /*
     * Moves the motor forward or backwards.
     */
    @Override
    protected void stepMotor(int step_number) {
        switch (step_number) {
            case 0:
                motor1pinPwm.setPwm(100);
                motor2pinPwm.setPwm(0);
                motor1pin1.low();
                motor2pin2.low();
                motor2pin3.low();
                motor1pin0.high();
                break;
            case 1:
                motor1pinPwm.setPwm(100);
                motor2pinPwm.setPwm(50);
                motor1pin1.low();
                motor2pin3.low();
                motor1pin0.high();
                motor2pin2.high();
                break;
            case 2:
                motor1pinPwm.setPwm(100);
                motor2pinPwm.setPwm(100);
                motor1pin1.low();
                motor2pin3.low();
                motor1pin0.high();
                motor2pin2.high();
                break;
            case 3:
                motor1pinPwm.setPwm(50);
                motor2pinPwm.setPwm(100);
                motor1pin1.low();
                motor2pin3.low();
                motor1pin0.high();
                motor2pin2.high();
                break;
            case 4:
                motor1pinPwm.setPwm(0);
                motor2pinPwm.setPwm(100);
                motor1pin0.low();
                motor1pin1.low();
                motor2pin3.low();
                motor2pin2.high();
                break;
            case 5:
                motor1pinPwm.setPwm(50);
                motor2pinPwm.setPwm(100);
                motor1pin0.low();
                motor2pin3.low();
                motor1pin1.high();
                motor2pin2.high();
                break;
            case 6:
                motor1pinPwm.setPwm(100);
                motor2pinPwm.setPwm(100);
                motor1pin0.low();
                motor2pin3.low();
                motor1pin1.high();
                motor2pin2.high();
                break;
            case 7:
                motor1pinPwm.setPwm(100);
                motor2pinPwm.setPwm(50);
                motor1pin0.low();
                motor2pin3.low();
                motor1pin1.high();
                motor2pin2.high();
                break;
            case 8:
                motor1pinPwm.setPwm(100);
                motor2pinPwm.setPwm(0);
                motor1pin0.low();
                motor2pin2.low();
                motor2pin3.low();
                motor1pin1.high();
                break;
            case 9:
                motor1pinPwm.setPwm(100);
                motor2pinPwm.setPwm(50);
                motor1pin0.low();
                motor2pin2.low();
                motor1pin1.high();
                motor2pin3.high();
                break;
            case 10:
                motor1pinPwm.setPwm(100);
                motor2pinPwm.setPwm(100);
                motor1pin0.low();
                motor2pin2.low();
                motor1pin1.high();
                motor2pin3.high();
                break;
            case 11:
                motor1pinPwm.setPwm(50);
                motor2pinPwm.setPwm(100);
                motor1pin0.low();
                motor2pin2.low();
                motor1pin1.high();
                motor2pin3.high();
                break;
            case 12:
                motor1pinPwm.setPwm(0);
                motor2pinPwm.setPwm(100);
                motor1pin0.low();
                motor1pin1.low();
                motor2pin2.low();
                motor2pin3.high();
                break;
            case 13:
                motor1pinPwm.setPwm(50);
                motor2pinPwm.setPwm(100);
                motor1pin1.low();
                motor2pin2.low();
                motor1pin0.high();
                motor2pin3.high();
                break;
            case 14:
                motor1pinPwm.setPwm(100);
                motor2pinPwm.setPwm(100);
                motor1pin1.low();
                motor2pin2.low();
                motor1pin0.high();
                motor2pin3.high();
                break;
            case 15:
                motor1pinPwm.setPwm(100);
                motor2pinPwm.setPwm(50);
                motor1pin1.low();
                motor2pin2.low();
                motor1pin0.high();
                motor2pin3.high();
                break;
        }
    }

    @Override
    public void disable() {
        super.disable();
        motor1pinPwm.setPwm(0);
        motor2pinPwm.setPwm(0);
    }

    @Override
    public void nextFrame() {
        step(40);
    }
}
