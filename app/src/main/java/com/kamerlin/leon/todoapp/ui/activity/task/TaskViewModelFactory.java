package com.kamerlin.leon.todoapp.ui.activity.task;

import com.kamerlin.leon.todoapp.db.TodoRoomDatabase;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

public class TaskViewModelFactory implements ViewModelProvider.Factory {
    private final TaskActivity mActivity;
    private final TaskContract.View mView;
    private final TodoRoomDatabase mDatabase;

    @Inject
    public TaskViewModelFactory(TaskActivity taskActivity, TaskContract.View view, TodoRoomDatabase database) {
        mActivity = taskActivity;
        mView = view;
        mDatabase = database;
    }

    @NonNull
    @Override
    public TaskViewModel create(Class modelClass) {
        return new TaskViewModel(mActivity, mView, mDatabase);
    }
}
