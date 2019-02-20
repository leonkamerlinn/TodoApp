package com.kamerlin.leon.todoapp.ui.activity.task;


import android.annotation.SuppressLint;
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

import javax.inject.Inject;

import androidx.lifecycle.Observer;
import dagger.android.support.DaggerAppCompatActivity;


public class TaskActivity extends DaggerAppCompatActivity implements TaskContract.View {
    @Inject
    TaskViewModel viewModel;

    @Inject
    ActivityTaskBinding binding;

    @Inject
    TaskContract.Model model;


    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        if (!setupActionBar()) return;

        if (getIntent().hasExtra(MainActivity.EXTRA_CATEGORY_NAME)) {
            String categoryName = getIntent().getStringExtra(MainActivity.EXTRA_CATEGORY_NAME);
            viewModel.setCategoryByName(categoryName);
        }


        ReminderFragmentDialog reminderDialog;
        PriorityFragmentDialog priorityDialog;
        CategoryFragmentDialog categoryDialog;


        if (getIntent().hasExtra(MainActivity.EXTRA_TASK)) {

            Task task = getIntent().getParcelableExtra(MainActivity.EXTRA_TASK);
            viewModel.setTask(task);

            reminderDialog = ReminderFragmentDialog.newInstance(task.getRemindMe());
            priorityDialog = PriorityFragmentDialog.newInstance(task.getPriorityCode());
            categoryDialog = CategoryFragmentDialog.newInstance(task.getCategoryName());

        } else {
            reminderDialog = ReminderFragmentDialog.newInstance();
            priorityDialog = PriorityFragmentDialog.newInstance();
            categoryDialog = CategoryFragmentDialog.newInstance();
        }

        viewModel.getCategories().observe(this, categoryDialog::setCategories);

        categoryDialog.getSelectedObservable()








        reminderDialog.getSelectedObservable().subscribe(viewModel::setReminderValue);
        binding.reminderLinearLayout.setOnClickListener(v -> {
            reminderDialog.show(getSupportFragmentManager(), ReminderFragmentDialog.TAG);
        });

        priorityDialog.getSelectedObservable().subscribe(viewModel::setPriorityValue);
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
        DatePickerDialog dialog = DatePickerFragmentDialog.Material(viewModel);
        dialog.setVersion(DatePickerDialog.Version.VERSION_2);
        dialog.show(getSupportFragmentManager(), DatePickerFragmentDialog.TAG);
    }

    private void showTimePickerDialog() {
        TimePickerDialog dialog = TimePickerFragmentDialog.Material(viewModel);
        dialog.show(getSupportFragmentManager(), TimePickerFragmentDialog.TAG);
    }


    @Override
    public void onShowTimePickerDialog() {
        showTimePickerDialog();
    }

    @Override
    public void onCloseActivity() {
        finish();
    }
}
