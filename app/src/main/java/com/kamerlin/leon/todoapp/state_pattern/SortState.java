package com.kamerlin.leon.todoapp.state_pattern;

import com.kamerlin.leon.todoapp.db.task.Task;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Single;

public interface SortState {
    LiveData<List<Task>> getTasksLiveData();
    Single<List<Task>> getSingle();
}
