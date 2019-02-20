package com.kamerlin.leon.todoapp.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;

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

    private static final String ARG_SELECTED_INT = "arg_selected_int";
    private static final String ARG_SELECTED_STRING = "arg_selected_string";

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

    private final ReplaySubject<Pair<String, Integer>> mStringReplaySubject;

    private String mSelected;
    private int mSelectedIndex;

    public static PriorityFragmentDialog newInstance() {

        Bundle args = new Bundle();

        PriorityFragmentDialog fragment = new PriorityFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static PriorityFragmentDialog newInstance(String selected) {

        Bundle args = new Bundle();
        args.putString(ARG_SELECTED_STRING, selected);
        PriorityFragmentDialog fragment = new PriorityFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static PriorityFragmentDialog newInstance(int selected) {

        Bundle args = new Bundle();
        args.putInt(ARG_SELECTED_INT, selected);

        PriorityFragmentDialog fragment = new PriorityFragmentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("CheckResult")
    public PriorityFragmentDialog() {


        mStringReplaySubject = ReplaySubject.create();



        getPositiveButtonClickObservable().subscribe(aBoolean -> {
            mStringReplaySubject.onNext(new Pair<>(mSelected, getData().get(mSelected)));
            getDialog().dismiss();
        });

        getNegativeButtonClickObservable().subscribe(aBoolean -> {
            getDialog().dismiss();
        });
    }

    public Observable<Pair<String, Integer>> getSelectedObservable() {
        return mStringReplaySubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        mSelected = getArrayData()[0];
        mSelectedIndex = 0;



        Bundle bundle = getArguments();
        if (bundle != null) {

            if (bundle.containsKey(ARG_SELECTED_INT)) {

                int selected = bundle.getInt(ARG_SELECTED_INT, Task.PRIORITY_NONE);
                mSelected = getData().inverse().get(selected);
                mSelectedIndex = Arrays.asList(getArrayData()).indexOf(mSelected);

            } else if (bundle.containsKey(ARG_SELECTED_STRING)) {
                mSelected = bundle.getString(ARG_SELECTED_STRING, "None");
                mSelectedIndex = Arrays.asList(getArrayData()).indexOf(mSelected);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.ic_low_priority_black_24dp);
        builder.setTitle("Set priority");

        builder.setPositiveButton("Ok", null);
        builder.setNegativeButton("Cancel", null);

        builder.setSingleChoiceItems(getArrayData(), mSelectedIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelected = getArrayData()[which];
                mSelectedIndex = which;
                getDialog().invalidateOptionsMenu();
            }
        });


        return builder.create();
    }
}
