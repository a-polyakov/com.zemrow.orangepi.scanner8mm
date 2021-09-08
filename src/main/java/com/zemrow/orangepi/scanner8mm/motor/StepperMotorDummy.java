package com.zemrow.orangepi.scanner8mm.motor;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2021.09.06
 */
public class StepperMotorDummy implements IStepperMotor {
    @Override
    public void enable() {
        System.out.println("Stepper motor enable()");
    }

    @Override
    public void disable() {
        System.out.println("Stepper motor disable()");
    }

    @Override
    public void step(int steps) {
        System.out.println("Stepper motor step(" + steps + ")");
    }

    @Override
    public void nextFrame() {
        System.out.println("Stepper motor nextFrame()");
    }
}
