package com.kamerlin.leon.todoapp.ui.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.kamerlin.leon.todoapp.R;

public class NavigationFooterView extends LinearLayoutCompat {
    private NavigationFooterListener mNavigationFooterListener;

    public NavigationFooterView(Context context) {
        super(context);
        init(context, null);
    }

    public NavigationFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NavigationFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.navigation_footer, this);
        LinearLayoutCompat newCategoryLayout = view.findViewById(R.id.new_category_layout);


        Activity activity = (Activity) getContext();
        if (activity instanceof NavigationFooterListener) {
            mNavigationFooterListener = (NavigationFooterListener)activity;

            newCategoryLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNavigationFooterListener.onNewCategoryClick(v);
                }
            });



        }


    }


    public interface NavigationFooterListener {
        void onNewCategoryClick(View v);
        void onSettingsClick(View v);
    }
}
