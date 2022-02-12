package com.zemrow.orangepi.scanner8mm.motor;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.wiringpi.Gpio;

/**
 * Bipolar stepper motor. Wave drive
 * https://3d-diy.ru/wiki/arduino-moduli/drayver-dvigatelya-l298n/
 * https://github.com/arduino-libraries/Stepper/blob/master/src/Stepper.cpp
 *
 * @author Alexandr Polyakov on 2021.08.26
 */
public class StepperMotorL298N implements IStepperMotor{

    private static final int PERIOD = 4;
    protected final int period;

    /**
     * delay between steps
     */
    private static final long STEP_DELAY = 4000;
    protected final long step_delay;

    /**
     * motor pin
     */
    protected final GpioPinDigitalOutput motor1pin0;
    protected final GpioPinDigitalOutput motor1pin1;
    protected final GpioPinDigitalOutput motor2pin2;
    protected final GpioPinDigitalOutput motor2pin3;

    /**
     * which step the motor is on
     */
    protected int step_number;
    /**
     * timestamp in us of when the last step was taken
     */
    protected long last_step_time;

    public StepperMotorL298N(GpioController gpio, Pin pin0, Pin pin1, Pin pin2, Pin pin3) {
        this(gpio.provisionDigitalOutputPin(pin0, PinState.LOW),
                gpio.provisionDigitalOutputPin(pin1, PinState.LOW),
                gpio.provisionDigitalOutputPin(pin2, PinState.LOW),
                gpio.provisionDigitalOutputPin(pin3, PinState.LOW));
    }

    public StepperMotorL298N(GpioPinDigitalOutput motor1pin0,
                             GpioPinDigitalOutput motor1pin1,
                             GpioPinDigitalOutput motor2pin2,
                             GpioPinDigitalOutput motor2pin3) {
        this(motor1pin0,
                motor1pin1,
                motor2pin2,
                motor2pin3, PERIOD, STEP_DELAY);
    }

    protected StepperMotorL298N(GpioPinDigitalOutput motor1pin0,
                                GpioPinDigitalOutput motor1pin1,
                                GpioPinDigitalOutput motor2pin2,
                                GpioPinDigitalOutput motor2pin3,
                                int period,
                                long step_delay
    ) {
        this.motor1pin0 = motor1pin0;
        this.motor1pin1 = motor1pin1;
        this.motor2pin2 = motor2pin2;
        this.motor2pin3 = motor2pin3;
        this.period = period;
        this.step_delay = step_delay;
    }

    /*
     * Moves the motor steps_to_move steps.  If the number is negative,
     * the motor moves in the reverse direction.
     */
    @Override
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
                    if (step_number == period) {
                        step_number = 0;
                    }
                } else {
                    if (step_number == 0) {
                        step_number = period;
                    }
                    step_number--;
                }
                stepMotor(step_number);
                // decrement the steps left:
                steps_left--;
            } else {
                // TODO Gpio.delayMicroseconds(-duration);
                try {
                    final long sleep = -duration / 1000;
                    if (sleep > 1 && sleep < step_delay / 1000) {
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
    protected void stepMotor(int step_number) {
        switch (step_number) {
            case 0:
                motor1pin1.low();
                motor2pin2.low();
                motor2pin3.low();
                motor1pin0.high();
                break;
            case 1:
                motor1pin0.low();
                motor1pin1.low();
                motor2pin3.low();
                motor2pin2.high();
                break;
            case 2:
                motor1pin0.low();
                motor2pin2.low();
                motor2pin3.low();
                motor1pin1.high();
                break;
            case 3:
                motor1pin0.low();
                motor1pin1.low();
                motor2pin2.low();
                motor2pin3.high();
                break;
        }
    }

    @Override
    public void enable() {
        stepMotor(step_number);
    }

    @Override
    public void disable() {
        long time = Gpio.micros();
        final long duration = time - last_step_time - step_delay;
        if (duration >= 0) {
            // get the timeStamp of when you stepped:
            last_step_time = time;
        } else {
            //TODO Gpio.delayMicroseconds(-duration);
            try {
                final long sleep = -duration / 1000;
                if (sleep > 1 && sleep < step_delay / 1000) {
                    Thread.sleep(sleep);
                }
            } catch (InterruptedException e) {
            }
        }
        motor1pin0.low();
        motor1pin1.low();
        motor2pin2.low();
        motor2pin3.low();
    }

    @Override
    public void nextFrame() {
        step(10);
    }
}
