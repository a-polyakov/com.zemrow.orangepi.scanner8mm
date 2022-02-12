package com.zemrow.scanner8mm.opencv;

import com.zemrow.scanner8mm.opencv.ui.ImageFrame;
import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO
 * https://docs.opencv.org/
 * https://www.tutorialspoint.com/opencv/opencv_quick_guide.htm
 * https://www.youtube.com/watch?v=2FYm3GOonhk
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class RunPostProcessing {
    public static void main(String[] args) throws IOException {

        OpenCV.loadShared();

        File sourceDir = new File("D:\\temp\\8mm\\001");
        File targetDir = new File("D:\\temp\\8mm\\002");
//        processing(new File("D:\\temp\\8mm\\001\\0005.jpg"), targetDir);
        for (File file : sourceDir.listFiles()) {
            try {
                processing(file, targetDir);
            } catch (Exception e) {
                System.err.println("Processing error " + file.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }

    private static void processing(File source, File targetDir) {
        String filename = source.getAbsolutePath();
        System.out.println("Processing " + filename);
        Mat sourceImage = Imgcodecs.imread(filename);
//        new ImageFrame(sourceImage, "sourceImage");

        boolean debug = false;

        // уменьшить в размере
        Mat smallImage = new Mat();
        Imgproc.resize(sourceImage, smallImage, new Size(), 0.6, 0.6);
        if (debug) {
            new ImageFrame(smallImage, "smallImage");
        }

//        Imgcodecs.imwrite(new File(targetDir, source.getName()).getAbsolutePath(), smallImage);
//        System.exit(0);

        // размытие
        Mat blur = new Mat();
        Imgproc.medianBlur(smallImage, blur, 33);
        if (debug) {
            new ImageFrame(blur, "blur");
        }

        // конвертация цветов
        Mat imgHSV = new Mat();
        Imgproc.cvtColor(blur, imgHSV, Imgproc.COLOR_HSV2BGR);
        if (debug) {
            new ImageFrame(imgHSV, "imgHSV");
        }

        // Поиск цвета
        Scalar lower = new Scalar(0, 221, 58);
        Scalar upper = new Scalar(121, 255, 255);
        Mat range = new Mat();
        Core.inRange(imgHSV, lower, upper, range);
        if (debug) {
            new ImageFrame(range, "range");
        }

        // создание контура https://www.tutorialspoint.com/opencv/opencv_canny_edge_detection.htm
//        Mat edges = new Mat();
//        Imgproc.Canny(range, edges, 40, 120);
//        new ImageFrame(edges, "edges");

        // обводка контура
//        Mat imgDil = new Mat();
//        Mat kernel2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
//        Imgproc.dilate(edges, imgDil, kernel2);
//        new ImageFrame(imgDil, "imgDil");

//         Поиск линий
//        Mat lines = new Mat();
//        Mat grey=new Mat();
//        Imgproc.cvtColor(blur, grey, Imgproc.COLOR_BGR2GRAY);
//        new ImageFrame(grey, "grey");
//        Mat edges=new Mat();
//        Imgproc.Canny(grey, edges, 60,60*3);
//        new ImageFrame(edges, "edges");
//        Imgproc.HoughLinesP(edges, lines, 1, Math.PI/2, 2, 30, 1);
//        new ImageFrame(lines, "lines");
////        Imgproc.line(img, pt1, pt2, (0,0,255), 3)
//        if (debug) return;

//
        // Поиск фигур
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(range, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        if (debug) {
            System.out.println("contours.size:" + contours.size());
            Mat drawContours = smallImage.clone();
            Imgproc.drawContours(drawContours, contours, -1, new Scalar(255, 0, 255));
            new ImageFrame(drawContours, "drawContours");
        }

        double minArea = 20000;
        double maxArea = 40000;
        List<MatOfPoint> need = new ArrayList<>();
        Point point1 = null;
        Point point2 = null;
        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
//            System.out.println("Area " + area);
            if (area > minArea && area < maxArea) {
                MatOfPoint2f contour2 = new MatOfPoint2f(contour.toArray());
                double arcLength = Imgproc.arcLength(contour2, true);
//                System.out.println("arcLength " + arcLength);
                MatOfPoint2f contour3 = new MatOfPoint2f();
                Imgproc.approxPolyDP(contour2, contour3, 0.04 * arcLength, true);
                if (debug) {
                    System.out.println("approxPolyDP " + contour3.size());
                    Imgproc.drawContours(smallImage, Collections.singletonList(new MatOfPoint(contour3.toArray())), -1, new Scalar(255, 0, 255));
                    new ImageFrame(smallImage, "approxPolyDP");
                }
                Point[] points = contour3.toArray();
                double x1 = points[0].x;
                double x2 = points[0].x;
                double y = 0;
                for (Point point : points) {
                    if (x1 < point.x) {
                        x2 = x1;
                        x1 = point.x;
                    }
                    y += point.y;
                }
                y /= points.length;
                if (point1 == null) {
                    point1 = new Point((x2 + x1) / 2, y);
                } else if (point2 == null) {
                    point2 = new Point((x2 + x1) / 2, y);
                } else {
                    Imgproc.circle(smallImage, new Point((x2 + x1) / 2, y), 5, new Scalar(255, 0, 255));
                    new ImageFrame(smallImage, "Лишняя точка");
                }
                need.add(new MatOfPoint(points));
            } else {
                System.out.println("Small area " + area);
            }
        }
        if (debug) {
            Imgproc.circle(smallImage, point1, 5, new Scalar(255, 0, 255));
            Imgproc.circle(smallImage, pointInLine(point1, point2, 0), 5, new Scalar(255, 0, 255));
            Imgproc.circle(smallImage, point2, 5, new Scalar(255, 0, 255));
            Imgproc.circle(smallImage, pointInLine(point1, point2, 0.57), 5, new Scalar(255, 0, 255));
            Imgproc.circle(smallImage, pointInLine(point1, point2, -0.445), 5, new Scalar(255, 0, 255));
            Imgproc.circle(smallImage, pointInParallelLine(point1, point2, 0.57, 1.36), 5, new Scalar(255, 0, 255));
            Imgproc.circle(smallImage, pointInParallelLine(point1, point2, -0.445, 1.36), 5, new Scalar(255, 0, 255));
            new ImageFrame(smallImage, "drawContours");
        }

        // Преобразование перспективы (Поворот, растягивание, обрезка)
        MatOfPoint2f findPoints = new MatOfPoint2f(
                pointInLine(point1, point2, -0.445), pointInParallelLine(point1, point2, -0.445, 1.36),
                pointInLine(point1, point2, 0.565), pointInParallelLine(point1, point2, 0.565, 1.36)
        );
//        for (Point point: source.toList()) {
//            Imgproc.circle(smallImage, point, 5, new Scalar(255,0,255));
//        }
//        new ImageFrame(smallImage, "draw frame");

        int w = 1024;
        int h = 768;
        MatOfPoint2f target = new MatOfPoint2f(
                new Point(0, 0), new Point(w, 0),
                new Point(0, h), new Point(w, h)
        );
        Mat matrix = Imgproc.getPerspectiveTransform(findPoints, target);
        Mat imgWrap = new Mat();
        Imgproc.warpPerspective(smallImage, imgWrap, matrix, new Size(w, h));
        if (debug) {
            new ImageFrame(imgWrap, "imgWrap");
        }

        Imgcodecs.imwrite(new File(targetDir, source.getName()).getAbsolutePath(), imgWrap);
    }

    private static Point pointInLine(Point point1, Point point2, double t) {
        final double l = point2.x - point1.x;
        final double m = point2.y - point1.y;
        final double x = l * t + point1.x;
        final double y = m * t + point1.y;
        return new Point(x, y);
    }

    private static Point pointInParallelLine(Point point1, Point point2, double t, double h) {
        final double l = point2.x - point1.x;
        final double m = point2.y - point1.y;
        final double x = -m * h + l * t + point1.x;
        final double y = l * h + m * t + point1.y;
        return new Point(x, y);
    }
}
