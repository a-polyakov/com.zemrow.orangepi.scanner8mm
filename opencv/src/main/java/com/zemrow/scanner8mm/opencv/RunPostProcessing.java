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
import java.util.List;
import java.util.stream.Stream;

/**
 * TODO
 * https://docs.opencv.org/
 * https://www.tutorialspoint.com/opencv/opencv_quick_guide.htm
 * https://www.youtube.com/watch?v=2FYm3GOonhk
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class RunPostProcessing {

    public static final double SCALE = 0.6;
    public static final double FRAME_WIDTH_PERCENT = 1.36;
    public static final double FRAME_HEIGHT_TOP_PERCENT = 0.57;
    public static final double FRAME_HEIGHT_BUTTON_PERCENT = -0.45;
    public static final int FRAME_WIDTH = 1024;
    public static final int FRAME_HEIGHT = 768;

    public static void main(String[] args) throws IOException {

        OpenCV.loadShared();

        final File sourceDir = new File("D:\\temp\\8mm\\001");
        final File targetDir = new File("D:\\temp\\8mm\\002");
        final File errorDir = new File("D:\\temp\\8mm\\error");

//        processing(new File("D:\\temp\\8mm\\001\\0005.jpg"), targetDir);

        Stream.of(sourceDir.listFiles()).parallel().forEach(file -> {
            try {
                processing(file, targetDir, errorDir);
            } catch (Exception e) {
                System.err.println("Processing error " + file.getAbsolutePath());
                e.printStackTrace();
            }
        });

//        for (File file : sourceDir.listFiles()) {
//            try {
//                processing(file, targetDir);
//            } catch (Exception e) {
//                System.err.println("Processing error " + file.getAbsolutePath());
//                e.printStackTrace();
//            }
//        }
    }

    private static void processing(File source, File targetDir, File errorDir) {
        final String filename = source.getAbsolutePath();
        System.out.println("Processing " + filename);
        final Mat sourceImage = Imgcodecs.imread(filename);
        boolean debug = false;
        if (debug) {
            new ImageFrame(sourceImage, "sourceImage");
        }

        // уменьшить в размере
        final Mat smallImage = new Mat();
        Imgproc.resize(sourceImage, smallImage, new Size(), SCALE, SCALE);
        sourceImage.release();
        if (debug) {
            new ImageFrame(smallImage, "smallImage");
        }

        // размытие
        final Mat blur = new Mat();
        Imgproc.medianBlur(smallImage, blur, 33);
        if (debug) {
            new ImageFrame(blur, "blur");
        }

        // конвертация цветов
        final Mat imgHSV = new Mat();
        Imgproc.cvtColor(blur, imgHSV, Imgproc.COLOR_HSV2BGR);
        blur.release();
        if (debug) {
            new ImageFrame(imgHSV, "imgHSV");
        }

        // Поиск цвета
        final Scalar lower = new Scalar(0, 221, 58);
        final Scalar upper = new Scalar(121, 255, 255);
        final Mat range = new Mat();
        Core.inRange(imgHSV, lower, upper, range);
        imgHSV.release();
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
//        Mat grey=new Mat();
//        Imgproc.cvtColor(blur, grey, Imgproc.COLOR_BGR2GRAY);
//        new ImageFrame(grey, "grey");
//        Mat edges=new Mat();
//        Imgproc.Canny(grey, edges, 20.0, 50, 3, true);
//        new ImageFrame(edges, "edges");
//        Mat lines = new Mat();
//        Imgproc.HoughLinesP(edges, lines, 1.0, 0.01, 30, 400.0, 25.0);
//        Mat drawLine=grey.clone();
//        for (int x = 0; x < lines.rows(); x++) {
//            double[] l = lines.get(x, 0);
//            Imgproc.line(drawLine, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
//        }
//        new ImageFrame(drawLine, "drawLine");
//
//
//        if (debug) return;

//
        // Поиск фигур
        final List<MatOfPoint> contours = new ArrayList<>();
        final Mat hierarchy = new Mat();
        Imgproc.findContours(range, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        range.release();
        hierarchy.release();
        if (debug) {
            System.out.println("contours.size:" + contours.size());
            final Mat drawContours = smallImage.clone();
            Imgproc.drawContours(drawContours, contours, -1, new Scalar(255, 0, 255));
            new ImageFrame(drawContours, "drawContours");
            drawContours.release();
        }

        // поиск перфорации
        final List<MatOfPoint> need = new ArrayList<>();
        Point point1 = null;
        Point point2 = null;
        for (MatOfPoint contour : contours) {
//            double area = Imgproc.contourArea(contour);
//            System.out.println("Area " + area);
            final Point[] points = contour.toArray();
            if (isPerf(points)) {

//                double arcLength = Imgproc.arcLength(contour2, true);
//                System.out.println("arcLength " + arcLength);
//                MatOfPoint2f contour3 = new MatOfPoint2f();
//                Imgproc.approxPolyDP(contour2, contour3, 0.04 * arcLength, true);
//                if (debug) {
//                    System.out.println("approxPolyDP " + contour3.size());
//                    Imgproc.drawContours(smallImage, Collections.singletonList(new MatOfPoint(contour3.toArray())), -1, new Scalar(255, 0, 255));
//                    new ImageFrame(smallImage, "approxPolyDP");
//                }
//                Point[] points = contour3.toArray();
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
//                    new ImageFrame(smallImage, "Лишняя точка");
                }
                need.add(contour);
            } else {
                contour.release();
                //System.out.println("Small area " + area);
            }
        }
        if (need.size() != 2) {
            Imgproc.drawContours(smallImage, need, -1, new Scalar(255, 0, 255));
            for (MatOfPoint contour:need){
                contour.release();
            }
            if (debug) {
                 new ImageFrame(smallImage, filename+", need contours size != 2");
            }
            Imgcodecs.imwrite(new File(errorDir, source.getName()).getAbsolutePath(), smallImage);
            smallImage.release();
            return;
        }

        if (debug) {
            Imgproc.circle(smallImage, point1, 5, new Scalar(255, 0, 255));
            Imgproc.circle(smallImage, pointInLine(point1, point2, 0), 5, new Scalar(255, 0, 255));
            Imgproc.circle(smallImage, point2, 5, new Scalar(255, 0, 255));
            Imgproc.circle(smallImage, pointInLine(point1, point2, FRAME_HEIGHT_TOP_PERCENT), 5, new Scalar(255, 0, 255));
            Imgproc.circle(smallImage, pointInLine(point1, point2, FRAME_HEIGHT_BUTTON_PERCENT), 5, new Scalar(255, 0, 255));
            Imgproc.circle(smallImage, pointInParallelLine(point1, point2, FRAME_HEIGHT_TOP_PERCENT, FRAME_WIDTH_PERCENT), 5, new Scalar(255, 0, 255));
            Imgproc.circle(smallImage, pointInParallelLine(point1, point2, FRAME_HEIGHT_BUTTON_PERCENT, FRAME_WIDTH_PERCENT), 5, new Scalar(255, 0, 255));
            new ImageFrame(smallImage, "draw frame");
        }

        // Преобразование перспективы (Поворот, растягивание, обрезка)
        final MatOfPoint2f findPoints = new MatOfPoint2f(
                pointInLine(point1, point2, FRAME_HEIGHT_BUTTON_PERCENT), pointInParallelLine(point1, point2, FRAME_HEIGHT_BUTTON_PERCENT, FRAME_WIDTH_PERCENT),
                pointInLine(point1, point2, FRAME_HEIGHT_TOP_PERCENT), pointInParallelLine(point1, point2, FRAME_HEIGHT_TOP_PERCENT, FRAME_WIDTH_PERCENT)
        );
        final MatOfPoint2f target = new MatOfPoint2f(
                new Point(0, 0), new Point(FRAME_WIDTH, 0),
                new Point(0, FRAME_HEIGHT), new Point(FRAME_WIDTH, FRAME_HEIGHT)
        );
        final Mat matrix = Imgproc.getPerspectiveTransform(findPoints, target);
        findPoints.release();
        target.release();
        final Mat imgWrap = new Mat();
        Imgproc.warpPerspective(smallImage, imgWrap, matrix, new Size(FRAME_WIDTH, FRAME_HEIGHT));
        smallImage.release();
        matrix.release();
        if (debug) {
            new ImageFrame(imgWrap, "imgWrap");
        }

        Imgcodecs.imwrite(new File(targetDir, source.getName()).getAbsolutePath(), imgWrap);
        imgWrap.release();
    }

    public static double perfMinX = 200 * SCALE;
    public static double perfMaxX = 1100 * SCALE;
    public static double perfMinHeight = 200 * SCALE;
    public static double perfMaxHeight = 500 * SCALE;

    /**
     * перфорации
     *
     * @param points
     * @return
     */
    public static boolean isPerf(Point[] points) {
        double x1 = points[0].x;
        double x2 = x1;
        double y1 = points[0].y;
        double y2 = y1;
        for (Point point : points) {
            if (x1 > point.x) {
                x1 = point.x;
            } else if (x2 < point.x) {
                x2 = point.x;
            }

            if (y1 > point.y) {
                y1 = point.y;
            } else if (y2 < point.y) {
                y2 = point.y;
            }
        }
        final double height = y2 - y1;
        boolean result = (x1 >= perfMinX) && (x2 <= perfMaxX) && (height >= perfMinHeight) && (height <= perfMaxHeight);
        return result;
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
