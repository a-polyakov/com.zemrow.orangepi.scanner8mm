package com.zemrow.orangepi.scanner8mm.ui;

import com.zemrow.orangepi.scanner8mm.IpCam;
import com.zemrow.orangepi.scanner8mm.Perf;
import com.zemrow.orangepi.scanner8mm.StepDto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2021.09.06
 */
public class PreviewPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    private final IpCam ipCam;

    private BufferedImage preview;
    private Perf perf;

    private int mouseX, mouseY;

    public PreviewPanel(IpCam ipCam) {
        this.ipCam = ipCam;
        setMinimumSize(new Dimension(640, 480));
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getSize().width, getSize().height);
        if (preview != null) {
            g.drawImage(preview, 0, 0, null);
        }
        if (perf != null) {
            g.setColor(Color.RED);
            g.drawRect(perf.getX1(), perf.getY1(), perf.getX2() - perf.getX1(), perf.getY2() - perf.getY1());
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        updatePreview();
        perf = Perf.findPerf(preview);
        repaint();

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        final int x = e.getX();
        final int y = e.getY();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ipCam.setOffsetX(ipCam.getOffsetX() + (mouseX - x) / 6);
                ipCam.setOffsetY(ipCam.getOffsetY() + (mouseY - y) / 6);
                updatePreview();
                mouseX = x;
                mouseY = y;
            }
        });
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        perf = null;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ipCam.setZoom(ipCam.getZoom() + event.getWheelRotation());
                updatePreview();
            }
        });
    }

    public void updatePreview() {
        perf = null;
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new SwingWorker<BufferedImage, Void>() {
            @Override
            protected BufferedImage doInBackground() throws Exception {
                preview = ipCam.preview();
                return preview;
            }

            @Override
            protected void done() {
                repaint();
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }.execute();
    }

    public void updatePreview(StepDto step) {
        preview = step.getPreview();
        perf = step.getPerf();
        repaint();
    }
}
