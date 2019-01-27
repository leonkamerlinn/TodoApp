package com.kamerlin.leon.todoapp.db.dao;

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


import com.kamerlin.leon.todoapp.db.pojo.Category;
import com.kamerlin.leon.utils.materialpallete.MaterialColor;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import io.reactivex.Observable;

/**
 * The Room Magic is in this file, where you map a Java method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

@Dao
public abstract class CategoryDao {

    @Query("SELECT * FROM category_table ORDER BY name ASC")
    public abstract Observable<List<Category>> getAlphabetizedCategories();

    @Query("SELECT * FROM category_table ORDER BY id ASC")
    public abstract Observable<List<Category>> getCategories();

    @Query("SELECT COUNT(name) FROM category_table WHERE name = :category")
    public abstract Observable<Integer> getNumberOfCategories(String category);


    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insert(Category category);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insertAll(Category... categories);

    @Transaction
    public void deleteAndPopulate() {
        Category[] categories = new Category[] {
                new Category("All", MaterialColor.AMBER),
                new Category("Personal", MaterialColor.BLUE),
                new Category("Shopping", MaterialColor.BLUE_GREY),
                new Category("Work", MaterialColor.BROWN),
                new Category("Sport", MaterialColor.INDIGO),
                new Category("Movies to watch", MaterialColor.LIME),
        };
        deleteAll();
        insertAll(categories);
    }

    @Query("DELETE FROM category_table")
    public abstract void deleteAll();
}
