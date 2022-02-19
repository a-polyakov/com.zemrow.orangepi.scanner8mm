package com.zemrow.scanner8mm.opencv.transformation.gaussianBlur;

import com.zemrow.scanner8mm.opencv.transformation.AbstractTransform;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Размытие
 *
 * @author Alexandr Polyakov on 2022.02.12
 * @see <a href="https://www.tutorialspoint.com/opencv/opencv_gaussian_blur.htm">GaussianBlur</a>
 */
public class TransformGaussianBlur extends AbstractTransform {

    private int sizeWidth;
    private int sizeHeight;
    // TODO double sigmaX, double sigmaY, int borderType

    public TransformGaussianBlur() {
        super("GaussianBlur");
        sizeWidth = 5;
        sizeHeight = 5;
        // TODO double sigmaX, double sigmaY, int borderType
    }

    public int getSizeWidth() {
        return sizeWidth;
    }

    public void setSizeWidth(int size) {
        if (this.sizeWidth != size) {
            this.sizeWidth = size;
            fireChange();
        }
    }

    public int getSizeHeight() {
        return sizeHeight;
    }

    public void setSizeHeight(int size) {
        if (this.sizeHeight != size) {
            this.sizeHeight = size;
            fireChange();
        }
    }

    @Override
    protected Mat transformOnly(Mat source) {
        Mat result = null;
        if (source != null) {
            // TODO double sigmaX, double sigmaY, int borderType
            log.info("Imgproc.GaussianBlur(source, result, new Size(" + sizeWidth + ", " + sizeHeight + "), 0)");
            result = new Mat();
            // TODO double sigmaX, double sigmaY, int borderType
            Imgproc.GaussianBlur(source, result, new Size(sizeWidth, sizeHeight), 0);
        }
        return result;
    }
}
