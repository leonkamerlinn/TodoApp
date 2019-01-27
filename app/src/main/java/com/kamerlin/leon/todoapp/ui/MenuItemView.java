package com.kamerlin.leon.todoapp.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.kamerlin.leon.todoapp.R;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;


public class MenuItemView extends LinearLayoutCompat {
    private AppCompatTextView mTextView;
    private ConstraintLayout mItemLayout;
    private AppCompatImageView mIcon;
    private AppCompatImageView mCircle;

    public MenuItemView(Context context) {
        super(context);
        init();
    }

    public MenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.item_menu, this);
        mTextView = view.findViewById(R.id.text);
        mItemLayout = view.findViewById(R.id.item_layout);
        mIcon = view.findViewById(R.id.icon);
        mCircle = view.findViewById(R.id.circle);
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setBackgroundColor(int backgroundColor) {
        Drawable background = mItemLayout.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(backgroundColor);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(backgroundColor);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(backgroundColor);
        }
    }

    public void hideCircle(boolean hide) {
        mCircle.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
        invalidate();
    }

    public View getItemView() {
        return this;
    }

    public void setIcon(int resource) {
        mIcon.setBackgroundResource(resource);
    }


    public void setCircleColor(int resource) {
        Drawable background = mCircle.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable)background).getPaint().setColor(ContextCompat.getColor(getContext(), resource));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable)background).setColor(ContextCompat.getColor(getContext(), resource));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable)background).setColor(ContextCompat.getColor(getContext(), resource));
        }



    }
}
