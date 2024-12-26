package com.tahhu.id;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class DriverRideSharingSearch extends AppCompatActivity {
    private FrameLayout rippleContainer;
    private List<View> rippleViews;
    private boolean isSearching = true;
    private static final int RIPPLE_COUNT = 3;
    private static final int RIPPLE_DURATION = 3000;
    private static final int RIPPLE_START_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_ride_sharing_search);

        rippleContainer = findViewById(R.id.rippleContainer);
        rippleViews = new ArrayList<>();
        // Initialize back button
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
        // Start ripple animation
        setupRippleAnimation();

        SlideToCancel slideToCancel = findViewById(R.id.slideToCancel);
        slideToCancel.setOnSlideCompleteListener(new SlideToCancel.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete() {
                // Stop searching
                isSearching = false;

                // Navigate to new activity
                Intent intent = new Intent(DriverRideSharingSearch.this, CancelOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Delay to auto-navigate after a certain time
        long delayMillis = 5000;
        new android.os.Handler().postDelayed(() -> {
            if (isSearching) {
                isSearching = false;

                // Navigate to another activity
                Intent intent = new Intent(DriverRideSharingSearch.this, DriverRideSharingSearchArrived.class);
                startActivity(intent);
                finish();
            }
        }, delayMillis);
    }

    private void setupRippleAnimation() {
        // Create ripple views
        for (int i = 0; i < RIPPLE_COUNT; i++) {
            View rippleView = new View(this);
            rippleView.setBackground(createRippleDrawable());

            // Set initial size and position
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    dpToPx(64), // Same size as driver image
                    dpToPx(64)
            );
            params.gravity = android.view.Gravity.CENTER;
            rippleView.setLayoutParams(params);

            rippleContainer.addView(rippleView);
            rippleViews.add(rippleView);

            startRippleAnimation(rippleView, i * RIPPLE_START_DELAY);
        }
    }

    private GradientDrawable createRippleDrawable() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(0x3000FF00);
        return drawable;
    }

    private void startRippleAnimation(View rippleView, long startDelay) {
        AnimatorSet animatorSet = new AnimatorSet();

        // Scale animation
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(rippleView, "scaleX", 1f, 8f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(rippleView, "scaleY", 1f, 8f);

        // Alpha animation
        ObjectAnimator alpha = ObjectAnimator.ofFloat(rippleView, "alpha", 0.5f, 0f);

        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        alpha.setRepeatCount(ValueAnimator.INFINITE);

        animatorSet.playTogether(scaleX, scaleY, alpha);
        animatorSet.setDuration(RIPPLE_DURATION);
        animatorSet.setStartDelay(startDelay);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isSearching) {
                    animatorSet.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        animatorSet.start();
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isSearching = false;
    }
}