package com.kamerlin.leon.todoapp.di.modules;


import com.kamerlin.leon.todoapp.di.scope.ActivityScoped;
import com.kamerlin.leon.todoapp.ui.activity.main.MainActivity;
import com.kamerlin.leon.todoapp.ui.activity.main.MainActivityModule;
import com.kamerlin.leon.todoapp.ui.activity.task.TaskActivity;
import com.kamerlin.leon.todoapp.ui.activity.task.TaskActivityModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = TaskActivityModule.class)
    abstract TaskActivity taskActivity();
}
