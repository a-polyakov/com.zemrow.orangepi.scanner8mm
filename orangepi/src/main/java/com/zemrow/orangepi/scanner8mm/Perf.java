package com.zemrow.orangepi.scanner8mm;

import sun.awt.image.ByteInterleavedRaster;

import java.awt.image.BufferedImage;

/**
 * TODO
 * https://en.wikipedia.org/wiki/Film_perforations
 *
 * @author Alexandr Polyakov on 2021.08.27
 */
public class Perf {

    private static final int minPerfHeight = 20;
    private static final int minPerfWidth = 15;
    private static final float bestRate=(float) minPerfHeight/minPerfWidth;

    private static final int minX = 50;
    private static final int maxX = 250;
    private static final int minY = IpCam.PREVIEW_HEIGHT / 4;
    private static final int maxY = IpCam.PREVIEW_HEIGHT - minY;

    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private float e;

    public Perf(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        if (x2 - x1 > 0) {
            this.e = bestRate - ((float) (y2 - y1))/ (x2 - x1);
            this.e *= this.e;
        } else {
            this.e = 999;
        }
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    private float getE() {
        return e;
    }

    public static Perf findPerf(BufferedImage preview) {
        Perf bestPerf = null;
        if (preview != null) {
            final int height = preview.getHeight();
            final int width = preview.getWidth();
            for (int y = minY; y < height && y < maxY; y++) {
                for (int x = minX; x < width && x < maxX; x++) {
                    if (isWhite(preview, x, y)) {
                        final Perf perf = findPerf(preview, x, y);
                        if (bestPerf == null) {
                            bestPerf = perf;
                        } else {
                            if (perf != null) {
                                float e = perf.getE();
                                float bestE = bestPerf.getE();
                                if (e < bestE) {
                                    bestPerf = perf;
                                }
                            }
                        }
                    }
                }
            }
        }
        return bestPerf;
    }

    private static Perf findPerf(BufferedImage preview, int x1, int y1) {
        int x2 = x1;
        int y2 = y1;
        int dx = 1;
        int dy = 1;
        while ((dx > 0 || dy > 0) && x2 < preview.getWidth() - dx && x2 < maxX && y2 < preview.getHeight() - dy && y2 < maxY) {
            if (dx > 0 && isWhite(preview, x2 + dx, y2)) {
                x2 += dx;
            } else {
                dx = 0;
            }
            if (dy > 0 && isWhite(preview, x2, y2 + dy)) {
                y2 += dy;
            } else {
                dy = 0;
            }
        }
        if (y2 - y1 > minPerfHeight && x2 - x1 > minPerfWidth) {
            return new Perf(x1, y1, x2, y2);
        } else {
            return null;
        }
    }

    private static boolean isWhite(BufferedImage preview, int x, int y) {
        final byte[] data = ((ByteInterleavedRaster) preview.getRaster()).getDataStorage();
        final int index = (y * preview.getWidth() + x) * 3;
        final int red = data[index + 2] & 0xFF;
        final int blue = data[index] & 0xFF;
        final int green = data[index + 1] & 0xFF;
        return red >= 220 && green >= 220 && blue >= 220;
    }
}
