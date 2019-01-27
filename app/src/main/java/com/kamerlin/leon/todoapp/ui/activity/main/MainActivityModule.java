package com.kamerlin.leon.todoapp.ui.activity.main;

import android.app.Activity;

import com.kamerlin.leon.todoapp.application.ApplicationModule;
import com.kamerlin.leon.todoapp.di.scope.ActivityScoped;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Leon on 2.2.2018..
 */

@Module
public abstract class MainActivityModule {

    @Binds
    abstract Activity provideActivity(MainActivity mainActivity);


}