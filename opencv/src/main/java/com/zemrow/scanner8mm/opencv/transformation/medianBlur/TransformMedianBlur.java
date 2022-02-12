package com.zemrow.scanner8mm.opencv.transformation.medianBlur;

import com.zemrow.scanner8mm.opencv.transformation.AbstractTransform;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * TODO
 * @see <a href="https://www.tutorialspoint.com/opencv/opencv_median_blur.htm">MedianBlur</a>
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class TransformMedianBlur extends AbstractTransform {

    private int size;

    public TransformMedianBlur() {
        super("MedianBlur");
        size = 5;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (this.size != size) {
            this.size = size;
            fireChange();
        }
    }

    @Override
    protected Mat transformOnly(Mat source) {
        Mat result = null;
        if (source != null) {
            log.info("Imgproc.medianBlur(source, result, " + size + ")");
            result = new Mat();
            Imgproc.medianBlur(source, result, size);
        }
        return result;
    }
}
