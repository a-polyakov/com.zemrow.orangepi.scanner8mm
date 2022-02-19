package com.zemrow.scanner8mm.opencv.transformation;

import com.zemrow.scanner8mm.opencv.transformation.bilateralFilter.TransformBilateralFilter;
import com.zemrow.scanner8mm.opencv.transformation.canny.TransformCanny;
import com.zemrow.scanner8mm.opencv.transformation.cvtcolor.TransformCvtColor;
import com.zemrow.scanner8mm.opencv.transformation.gaussianBlur.TransformGaussianBlur;
import com.zemrow.scanner8mm.opencv.transformation.houghLines.TransformHoughLines;
import com.zemrow.scanner8mm.opencv.transformation.inrange.TransformInRange;
import com.zemrow.scanner8mm.opencv.transformation.medianBlur.TransformMedianBlur;
import com.zemrow.scanner8mm.opencv.transformation.resize.TransformResize;

/**
 * TODO
 *
 * @author Alexandr Polyakov on 2022.02.12
 */
public enum TransformEnum {
    RESIZE {
        @Override
        public AbstractTransform newInstance() {
            return new TransformResize();
        }
    },
    CVT_COLOR {
        @Override
        public AbstractTransform newInstance() {
            return new TransformCvtColor();
        }
    },
    IN_RANGE {
        @Override
        public AbstractTransform newInstance() {
            return new TransformInRange();
        }
    },
    MEDIAN_BLUR {
        @Override
        public AbstractTransform newInstance() {
            return new TransformMedianBlur();
        }
    },
    BILATERAL_FILTER {
        @Override
        public AbstractTransform newInstance() {
            return new TransformBilateralFilter();
        }
    },
    GAUSSIAN_BLUR {
        @Override
        public AbstractTransform newInstance() {
            return new TransformGaussianBlur();
        }
    },
    CANNY {
        @Override
        public AbstractTransform newInstance() {
            return new TransformCanny();
        }
    },
    HOUGH_LINES {
        @Override
        public AbstractTransform newInstance() {
            return new TransformHoughLines();
        }
    };

    public abstract AbstractTransform newInstance();
}
