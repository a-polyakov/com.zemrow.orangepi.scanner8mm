package com.zemrow.scanner8mm.opencv.ui;

import com.zemrow.scanner8mm.opencv.transformation.open.TransformOpenPanel;
import com.zemrow.scanner8mm.opencv.transformation.resize.TransformResizePanel;
import com.zemrow.scanner8mm.opencv.transformation.AbstractTransform;
import com.zemrow.scanner8mm.opencv.transformation.bilateralFilter.TransformBilateralFilter;
import com.zemrow.scanner8mm.opencv.transformation.bilateralFilter.TransformBilateralFilterPanel;
import com.zemrow.scanner8mm.opencv.transformation.canny.TransformCanny;
import com.zemrow.scanner8mm.opencv.transformation.canny.TransformCannyPanel;
import com.zemrow.scanner8mm.opencv.transformation.cvtcolor.TransformCvtColor;
import com.zemrow.scanner8mm.opencv.transformation.cvtcolor.TransformCvtColorPanel;
import com.zemrow.scanner8mm.opencv.transformation.gaussianBlur.TransformGaussianBlur;
import com.zemrow.scanner8mm.opencv.transformation.gaussianBlur.TransformGaussianBlurPanel;
import com.zemrow.scanner8mm.opencv.transformation.inrange.TransformInRange;
import com.zemrow.scanner8mm.opencv.transformation.inrange.TransformInRangePanel;
import com.zemrow.scanner8mm.opencv.transformation.medianBlur.TransformMedianBlur;
import com.zemrow.scanner8mm.opencv.transformation.medianBlur.TransformMedianBlurPanel;
import com.zemrow.scanner8mm.opencv.transformation.open.TransformOpen;
import com.zemrow.scanner8mm.opencv.transformation.resize.TransformResize;

import javax.swing.JScrollPane;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public class TransformEditPanel extends JScrollPane {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final Map<Class, AbstractTransformPanel> panels;

    public TransformEditPanel() {
        panels = new HashMap<>();
        panels.put(TransformOpen.class, new TransformOpenPanel());
        panels.put(TransformResize.class, new TransformResizePanel());
        panels.put(TransformCvtColor.class, new TransformCvtColorPanel());
        panels.put(TransformInRange.class, new TransformInRangePanel());
        panels.put(TransformMedianBlur.class, new TransformMedianBlurPanel());
        panels.put(TransformBilateralFilter.class, new TransformBilateralFilterPanel());
        panels.put(TransformGaussianBlur.class, new TransformGaussianBlurPanel());
        panels.put(TransformCanny.class, new TransformCannyPanel());
    }

    public void setTransform(AbstractTransform transform) {
        log.info("setTransform class:" + transform.getClass().getSimpleName());
        final AbstractTransformPanel panel = panels.get(transform.getClass());
        if (panel == null) {
            throw new RuntimeException("Unknown transformation " + transform.getClass().getSimpleName());
        }
        panel.setTransform(transform);
        setViewportView(panel);
    }
}
