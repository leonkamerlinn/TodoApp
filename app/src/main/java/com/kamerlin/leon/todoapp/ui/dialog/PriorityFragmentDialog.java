package com.kamerlin.leon.todoapp.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.kamerlin.leon.todoapp.R;
import com.kamerlin.leon.todoapp.db.task.Task;
import com.kamerlin.leon.utils.data_structures.Pair;
import com.kamerlin.leon.utils.utils.DaggerDialogFragmentCancelable;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;

public class PriorityFragmentDialog extends DaggerDialogFragmentCancelable {
    public static final String TAG = PriorityFragmentDialog.class.getSimpleName();
    private final ReplaySubject<Pair<String, Integer>> mStringReplaySubject;
    private String mSelected;
    private int mSelectedIndex;
    private static int numberOfCreation = 0;
    private static final String ARG_PRIORITY_CODE = "arg_selected_int";
    private static final String ARG_PRIORITY = "arg_selected_string";

    public static BiMap<String, Integer> getData() {
        BiMap<String, Integer> data = HashBiMap.create();
        data.put("None", Task.PRIORITY_NONE);
        data.put("Low Priority", Task.PRIORITY_LOW);
        data.put("Medium Priority", Task.PRIORITY_MEDIUM);
        data.put("High Priority", Task.PRIORITY_HIGH);
        return data;
    }


    public static String[] getArrayData() {
        return getData().inverse().values().toArray(new String[getData().values().size()]);
    }



    public static PriorityFragmentDialog newInstance() {

        Bundle args = new Bundle();

        PriorityFragmentDialog fragment = new PriorityFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static PriorityFragmentDialog newInstance(String selected) {

        Bundle args = new Bundle();
        args.putString(ARG_PRIORITY, selected);
        PriorityFragmentDialog fragment = new PriorityFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static PriorityFragmentDialog newInstance(int selected) {

        Bundle args = new Bundle();
        args.putInt(ARG_PRIORITY_CODE, selected);

        PriorityFragmentDialog fragment = new PriorityFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("CheckResult")
    public PriorityFragmentDialog() {


        mStringReplaySubject = ReplaySubject.create();
        mSelectedIndex = 0;


        getPositiveButtonClickObservable().subscribe(aBoolean -> {
            mStringReplaySubject.onNext(new Pair<>(mSelected, getData().get(mSelected)));
            getDialog().dismiss();
        }, System.err::println);

        getNegativeButtonClickObservable().subscribe(aBoolean -> {
            getDialog().dismiss();
        }, System.err::println);
    }

    public Observable<Pair<String, Integer>> getSelectedObservable() {
        return mStringReplaySubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        Bundle bundle = getArguments();
        if (numberOfCreation == 0 && bundle != null) {
            if (bundle.containsKey(ARG_PRIORITY_CODE)) {

                int priorityCode = bundle.getInt(ARG_PRIORITY_CODE, Task.PRIORITY_NONE);
                mSelected = getData().inverse().get(priorityCode);
                mSelectedIndex = Arrays.asList(getArrayData()).indexOf(mSelected);

            } else if (bundle.containsKey(ARG_PRIORITY)) {
                mSelected = bundle.getString(ARG_PRIORITY, "None");
                mSelectedIndex = Arrays.asList(getArrayData()).indexOf(mSelected);
            }
        }

        mSelected = getArrayData()[mSelectedIndex];

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_low_priority_black_24dp);
        builder.setTitle("Set priority");

        builder.setPositiveButton("Ok", null);
        builder.setNegativeButton("Cancel", null);

        builder.setSingleChoiceItems(getArrayData(), mSelectedIndex, (dialog, which) -> {
            mSelected = getArrayData()[which];
            mSelectedIndex = which;
        });
        numberOfCreation++;

        return builder.create();
    }



    public void setSelectedPriority(String priority) {
        mSelected = priority;
        mSelectedIndex = Arrays.asList(getArrayData()).indexOf(priority);
    }
}
