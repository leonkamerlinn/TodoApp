package com.kamerlin.leon.todoapp.db.category;

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


import com.kamerlin.leon.utils.materialpallete.MaterialColor;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import io.reactivex.Observable;
import io.reactivex.Single;

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

    @Query("SELECT * FROM category_table ORDER BY category_name ASC")
    public abstract Observable<List<Category>> getAlphabetizedCategoriesObservable();

    @Query("SELECT * FROM category_table ORDER BY category_name ASC")
    public abstract LiveData<List<Category>> getAlphabetizedCategoriesLiveData();

    @Query("SELECT * FROM category_table")
    public abstract LiveData<List<Category>> getCategoriesLiveData();

    @Query("SELECT * FROM category_table WHERE category_name = :categoryName")
    public abstract Single<Category> getCategoryByNameSingle(String categoryName);

    @Query("SELECT * FROM category_table WHERE category_name = :categoryName")
    public abstract Category getCategoryByName(String categoryName);

    @Query("SELECT COUNT(category_name) FROM category_table WHERE category_name = :category")
    public abstract Observable<Integer> getCategoriesNumberObservable(String category);

    @Query("SELECT COUNT(category_name) FROM category_table WHERE category_name = :category")
    public abstract LiveData<Integer> getCategoriesNumberLiveData(String category);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Category category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(Category... categories);

    @Transaction
    public void deleteAndPopulate() {
        Category[] categories = new Category[] {
                new Category(1,"All", MaterialColor.AMBER),
                new Category(2,"Personal", MaterialColor.BLUE),
                new Category(3,"Shopping", MaterialColor.BLUE_GREY),
                new Category(4,"Work", MaterialColor.BROWN),
                new Category(5,"Sport", MaterialColor.INDIGO),
                new Category(6,"Movies to watch", MaterialColor.LIME),
        };
        deleteAll();
        insertAll(categories);
    }


    @Delete
    public abstract void delete(Category category);


    public void delete(String categoryName) {
        Category category = getCategoryByName(categoryName);
        if (category == null) throw new NullPointerException("Category is null");
        delete(category);
    }

    @Query("DELETE FROM category_table")
    public abstract void deleteAll();
}
