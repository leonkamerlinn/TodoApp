package com.kamerlin.leon.todoapp.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import com.annimon.stream.Stream;
import com.kamerlin.leon.todoapp.db.category.Category;
import com.kamerlin.leon.utils.utils.DaggerDialogFragmentCancelable;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;

public class CategoryFragmentDialog extends DaggerDialogFragmentCancelable {
    public static final String TAG = CategoryFragmentDialog.class.getSimpleName();

    private static final String ARG_SELECTED_STRING = "arg_selected_string";

    private Category[] mCategories;


    private final ReplaySubject<String> mStringReplaySubject;

    private String mSelected;
    private int mSelectedIndex;

    public static CategoryFragmentDialog newInstance() {

        Bundle args = new Bundle();

        CategoryFragmentDialog fragment = new CategoryFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static CategoryFragmentDialog newInstance(String selected) {

        Bundle args = new Bundle();
        args.putString(ARG_SELECTED_STRING, selected);
        CategoryFragmentDialog fragment = new CategoryFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @SuppressLint("CheckResult")
    public CategoryFragmentDialog() {
        mStringReplaySubject = ReplaySubject.create();


        getPositiveButtonClickObservable().subscribe(aBoolean -> {
            mStringReplaySubject.onNext(mSelected);
            getDialog().dismiss();
        });

        getNegativeButtonClickObservable().subscribe(aBoolean -> {
            getDialog().dismiss();
        });


    }

    public Observable<String> getSelectedObservable() {
        return mStringReplaySubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    @SuppressLint("CheckResult")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {



        mSelected = getCategoryNames()[mSelectedIndex];
        mSelectedIndex = 0;
        if(getArguments().containsKey(ARG_SELECTED_STRING)) {
            mSelected = getArguments().getString(ARG_SELECTED_STRING);
            mSelectedIndex = Arrays.asList(getCategoryNames()).indexOf(mSelected);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Set category");

        builder.setPositiveButton("Ok", null);
        builder.setNegativeButton("Cancel", null);



        builder.setSingleChoiceItems(getCategoryNames(), mSelectedIndex, (dialog, which) -> {
            mSelectedIndex = which;
            mSelected = getCategoryNames()[which];
        });




        return builder.create();
    }

    public void setCategories(Category[] categories) {
        mCategories = categories;
    }


    private String[] getCategoryNames() {
        return Stream.of(mCategories)
                .map(Category::getName)
                .toList()
                .toArray(new String[mCategories.length]);
    }


}
