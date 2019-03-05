package com.kamerlin.leon.todoapp.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.kamerlin.leon.todoapp.R;
import com.kamerlin.leon.todoapp.db.category.Category;
import com.kamerlin.leon.todoapp.ui.MenuItemView;
import com.kamerlin.leon.utils.materialpallete.MaterialColor;
import com.kamerlin.leon.utils.materialpallete.MaterialColorFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuAdapter extends ArrayAdapter<Category> {
    private final String mDefaultSelected;
    private ItemListener mItemListener;
    private List<Category> mCategories;

    public MenuAdapter(@NonNull Context context, String selected) {
        super(context, 0);
        mCategories = new ArrayList<>();
        mDefaultSelected = selected;
        Activity activity = (Activity)context;
        if (activity instanceof ItemListener) {
            mItemListener = (ItemListener)activity;
        }
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



            MenuItemView menuItem = new MenuItemView(getContext());
            convertView = menuItem;
            Category category = mCategories.get(position);
            menuItem.setText(category.getName());
            menuItem.setIcon(R.drawable.icon_menulist_grey);

            MaterialColor materialColor = MaterialColorFactory.getColor(category.getColorName());
            if (materialColor != null) {
                menuItem.setCircleColor(materialColor.get500());
            }


            if (mItemListener != null) {
                convertView.setOnClickListener(v -> mItemListener.onItemClick(v, position, category));
            }


        return convertView;
    }

    public Category getCategory(int index) {
        return mCategories.get(index);
    }


    public void setCategories(List<Category> categories) {
        mCategories.clear();
        mCategories.addAll(categories);
        notifyDataSetInvalidated();
    }


    @Override
    public int getCount() {
        return (mCategories == null) ? 0 : mCategories.size();
    }

    public interface ItemListener {
        void onItemClick(View itemView, int position, Category category);
    }


}
