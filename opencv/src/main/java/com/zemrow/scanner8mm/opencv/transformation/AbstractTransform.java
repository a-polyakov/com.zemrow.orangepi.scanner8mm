package com.zemrow.scanner8mm.opencv.transformation;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public abstract class AbstractTransform {

    protected final Logger log = Logger.getLogger(getClass().getName());

    private final String title;

    private Mat mat;
    private ChangeListener changeListener;
    private ImageIcon image;

    public AbstractTransform(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public Mat getMat() {
        return mat;
    }

    public ImageIcon getImage() {
        return image;
    }

    public final Mat transform(Mat source) throws IOException {
        log.info("transform");
        long time = System.currentTimeMillis();
        mat = transformOnly(source);
        time = System.currentTimeMillis() - time;
        if (mat != null) {
            log.info("Render image " + time + "ms");
            final MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".jpg", mat, matOfByte);
            final byte[] byteArray = matOfByte.toArray();
            final InputStream in = new ByteArrayInputStream(byteArray);
            final BufferedImage bufImage = ImageIO.read(in);
            image = new ImageIcon(bufImage);
        } else {
            image = null;
        }
        return mat;
    }

    protected abstract Mat transformOnly(Mat source);

    protected void fireChange() {
        log.info("fireChange");
        if (changeListener != null) {
            changeListener.stateChanged(new ChangeEvent(this));
        }
    }
}
