package com.zemrow.orangepi.scanner8mm.motor;

/**
 * Interface for stepper motor
 *
 * @author Alexandr Polyakov on 2021.09.06
 */
public interface IStepperMotor {
    public void enable();
    public void disable();
    public void step(int steps);
    public void nextFrame();
}
