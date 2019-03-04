package com.kamerlin.leon.todoapp.state_pattern;

import com.kamerlin.leon.todoapp.db.TodoRoomDatabase;
import com.kamerlin.leon.todoapp.db.task.Task;
import com.kamerlin.leon.todoapp.db.task.TaskDao;

import java.util.List;

import androidx.lifecycle.LiveData;
import io.reactivex.Single;

public class SortDueDate implements SortState {

    private final TaskDao taskDao;
    private final String selectedCategory;

    public SortDueDate(TodoRoomDatabase todoRoomDatabase, String selectedCategory) {
        taskDao = todoRoomDatabase.taskDao();
        this.selectedCategory = selectedCategory;
    }
    @Override
    public LiveData<List<Task>> getTasksLiveData() {
        if (selectedCategory.equals("All")) {
            return taskDao.getTasksByDueDateLiveData();
        }

        return taskDao.getCategoryTasksByDueDateLiveData(selectedCategory);
    }

    @Override
    public Single<List<Task>> getSingle() {
        if (selectedCategory.equals("All")) {
            return taskDao.getTasksByDueDateSingle();
        }

        return taskDao.getCategoryTasksByDueDateSingle(selectedCategory);
    }
}
