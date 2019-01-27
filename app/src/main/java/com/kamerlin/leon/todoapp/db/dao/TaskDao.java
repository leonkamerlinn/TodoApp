package com.kamerlin.leon.todoapp.db.dao;


import com.kamerlin.leon.todoapp.db.pojo.Task;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public abstract class TaskDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insert(Task task);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insertAll(Task... tasks);
}
