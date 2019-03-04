package com.kamerlin.leon.todoapp.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.kamerlin.leon.todoapp.R;
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

public class ReminderFragmentDialog extends DaggerDialogFragmentCancelable {
    public static final String TAG = ReminderFragmentDialog.class.getSimpleName();
    private static int numberOfCreation = 0;

    private static final String ARG_SELECTED_STRING = "arg_selected_string";
    private static final String ARG_SELECTED_LONG = "arg_selected_long";

    private final ReplaySubject<Pair<String, Long>> mStringReplaySubject;
    private String mSelected;
    private int mSelectedIndex;



    public static BiMap<String, Long> getData() {
        long one_second = 1000L;
        long one_minute = 60L * one_second;
        long one_hour = 60L * one_minute;

        BiMap<String, Long> data = HashBiMap.create();
        data.put("None", -1L);
        data.put("10 minute before", 10 * one_minute);
        data.put("15 minute before", 15 * one_minute);
        data.put("20 minute before", 20 * one_minute);
        data.put("30 minute before", 30 * one_minute);
        data.put("45 minute before", 45 * one_minute);
        data.put("1 hour before", one_hour);
        data.put("2 hour before", 2 * one_hour);
        data.put("3 hour before", 3 * one_hour);
        data.put("4 hour before", 4 * one_hour);
        data.put("6 hour before", 6 * one_hour);
        data.put("8 hour before", 8 * one_hour);
        data.put("10 hour before", 10 * one_hour);
        data.put("12 hour before", 12 * one_hour);



        return data;
    }

    public static String[] getArrayData() {
        return getData().inverse().values().toArray(new String[getData().inverse().values().size()]);
    }




    public static ReminderFragmentDialog newInstance() {
        Bundle args = new Bundle();
        ReminderFragmentDialog fragment = new ReminderFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReminderFragmentDialog newInstance(String selected) {

        Bundle args = new Bundle();
        args.putString(ARG_SELECTED_STRING, selected);
        ReminderFragmentDialog fragment = new ReminderFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static ReminderFragmentDialog newInstance(long selected) {

        Bundle args = new Bundle();
        args.putLong(ARG_SELECTED_LONG, selected);
        ReminderFragmentDialog fragment = new ReminderFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }



    @SuppressLint("CheckResult")
    public ReminderFragmentDialog() {
        mStringReplaySubject = ReplaySubject.create();
        mSelected = getData().inverse().get(-1L);
        mSelectedIndex = Arrays.asList(getArrayData()).indexOf(mSelected);

        getPositiveButtonClickObservable().subscribe(aBoolean -> {
            mStringReplaySubject.onNext(new Pair<>(mSelected, getData().get(mSelected)));
            getDialog().dismiss();
        });

        getNegativeButtonClickObservable().subscribe(aBoolean -> {
            getDialog().dismiss();
        });
    }


    public Observable<Pair<String, Long>> getSelectedObservable() {
        return mStringReplaySubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }




    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        Bundle bundle = getArguments();
        if (numberOfCreation == 0 && bundle != null) {
            if (bundle.containsKey(ARG_SELECTED_LONG)) {
                long selected = bundle.getLong(ARG_SELECTED_LONG, -1L);
                mSelected = getData().inverse().get(selected);
                mSelectedIndex = Arrays.asList(getArrayData()).indexOf(mSelected);
            } else if (bundle.containsKey(ARG_SELECTED_STRING)) {
                mSelected = bundle.getString(ARG_SELECTED_STRING, getArrayData()[0]);
                mSelectedIndex = Arrays.asList(getArrayData()).indexOf(mSelected);
            }
        }




        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_update_black_24dp);
        builder.setTitle("Remind me");

        builder.setPositiveButton("Ok", null);
        builder.setNegativeButton("Cancel", null);


        builder.setSingleChoiceItems(getArrayData(), mSelectedIndex, (dialog, which) -> {
            mSelected = getArrayData()[which];
            mSelectedIndex = which;
        });

        numberOfCreation++;
        return builder.create();
    }


    public void setSelectedReminder(String selected) {
        mSelected = selected;
        mSelectedIndex = Arrays.asList(getArrayData()).indexOf(selected);
    }
}
