package com.kamerlin.leon.todoapp.di;



import com.kamerlin.leon.todoapp.ui.activity.main.MainActivity;
import com.kamerlin.leon.todoapp.ui.activity.main.MainActivityModule;
import com.kamerlin.leon.todoapp.di.scope.ActivityScoped;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity mainActivity();
}
