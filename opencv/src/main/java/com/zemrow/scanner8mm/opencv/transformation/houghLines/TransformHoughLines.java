package com.zemrow.scanner8mm.opencv.transformation.houghLines;

import com.zemrow.scanner8mm.opencv.transformation.AbstractTransform;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.awt.Color;

/**
 * TODO
 * https://www.tutorialspoint.com/opencv/opencv_hough_line_transform.htm
 *
 * @author Alexandr Polyakov on 2022.02.19
 */
public class TransformHoughLines extends AbstractTransform {
    /**
     * Distance resolution of the accumulator in pixels.
     */
    private double rho;
    /**
     * Angle resolution of the accumulator in radians.
     */
    private double theta;
    /**
     * Accumulator threshold parameter. Only those lines are returned that get enough votes (threshold).
     */
    private int threshold;
    /**
     * Minimum line length. Line segments shorter than that are rejected.
     */
    private double minLineLength;
    /**
     * Maximum allowed gap between points on the same line to link them. SEE: LineSegmentDetector
     */
    private double maxLineGap;

    private Color color;

    public TransformHoughLines() {
        super("HoughLines");
        rho = 1;
        theta = Math.PI / 180;
        threshold = 100;
        minLineLength = 100;
        maxLineGap = 50;
        color = Color.MAGENTA;
    }

    public double getRho() {
        return rho;
    }

    public void setRho(double rho) {
        if (this.rho != rho) {
            this.rho = rho;
            fireChange();
        }
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        if (this.theta != theta) {
            this.theta = theta;
            fireChange();
        }
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        if (this.threshold != threshold) {
            this.threshold = threshold;
            fireChange();
        }
    }

    public double getMinLineLength() {
        return minLineLength;
    }

    public void setMinLineLength(double minLineLength) {
        if (this.minLineLength != minLineLength) {
            this.minLineLength = minLineLength;
            fireChange();
        }
    }

    public double getMaxLineGap() {
        return maxLineGap;
    }

    public void setMaxLineGap(double maxLineGap) {
        if (this.maxLineGap != maxLineGap) {
            this.maxLineGap = maxLineGap;
            fireChange();
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (this.color != color) {
            this.color = color;
            fireChange();
        }
    }

    @Override
    protected Mat transformOnly(Mat source) {
        Mat result = null;
        if (source != null) {
            log.info("Imgproc.HoughLinesP(source, result" +
                    ", " + rho +
                    ", " + theta +
                    ", " + threshold +
                    ", " + minLineLength +
                    ", " + maxLineGap +
                    ")");
            Mat lines = new Mat();
            // TODO compare Imgproc.HoughLines(source, result, threshold1, threshold2, apertureSize, l2gradient);
            Imgproc.HoughLinesP(source, lines,
                    rho,
                    theta,
                    threshold,
                    minLineLength,
                    maxLineGap);
            result = new Mat();
            Imgproc.cvtColor(source, result, Imgproc.COLOR_GRAY2BGR);
            for (int x = 0; x < lines.rows(); x++) {
                double[] l = lines.get(x, 0);
                Imgproc.line(result, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(color.getBlue(), color.getGreen(), color.getRed()), 3, Imgproc.LINE_AA, 0);
            }
        }
        return result;
    }
}
