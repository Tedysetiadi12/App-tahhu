package com.tahhu.id;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SlideToCancel extends FrameLayout {
    private View slideButton;
    private float initialX;
    private float buttonWidth;
    private boolean isSliding = false;
    private OnSlideCompleteListener listener;
    public interface OnSlideCompleteListener {
        void onSlideComplete();
    }

    public SlideToCancel(Context context) {
        super(context);
        init();
    }

    public SlideToCancel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.slide_to_cancel_layout, this);
        slideButton = findViewById(R.id.slideButton);

        post(() -> buttonWidth = slideButton.getWidth());

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isClickOnButton(event.getX(), event.getY())) {
                            isSliding = true;
                            initialX = event.getX();
                            return true;
                        }
                        return false;

                    case MotionEvent.ACTION_MOVE:
                        if (isSliding) {
                            float delta = event.getX() - initialX;
                            float newX = slideButton.getX() + delta;

                            // Limit sliding within the view bounds
                            newX = Math.max(0, Math.min(newX, getWidth() - buttonWidth));

                            slideButton.setX(newX);
                            initialX = event.getX();

                            // Check if slide is complete
                            if (newX >= getWidth() - buttonWidth - 50) {
                                if (listener != null) {
                                    listener.onSlideComplete();
                                }
                                return false;
                            }
                            return true;
                        }
                        return false;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (isSliding) {
                            isSliding = false;
                            // Animate button back to start
                            slideButton.animate()
                                    .x(0)
                                    .setDuration(200)
                                    .start();
                            return true;
                        }
                        return false;
                }
                return false;
            }
        });
    }

    private boolean isClickOnButton(float x, float y) {
        return x >= slideButton.getX() &&
                x <= slideButton.getX() + buttonWidth &&
                y >= slideButton.getY() &&
                y <= slideButton.getY() + slideButton.getHeight();
    }

    public void setOnSlideCompleteListener(OnSlideCompleteListener listener) {
        this.listener = listener;
    }
}