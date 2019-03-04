package com.kamerlin.leon.todoapp.ui.activity.task;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;

import com.kamerlin.leon.todoapp.R;
import com.kamerlin.leon.todoapp.databinding.ActivityTaskBinding;
import com.kamerlin.leon.todoapp.db.task.Task;
import com.kamerlin.leon.todoapp.ui.activity.main.MainActivity;
import com.kamerlin.leon.todoapp.ui.dialog.CategoryFragmentDialog;
import com.kamerlin.leon.todoapp.ui.dialog.PriorityFragmentDialog;
import com.kamerlin.leon.todoapp.ui.dialog.ReminderFragmentDialog;
import com.kamerlin.leon.utils.dialog.DatePickerFragmentDialog;
import com.kamerlin.leon.utils.dialog.TimePickerFragmentDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;


public class TaskActivity extends DaggerAppCompatActivity implements TaskContract.View, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    @Inject
    TaskViewModel viewModel;

    @Inject
    ActivityTaskBinding binding;

    @Inject
    TaskContract.Model model;
    private Task mTask;
    private TimePickerDialog mTimePickerDialog;
    private DatePickerDialog mDatePickerDialog;


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        if (!setupActionBar()) return;


        if (savedInstanceState == null) {

            if (getIntent().hasExtra(MainActivity.EXTRA_TASK)) {
                mTask = getIntent().getParcelableExtra(MainActivity.EXTRA_TASK);
                viewModel.setTask(mTask);


            }
        }

        mTimePickerDialog = (TimePickerDialog) getTimePickerDialog();
        mTimePickerDialog.setInitialSelection(new Timepoint(model.getHour(), model.getMinute(), model.getSeconds()));

        mDatePickerDialog = (DatePickerDialog) getDatePickerDialog();
        mDatePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);



        ReminderFragmentDialog reminderDialog = getReminderDialog(mTask);
        PriorityFragmentDialog priorityDialog = getPriorityDialog(mTask);
        CategoryFragmentDialog categoryDialog = getCategoryDialog(mTask);


        viewModel.getReminder().observe(this, reminderDialog::setSelectedReminder);
        viewModel.getPriority().observe(this, priorityDialog::setSelectedPriority);
        viewModel.getCategories().observe(this, categoryDialog::setCategories);
        viewModel.getCategory().observe(this, categoryDialog::setSelectedCategory);

        reminderDialog.getSelectedObservable().subscribe(viewModel::setReminderValue);
        priorityDialog.getSelectedObservable().subscribe(viewModel::setPriorityValue);
        categoryDialog.getSelectedObservable().subscribe(viewModel::setCategoryByNameAsync);



        binding.reminderLinearLayout.setOnClickListener(v -> {
            reminderDialog.show(getFragmentManager(), ReminderFragmentDialog.TAG);
        });


        binding.priorityLinearLayout.setOnClickListener(v -> {
            priorityDialog.show(getFragmentManager(), PriorityFragmentDialog.TAG);
        });

        binding.categoryLinearLayout.setOnClickListener(v -> {
            categoryDialog.show(getFragmentManager(), CategoryFragmentDialog.TAG);
        });


        binding.dueDateLinearLayout.setOnClickListener(v -> {
            showDatePickerDialog();
        });

        binding.dueDatetime.setOnClickListener(v -> {
            onShowTimePickerDialog();
        });
        model.onCreate();
    }



    private boolean setupActionBar() {
        if (getSupportActionBar() == null) return false;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        return true;
    }

    private void showDatePickerDialog() {
        mDatePickerDialog.show(getSupportFragmentManager(), DatePickerFragmentDialog.TAG);
    }

    private void showTimePickerDialog() {
        mTimePickerDialog.show(getSupportFragmentManager(), TimePickerFragmentDialog.TAG);
    }

    private CategoryFragmentDialog getCategoryDialog(Task task) {
        CategoryFragmentDialog dialog = (CategoryFragmentDialog) getFragmentManager().findFragmentByTag(CategoryFragmentDialog.TAG);
        if(dialog != null) return dialog;
        if (task == null) {
            dialog = CategoryFragmentDialog.newInstance();
        } else {
            dialog = CategoryFragmentDialog.newInstance(task.getCategoryName());
        }

        return dialog;
    }

    private PriorityFragmentDialog getPriorityDialog(Task task) {
        PriorityFragmentDialog dialog = (PriorityFragmentDialog) getFragmentManager().findFragmentByTag(PriorityFragmentDialog.TAG);
        if(dialog != null) return dialog;
        if (task == null) {
            dialog = PriorityFragmentDialog.newInstance();
        } else {
            dialog = PriorityFragmentDialog.newInstance(task.getPriorityCode());
        }

        return dialog;
    }

    private ReminderFragmentDialog getReminderDialog(Task task) {
        ReminderFragmentDialog dialog = (ReminderFragmentDialog) getFragmentManager().findFragmentByTag(ReminderFragmentDialog.TAG);
        if(dialog != null) return dialog;
        if (task == null) {
            dialog = ReminderFragmentDialog.newInstance();
        } else {
            dialog = ReminderFragmentDialog.newInstance(task.getRemindDiff());
        }

        return dialog;
    }

    private Object getTimePickerDialog() {
        Fragment dialog = getFragmentManager().findFragmentByTag(TimePickerFragmentDialog .TAG);

        if (dialog != null) return dialog;
        return TimePickerFragmentDialog.Material(this);
    }

    private Object getDatePickerDialog() {
        Fragment dialog = getFragmentManager().findFragmentByTag(DatePickerFragmentDialog .TAG);

        if (dialog != null) return dialog;
        return DatePickerFragmentDialog.Material(this);
    }




    @Override
    public void onShowTimePickerDialog() {
        showTimePickerDialog();
    }

    @Override
    public void onCloseActivity() {
        finish();
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        model.onDateSet(year, monthOfYear, dayOfMonth);
        showTimePickerDialog();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        model.onTimeSet(hourOfDay, minute, second);
    }
}
