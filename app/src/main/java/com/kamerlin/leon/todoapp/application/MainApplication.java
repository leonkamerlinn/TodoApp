package com.kamerlin.leon.todoapp.application;



import androidx.multidex.MultiDex;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;


/**
 * Created by Leon on 2.2.2018..
 */

public class MainApplication extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerApplicationComponent.builder().application(this).build();
    }
}