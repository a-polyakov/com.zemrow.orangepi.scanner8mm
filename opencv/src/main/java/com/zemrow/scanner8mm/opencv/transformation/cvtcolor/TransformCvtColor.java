package com.zemrow.scanner8mm.opencv.transformation.cvtcolor;

import com.zemrow.scanner8mm.opencv.transformation.AbstractTransform;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class TransformCvtColor extends AbstractTransform {

    private CvtColorEnum code;

    public TransformCvtColor() {
        super("CvtColor");
        this.code = CvtColorEnum.BGR2GRAY;
    }

    public CvtColorEnum getCode() {
        return code;
    }

    public void setCode(CvtColorEnum code) {
        if (this.code != code) {
            this.code = code;
            fireChange();
        }
    }

    @Override
    protected Mat transformOnly(Mat source) {
        log.info("Imgproc.cvtColor(source, result, " + code + ")");
        Mat result = null;
        if (source != null) {
            result = new Mat();
            Imgproc.cvtColor(source, result, code.getCode());
        }
        return result;
    }
}
