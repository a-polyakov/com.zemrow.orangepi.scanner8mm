package com.zemrow.scanner8mm.opencv.ui;


import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class ImageFrame extends JFrame {

    private final JLabel image;

    public ImageFrame(Mat sourceImage, String title) {
        super(title);
        image = new JLabel();
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", sourceImage, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        InputStream in = new ByteArrayInputStream(byteArray);
        BufferedImage bufImage = null;
        try {
            bufImage = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.setIcon(new ImageIcon(bufImage));
        add(new JScrollPane(image), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1800, 1000);
        setVisible(true);
    }
}
