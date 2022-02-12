package com.zemrow.scanner8mm.opencv.transformation.open;

import com.zemrow.scanner8mm.opencv.transformation.AbstractTransform;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Objects;

/**
 * TODO
 * @see <a href="https://www.tutorialspoint.com/opencv/opencv_reading_images.htm">Reading images</a>
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class TransformOpen extends AbstractTransform {

    private String file;

    public TransformOpen() {
        super("Open");
        // TODO
        // file="D:\\temp\\8mm\\001\\0005.jpg";
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        log.info("setFile file:" + file);
        if (!Objects.equals(this.file, file)) {
            this.file = file;
            fireChange();
        }
    }

    @Override
    protected Mat transformOnly(Mat source) {
        log.info("Imgcodecs.imread(" + file + ")");
        Mat result = null;
        if (file != null) {
            result = Imgcodecs.imread(file);
        }
        return result;
    }
}
