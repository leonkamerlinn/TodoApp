package com.kamerlin.leon.todoapp.state_pattern;

import com.kamerlin.leon.todoapp.db.TodoRoomDatabase;
import com.kamerlin.leon.todoapp.db.task.Task;
import com.kamerlin.leon.todoapp.db.task.TaskDao;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Single;

public class SortPriority implements SortState {

    private final TaskDao taskDao;
    private final String selectedCategory;

    public SortPriority(TodoRoomDatabase todoRoomDatabase, String selectedCategory) {
        taskDao = todoRoomDatabase.taskDao();
        this.selectedCategory = selectedCategory;
    }
    @Override
    public LiveData<List<Task>> getTasksLiveData() {
        if (selectedCategory.equals("All")) {
            return taskDao.getTasksByPriorityLiveData();
        }

        return taskDao.getCategoryTasksByPriorityLiveData(selectedCategory);
    }

    @Override
    public Single<List<Task>> getSingle() {
        if (selectedCategory.equals("All")) {
            return taskDao.getTasksByPrioritySingle();
        }

        return taskDao.getCategoryTasksByPrioritySingle(selectedCategory);
    }
}
