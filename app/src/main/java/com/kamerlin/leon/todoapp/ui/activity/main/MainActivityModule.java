package com.kamerlin.leon.todoapp.ui.activity.main;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Leon on 2.2.2018..
 */

@Module
public abstract class MainActivityModule {

    @Binds
    abstract Activity provideActivity(MainActivity mainActivity);

}