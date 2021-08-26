package com.zemrow.orangepi.scanner8mm;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.Gpio;

/**
 * TODO
 * https://3d-diy.ru/wiki/arduino-moduli/drayver-dvigatelya-l298n/
 * https://github.com/arduino-libraries/Stepper/blob/master/src/Stepper.cpp
 *
 * @author Alexandr Polyakov on 2021.08.26
 */
public class StepperMotor {

    private static final int PERIOD = 8;

    /**
     * motor pin
     */
    GpioPinDigitalOutput motor_pin0;
    GpioPinDigitalOutput motor_pin1;
    GpioPinDigitalOutput motor_pin2;
    GpioPinDigitalOutput motor_pin3;

    /**
     * delay between steps, in ms, based on speed
     */
    private static long step_delay = 4000;
    /**
     * which step the motor is on
     */
    int step_number;
    /**
     * timestamp in us of when the last step was taken
     */
    long last_step_time;

    public StepperMotor(GpioController gpio, Pin pin0, Pin pin1, Pin pin2, Pin pin3) {
        this.motor_pin0 = gpio.provisionDigitalOutputPin(pin0, PinState.LOW);
        this.motor_pin1 = gpio.provisionDigitalOutputPin(pin1, PinState.LOW);
        this.motor_pin2 = gpio.provisionDigitalOutputPin(pin2, PinState.LOW);
        this.motor_pin3 = gpio.provisionDigitalOutputPin(pin3, PinState.LOW);
    }

    /*
     * Moves the motor steps_to_move steps.  If the number is negative,
     * the motor moves in the reverse direction.
     */
    public void step(int steps_to_move) {
        int steps_left = Math.abs(steps_to_move);  // how many steps to take

        // decrement the number of steps, moving one step each time:
        while (steps_left > 0) {
            long time = Gpio.micros();
            // move only if the appropriate delay has passed:
            final long duration = time - last_step_time - step_delay;
            if (duration >= 0) {
                // get the timeStamp of when you stepped:
                last_step_time = time;
                // increment or decrement the step number,
                // depending on direction:
                if (steps_to_move > 0) {
                    step_number++;
                    if (step_number == PERIOD) {
                        step_number = 0;
                    }
                } else {
                    if (step_number == 0) {
                        step_number = PERIOD;
                    }
                    step_number--;
                }
                // decrement the steps left:
                steps_left--;
                // step the motor to step number 0, 1, ..., {3 or 10}
                stepMotor(step_number);
            } else {
                try {
                    final long sleep = -duration / 1000;
                    if (sleep > 1 && sleep < step_delay / 1000){
                        Thread.sleep(sleep);
                    }
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /*
     * Moves the motor forward or backwards.
     */
    private void stepMotor(int step_number) {
        switch (step_number) {
            case 0:
                motor_pin1.low();
                motor_pin2.low();
                motor_pin3.low();
                motor_pin0.high();
                break;
            case 1:
                motor_pin1.low();
                motor_pin3.low();
                motor_pin0.high();
                motor_pin2.high();
                break;
            case 2:
                motor_pin0.low();
                motor_pin1.low();
                motor_pin3.low();
                motor_pin2.high();
                break;
            case 3:
                motor_pin0.low();
                motor_pin3.low();
                motor_pin1.high();
                motor_pin2.high();
                break;
            case 4:
                motor_pin0.low();
                motor_pin2.low();
                motor_pin3.low();
                motor_pin1.high();
                break;
            case 5:
                motor_pin0.low();
                motor_pin2.low();
                motor_pin1.high();
                motor_pin3.high();
                break;
            case 6:
                motor_pin0.low();
                motor_pin1.low();
                motor_pin2.low();
                motor_pin3.high();
                break;
            case 7:
                motor_pin1.low();
                motor_pin2.low();
                motor_pin0.high();
                motor_pin3.high();
                break;
        }
    }

    public void disable() {
        long time = Gpio.micros();
        final long duration = time - last_step_time - step_delay;
        if (duration >= 0) {
            // get the timeStamp of when you stepped:
            last_step_time = time;
        } else {
            try {
                final long sleep = -duration / 1000;
                if (sleep > 1 && sleep < step_delay / 1000){
                    Thread.sleep(sleep);
                }
            } catch (InterruptedException e) {
            }
        }
        motor_pin0.low();
        motor_pin1.low();
        motor_pin2.low();
        motor_pin3.low();
    }
}
