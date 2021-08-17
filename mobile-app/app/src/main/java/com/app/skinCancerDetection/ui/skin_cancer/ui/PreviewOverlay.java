package com.app.skinCancerDetection.ui.skin_cancer.ui;

import android.content.Context;
import android.util.AttributeSet;


public class PreviewOverlay extends androidx.appcompat.widget.AppCompatImageView {

    public PreviewOverlay(Context context) {
        super(context);
    }
    public PreviewOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PreviewOverlay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        if (width != height) {
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
        }
    }
}