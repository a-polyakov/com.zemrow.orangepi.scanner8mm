package com.zemrow.orangepi.scanner8mm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * TODO
 * Ip Webcam
 * https://play.google.com/store/apps/details?id=com.pas.webcam&hl=ru&gl=US
 *
 * @author Alexandr Polyakov on 2021.08.27
 */
public class IpCam {
    private static final String URL_IMAGE = "http://192.168.1.72:8080/photo.jpg";

    public File tackImage(int i) throws IOException {
        final File file = new File(String.format("image/%04d.jpg", i));
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(URL_IMAGE).openStream());
             FileOutputStream fileOS = new FileOutputStream(file)) {
            final byte data[] = new byte[4 * 1024];
            int byteContent;
            while ((byteContent = inputStream.read(data)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        }
        return file;
    }
}
