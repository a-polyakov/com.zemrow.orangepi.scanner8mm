package com.zemrow.scanner8mm.opencv.ui;


import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

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
        image.setIcon(new ImageIcon(HighGui.toBufferedImage(sourceImage)));
        add(new JScrollPane(image), BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1800, 1000);
        setVisible(true);
    }
}
