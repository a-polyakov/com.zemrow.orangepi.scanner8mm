package com.zemrow.scanner8mm.opencv.transformation.canny;

import com.zemrow.scanner8mm.opencv.transformation.AbstractTransform;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * TODO
 * https://www.tutorialspoint.com/opencv/opencv_canny_edge_detection.htm
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class TransformCanny extends AbstractTransform {

    private double threshold1;
    private double threshold2;
    private int apertureSize;
    private boolean l2gradient;

    public TransformCanny() {
        super("Canny");
        threshold1 = 60;
        threshold2 = 60 * 3;
        apertureSize = 3;
        l2gradient = false;
    }

    public double getThreshold1() {
        return threshold1;
    }

    public void setThreshold1(double threshold1) {
        if (this.threshold1 != threshold1) {
            this.threshold1 = threshold1;
            fireChange();
        }
    }

    public double getThreshold2() {
        return threshold2;
    }

    public void setThreshold2(double threshold2) {
        if (this.threshold2 != threshold2) {
            this.threshold2 = threshold2;
            fireChange();
        }
    }

    public int getApertureSize() {
        return apertureSize;
    }

    public void setApertureSize(int apertureSize) {
        if (this.apertureSize != apertureSize) {
            this.apertureSize = apertureSize;
            fireChange();
        }
    }

    public boolean isL2gradient() {
        return l2gradient;
    }

    public void setL2gradient(boolean l2gradient) {
        if (this.l2gradient != l2gradient) {
            this.l2gradient = l2gradient;
            fireChange();
        }
    }

    @Override
    protected Mat transformOnly(Mat source) {
        Mat result = null;
        if (source != null) {
            log.info("Imgproc.Canny(source, result" +
                    ", " + threshold1 +
                    ", " + threshold2 +
                    ", " + apertureSize +
                    ", " + l2gradient +
                    ")");
            result = new Mat();
            Imgproc.Canny(source, result, threshold1, threshold2, apertureSize, l2gradient);
        }
        return result;
    }
}
