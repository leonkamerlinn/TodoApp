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

import android.annotation.SuppressLint;
import android.app.Application;

import com.kamerlin.leon.todoapp.db.dao.CategoryDao;
import com.kamerlin.leon.todoapp.db.dao.TaskDao;
import com.kamerlin.leon.todoapp.db.pojo.Category;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */

public class DatabaseRepository {
    private static DatabaseRepository INSTANCE;
    private final TodoRoomDatabase mRoomDatabase;
    private final TaskDao mTaskDao;
    private CategoryDao mCategoryDao;


    // Note that in order to unit test the DatabaseRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    private DatabaseRepository(Application application) {
        mRoomDatabase = TodoRoomDatabase.getInstance(application);
        mCategoryDao = mRoomDatabase.categoryDao();
        mTaskDao = mRoomDatabase.taskDao();
    }


    public static DatabaseRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (DatabaseRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DatabaseRepository(application);
                }

            }
        }

        return INSTANCE;
    }




    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public Observable<List<Category>> getAlphabetizedCategoriesObservable() {
        return mCategoryDao.getAlphabetizedCategories();
    }

    public Observable<Integer> getNumberOfCategoriesObservable(String category) {
        return mCategoryDao.getNumberOfCategories(category);
    }

    public Observable<List<Category>> getCategoriesObservable() {
        return mCategoryDao.getCategories();
    }



    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    @SuppressLint("CheckResult")
    public void insertCategory(Category category) {
        Observable.fromCallable(() -> {
            try {
                mCategoryDao.insert(category);
                return true;
            } catch (Exception e) {
                return false;
            }
        })
        .subscribeOn(Schedulers.io())
        .subscribe();
    }



    public void deleteAndPopulateCategory() {
         Observable.fromCallable(() -> {
            try {
                mCategoryDao.deleteAndPopulate();
                return true;
            } catch (Exception e) {
                return false;
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }


}
