package com.example.alertmeapp.utils;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

public class FactoryAnimation {

    public static Animation createButtonTouchedAnimation() {
        Animation fadeIn = new AlphaAnimation(0.7f, 1f);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setStartOffset(0);
        fadeIn.setDuration(650);

        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeIn);
        animation.setFillAfter(true);
        return animation;
    }
}
