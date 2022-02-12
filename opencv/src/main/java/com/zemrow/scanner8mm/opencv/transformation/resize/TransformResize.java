package com.zemrow.scanner8mm.opencv.transformation.resize;

import com.zemrow.scanner8mm.opencv.transformation.AbstractTransform;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class TransformResize extends AbstractTransform {

    // TODO Size
    private double fx;
    private double fy;

    public TransformResize() {
        super("Resize");
        fx = 1;
        fy = 1;
    }

    public double getFx() {
        return fx;
    }

    public void setFx(double fx) {
        if (this.fx != fx) {
            this.fx = fx;
            fireChange();
        }
    }

    public double getFy() {
        return fy;
    }

    public void setFy(double fy) {
        if (this.fy != fy) {
            this.fy = fy;
            fireChange();
        }
    }

    @Override
    protected Mat transformOnly(Mat source) {
        Mat result = null;
        if (source != null) {
            log.info("Imgproc.resize(source, result, new Size(), " + fx + ", " + fy + ")");
            result = new Mat();
            Imgproc.resize(source, result, new Size(), fx, fy);
        }
        return result;
    }
}
