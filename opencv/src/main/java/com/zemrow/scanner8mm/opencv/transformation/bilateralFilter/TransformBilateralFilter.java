package com.zemrow.scanner8mm.opencv.transformation.bilateralFilter;

import com.zemrow.scanner8mm.opencv.transformation.AbstractTransform;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Фильтр сглаживания, уменьшающий шум, сохраняющий края
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class TransformBilateralFilter extends AbstractTransform {

    private int d;
    private double sigmaColor;
    private double sigmaSpace;
    private BorderTypesEnum borderType;

    public TransformBilateralFilter() {
        super("BilateralFilter");
        d = 5;
        sigmaColor = 80;
        sigmaSpace = 80;
        borderType = BorderTypesEnum.DEFAULT;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        if (this.d != d) {
            this.d = d;
            fireChange();
        }
    }

    public double getSigmaColor() {
        return sigmaColor;
    }

    public void setSigmaColor(double sigmaColor) {
        if (this.sigmaColor != sigmaColor) {
            this.sigmaColor = sigmaColor;
            fireChange();
        }
    }

    public double getSigmaSpace() {
        return sigmaSpace;
    }

    public void setSigmaSpace(double sigmaSpace) {
        if (this.sigmaSpace != sigmaSpace) {
            this.sigmaSpace = sigmaSpace;
            fireChange();
        }
    }

    public BorderTypesEnum getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderTypesEnum borderType) {
        if (this.borderType != borderType) {
            this.borderType = borderType;
            fireChange();
        }
    }

    @Override
    protected Mat transformOnly(Mat source) {
        Mat result = null;
        if (source != null) {
            log.info("Imgproc.bilateralFilter(source, result, " + d + ", " + sigmaColor + ", " + sigmaSpace + ", " + borderType + ")");
            result = new Mat();
            Imgproc.bilateralFilter(source, result, d, sigmaColor, sigmaSpace, borderType.getBorderType());
        }
        return result;
    }
}
