package com.kamerlin.leon.todoapp.application;

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;

/**
 * Created by Leon on 2.2.2018..
 */

@Module
public abstract class ApplicationModule {

    @Binds
    abstract Context bindContext(Application application);

}