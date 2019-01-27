package com.kamerlin.leon.todoapp.application;

import android.app.Application;
import android.content.Context;


import com.kamerlin.leon.todoapp.db.DatabaseRepository;


import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Leon on 2.2.2018..
 */

@Module
public abstract class ApplicationModule {

    @Binds
    abstract Context bindContext(Application application);



    @Provides
    static DatabaseRepository provideDatabase(Application application) {
        return DatabaseRepository.getInstance(application);
    }


}