package com.kamerlin.leon.todoapp.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

public class Helper {
    public static void setBackgroundColorDrawable(@ColorRes int colorRes, Drawable drawable, Context context) {
        if (drawable instanceof ShapeDrawable) {
            ((ShapeDrawable)drawable).getPaint().setColor(ContextCompat.getColor(context, colorRes));
        } else if (drawable instanceof GradientDrawable) {
            ((GradientDrawable)drawable).setColor(ContextCompat.getColor(context, colorRes));
        } else if (drawable instanceof ColorDrawable) {
            ((ColorDrawable)drawable).setColor(ContextCompat.getColor(context, colorRes));
        }
    }

    public static void setBackgroundColorDrawable(@ColorRes int colorRes, View view) {
        Drawable drawable = view.getBackground();
        setBackgroundColorDrawable(colorRes, drawable, view.getContext());
    }
}
