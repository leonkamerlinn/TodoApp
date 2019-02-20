package com.kamerlin.leon.todoapp.ui.activity.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.kamerlin.leon.todoapp.db.TodoRoomDatabase;

import javax.inject.Inject;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    private final MainActivity mActivity;
    private final TodoRoomDatabase mDatabase;

    @Inject
    public MainViewModelFactory(MainActivity mainActivity, TodoRoomDatabase database) {
        mActivity = mainActivity;
        mDatabase = database;
    }

    @NonNull
    @Override
    public MainViewModel create(Class modelClass) {
        return new MainViewModel(mActivity, mDatabase);
    }
}
