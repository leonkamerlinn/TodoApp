package com.kamerlin.leon.todoapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kamerlin.leon.todoapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

public class ContentFragment extends Fragment {
    public static final String TAG = ContentFragment.class.getSimpleName();
    private static final String CATEGORY_ARG = "category_arg";
    private AppCompatTextView mTextView;

    public ContentFragment() {

    }

    public static ContentFragment newInstance(String category) {
        ContentFragment menuFragment = new ContentFragment();
        Bundle bundle =  new Bundle();
        bundle.putString(CATEGORY_ARG, category);
        menuFragment.setArguments(bundle);
        return menuFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        String category = args.getString(CATEGORY_ARG, "None");
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        mTextView = rootView.findViewById(R.id.text);

        mTextView.setText(category);

        return rootView;
    }

    public void setText(String text) {
        mTextView.setText(text);
    }
}
