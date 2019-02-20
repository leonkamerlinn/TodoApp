package com.kamerlin.leon.todoapp.db;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kamerlin.leon.todoapp.db.category.CategoryDao;
import com.kamerlin.leon.todoapp.db.task.TaskDao;
import com.kamerlin.leon.todoapp.db.category.Category;
import com.kamerlin.leon.todoapp.db.task.Task;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */

@Database(entities = {Category.class, Task.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class TodoRoomDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "todo_database4";

    public abstract CategoryDao categoryDao();
    public abstract TaskDao taskDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile TodoRoomDatabase INSTANCE;

    public static TodoRoomDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (TodoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TodoRoomDatabase.class, DATABASE_NAME)
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this codelab.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            populateDatabase();
        }
    };

    private static void populateDatabase() {
        Observable.fromCallable(() -> {

            INSTANCE.runInTransaction(() -> {
                INSTANCE.categoryDao().deleteAndPopulate();
                INSTANCE.taskDao().deleteAndPopulate();
            });

            return true;
        })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     *
     * If you want to populate the database only when the database is created for the 1st time,
     * override RoomDatabase.Callback()#onCreate
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            //populateDatabase();
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            populateDatabase();
        }
    };



}
