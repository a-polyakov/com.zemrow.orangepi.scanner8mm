package com.zemrow.orangepi.scanner8mm;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * TODO
 * Ip Webcam
 * https://play.google.com/store/apps/details?id=com.pas.webcam&hl=ru&gl=US
 *
 * @author Alexandr Polyakov on 2021.08.27
 */
public class IpCam {
    // TODO customization url
    public static final String URL_IP_WEBCAM = "http://192.168.1.67:8080/";
    private static final String URL_IMAGE = URL_IP_WEBCAM + "photo.jpg";
    private static final String URL_PREVIEW = URL_IP_WEBCAM + "shot.jpg?rnd=%d";
    public static final int PREVIEW_HEIGHT=480;
    private static final String URL_VIDEO_SIZE = URL_IP_WEBCAM + "settings/video_size?set=640x" +PREVIEW_HEIGHT;
    private static final String URL_PHOTO_SIZE = URL_IP_WEBCAM + "settings/photo_size?set=4000x3000";
    private static final String URL_WHITEBALANCE = URL_IP_WEBCAM + "settings/whitebalance?set=incandescent";
    private static final String URL_MANUAL_SENSOR = URL_IP_WEBCAM + "settings/manual_sensor?set=on";
    private static final String URL_ISO = URL_IP_WEBCAM + "settings/iso?set=200";
    private static final String URL_EXPOSURE_NS = URL_IP_WEBCAM + "settings/exposure_ns?set=450000";
    private static final String URL_FOCUSMODE = URL_IP_WEBCAM + "settings/focusmode?set=off";
    private static final String URL_FOCUS_DISTANCE = URL_IP_WEBCAM + "settings/focus_distance?set=5.5";

    private static final String URL_ZOOM = URL_IP_WEBCAM + "ptz?zoom=%d";
    private static final String URL_MOVE_X = URL_IP_WEBCAM + "settings/crop_x?set=%d";
    private static final String URL_MOVE_Y = URL_IP_WEBCAM + "settings/crop_y?set=%d";

    public static final String DEFAULT_OUT_DIR = "/home/image";

    private static final int OFFSET_X_MIN =0;
    private static final int OFFSET_X_MAX =100;
    private static final int OFFSET_Y_MIN =0;
    private static final int OFFSET_Y_MAX =100;

    private int zoom;
    private int offsetX;
    private int offsetY;

    private File outputDirectory;

    public IpCam() {
        zoom = 10;
        offsetX = 50;
        offsetY = 50;
        this.outputDirectory = new File(DEFAULT_OUT_DIR);
    }

    public synchronized void prepare() {
        try {
            sendGet(URL_VIDEO_SIZE);
            sendGet(URL_PHOTO_SIZE);
            sendGet(URL_WHITEBALANCE);
            sendGet(URL_MANUAL_SENSOR);
            sendGet(URL_ISO);
            sendGet(URL_EXPOSURE_NS);
            sendGet(URL_FOCUSMODE);
            sendGet(URL_FOCUS_DISTANCE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getZoom() {
        return zoom;
    }

    public synchronized void setZoom(int zoom) {
        try {
            sendGet(String.format(URL_ZOOM, zoom));
            this.zoom = zoom;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getOffsetX() {
        return offsetX;
    }

    public synchronized void setOffsetX(int offsetX) {
        if (this.offsetX != offsetX && offsetX >= OFFSET_X_MIN && offsetX <= OFFSET_X_MAX) {
            try {
                sendGet(String.format(URL_MOVE_X, offsetX));
                this.offsetX = offsetX;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getOffsetY() {
        return offsetY;
    }

    public synchronized void setOffsetY(int offsetY) {
        if (this.offsetY != offsetY && offsetY >= OFFSET_Y_MIN && offsetY <= OFFSET_Y_MAX) {
            try {
                sendGet(String.format(URL_MOVE_Y, offsetY));
                this.offsetY = offsetY;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized BufferedImage preview() throws IOException {
        return ImageIO.read(new URL(String.format(URL_PREVIEW, System.currentTimeMillis())).openStream());
    }

    public synchronized File tackImage(int i) throws IOException {
        final File file = new File(outputDirectory, String.format("%04d.jpg", i));
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(URL_IMAGE).openStream());
             FileOutputStream fileOS = new FileOutputStream(file)) {
            final byte buffer[] = new byte[4 * 1024];
            int byteContent;
            while ((byteContent = inputStream.read(buffer)) != -1) {
                fileOS.write(buffer, 0, byteContent);
            }
        }
        return file;
    }

    public void setOutputDirectory(String path) {
        outputDirectory = new File(path);
    }

    private int sendGet(String url) throws IOException {
        final HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("Ip Webcam request:" + url + " response:" + responseCode);
        return responseCode;
    }
}
