package com.kamerlin.leon.todoapp.di.modules;

import android.app.Application;
import android.content.Context;

import com.kamerlin.leon.todoapp.db.TodoRoomDatabase;
import com.kamerlin.leon.todoapp.db.category.CategoryDao;
import com.kamerlin.leon.todoapp.db.task.TaskDao;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Leon on 2.2.2018..
 */

@Module
public abstract class ApplicationBindingModule {

    @Binds
    abstract Context bindContext(Application application);


    @Provides
    static TodoRoomDatabase provideDatabase(Application application) {
        return TodoRoomDatabase.getInstance(application.getApplicationContext());
    }

    @Provides
    static TaskDao provideTaskDao(TodoRoomDatabase todoRoomDatabase) {
        return todoRoomDatabase.taskDao();
    }

    @Provides
    static CategoryDao provideCategoryDao(TodoRoomDatabase todoRoomDatabase) {
        return todoRoomDatabase.categoryDao();
    }

    @Provides
    static String provideTag() {
        return ApplicationBindingModule.class.getSimpleName();
    }


}