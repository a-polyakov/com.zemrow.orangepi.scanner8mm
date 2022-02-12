package com.zemrow.scanner8mm.opencv.transformation.inrange;

import com.zemrow.scanner8mm.opencv.transformation.AbstractTransform;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

/**
 * Поиск цвета
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class TransformInRange extends AbstractTransform {
    private double lowerA;
    private double lowerB;
    private double lowerC;
    private double upperA;
    private double upperB;
    private double upperC;

    public TransformInRange() {
        super("InRange");
        lowerA = 0;
        lowerB = 0;
        lowerC = 0;
        upperA = 255;
        upperB = 255;
        upperC = 255;
    }

    public double getLowerA() {
        return lowerA;
    }

    public void setLowerA(double lowerA) {
        if (this.lowerA != lowerA) {
            this.lowerA = lowerA;
            fireChange();
        }
    }

    public double getLowerB() {
        return lowerB;
    }

    public void setLowerB(double lowerB) {
        if (this.lowerB != lowerB) {
            this.lowerB = lowerB;
            fireChange();
        }
    }

    public double getLowerC() {
        return lowerC;
    }

    public void setLowerC(double lowerC) {
        if (this.lowerC != lowerC) {
            this.lowerC = lowerC;
            fireChange();
        }
    }

    public double getUpperA() {
        return upperA;
    }

    public void setUpperA(double upperA) {
        if (this.upperA != upperA) {
            this.upperA = upperA;
            fireChange();
        }
    }

    public double getUpperB() {
        return upperB;
    }

    public void setUpperB(double upperB) {
        if (this.upperB != upperB) {
            this.upperB = upperB;
            fireChange();
        }
    }

    public double getUpperC() {
        return upperC;
    }

    public void setUpperC(double upperC) {
        if (this.upperC != upperC) {
            this.upperC = upperC;
            fireChange();
        }
    }

    @Override
    protected Mat transformOnly(Mat source) {
        Mat result = null;
        if (source != null) {
            log.info("ICore.inRange(source" +
                    ", new Scalar(" + lowerA + ", " + lowerB + ", " + lowerC + ")" +
                    ", new Scalar(" + upperA + ", " + upperB + ", " + upperC + ")" +
                    ", result)");
            result = new Mat();
            Core.inRange(source, new Scalar(lowerA, lowerB, lowerC), new Scalar(upperA, upperB, upperC), result);
        }
        return result;
    }
}
