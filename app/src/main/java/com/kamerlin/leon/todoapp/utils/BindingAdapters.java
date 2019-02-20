package com.kamerlin.leon.todoapp.utils;

import android.graphics.PorterDuff;

import com.kamerlin.leon.utils.materialpallete.MaterialColor;
import com.kamerlin.leon.utils.materialpallete.MaterialColorFactory;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

public class BindingAdapters {
    @BindingAdapter("setError")
    public static void setEditTextError(AppCompatEditText editText, String errorMessage) {
        editText.setError(errorMessage);
    }

    @BindingAdapter("setMaterialColor")
    public static void setMaterialColor(AppCompatImageButton imageButton, String colorName) {
        if (colorName != null) {
            MaterialColor materialColor = MaterialColorFactory.getColor(colorName);
            if (materialColor != null) {
                int newColor = ContextCompat.getColor(imageButton.getContext(), materialColor.get500());
                imageButton.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);
            }
        }



    }
}
