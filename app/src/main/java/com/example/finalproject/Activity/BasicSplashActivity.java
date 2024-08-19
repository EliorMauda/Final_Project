package com.example.finalproject.Activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalproject.R;
import com.example.finalproject.databinding.ActivityBasicSplashBinding;
import com.example.finalproject.databinding.ActivityCartBinding;

public class BasicSplashActivity extends AppCompatActivity {
    ActivityBasicSplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBasicSplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startAnimation(binding.cardView);
        startTextAnimation(binding.welcomeText);
    }

    private void startTextAnimation(View textView) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        textView.setY(height); // Start position: below the screen
        textView.setAlpha(0.0f); // Start with no visibility

        textView.animate()
                .translationY(0) // Move to final position
                .alpha(1.0f) // Fade in to full visibility
                .setDuration(4000) // Duration of the animation
                .start();
    }

    private void startAnimation(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        view.setY(-height / 2.0f);
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
        view.setAlpha(0.0f);

        view.animate()
                .alpha(1.0f)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .translationY(0)
                .setDuration(4000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animation) {
                        //pass
                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animation) {
                        transactToIntroActivity();
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animation) {
                        //pass
                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animation) {
                        //pass
                    }
                });


    }

    private void transactToIntroActivity() {
        startActivity(new Intent(this,IntroActivity.class));
        finish();
    }
}