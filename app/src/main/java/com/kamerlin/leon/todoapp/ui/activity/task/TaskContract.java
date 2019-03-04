package com.kamerlin.leon.todoapp.ui.activity.task;

import com.kamerlin.leon.todoapp.db.task.Task;

public interface TaskContract {
    interface View {
        void onShowTimePickerDialog();
        void onCloseActivity();
    }

    interface Model {
        void onTimeSet(int hourOfDay, int minute, int second);
        void onDateSet(int year, int monthOfYear, int dayOfMonth);
        int getHour();
        int getMinute();
        int getSeconds();
        int getYear();
        int getMonth();
        int getDay();
        void insertOrUpdateTask();
        void setTask(Task task);
        void onCreate();
    }
}
