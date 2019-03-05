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
    private static int numberOfCreation = 0;
    private static final String ARG_SELECTED_STRING = "arg_selected_string";
    public static final String STATE_DATA = "state_data";

    private final ReplaySubject<String> mStringReplaySubject;
    private Category[] mCategories;
    private String mSelected;
    private int mSelectedIndex;




    public static CategoryFragmentDialog newInstance() {

        CategoryFragmentDialog fragment = new CategoryFragmentDialog();

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
        mSelectedIndex = 0;
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

        if (savedInstanceState != null) {
            mCategories = (Category[]) savedInstanceState.getParcelableArray(STATE_DATA);
        }

        if (numberOfCreation == 0) {
            Bundle bundle = getArguments();
            if(bundle != null && bundle.containsKey(ARG_SELECTED_STRING)) {
                mSelected = getArguments().getString(ARG_SELECTED_STRING);
                mSelectedIndex = Arrays.asList(getCategoryNames()).indexOf(mSelected);
                if (mSelectedIndex == -1) {
                    mSelectedIndex = 0;
                    mSelected = getCategoryNames()[mSelectedIndex];
                }
            } else {
                mSelectedIndex = Arrays.asList(getCategoryNames()).indexOf("Personal");
                if (mSelectedIndex == -1) mSelectedIndex = 0;
                mSelected = getCategoryNames()[mSelectedIndex];
            }
        }

        mSelected = getCategoryNames()[mSelectedIndex];



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Set category");

        builder.setPositiveButton("Ok", null);
        builder.setNegativeButton("Cancel", null);



        builder.setSingleChoiceItems(getCategoryNames(), mSelectedIndex, (dialog, which) -> {
            mSelectedIndex = which;
            mSelected = getCategoryNames()[which];
        });



        numberOfCreation++;
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(STATE_DATA, mCategories);
    }

    public void setCategories(Category[] categories) {
        mCategories = categories;
    }

    public void setSelectedCategory(String categoryName) {
        mSelected = categoryName;
        mSelectedIndex = Arrays.asList(getCategoryNames()).indexOf(mSelected);
    }

    private String[] getCategoryNames() {
        return Stream.of(mCategories)
                .map(Category::getName)
                .toList()
                .toArray(new String[mCategories.length]);
    }


}
