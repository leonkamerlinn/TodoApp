package com.kamerlin.leon.todoapp.db.task;


import com.kamerlin.leon.todoapp.ui.dialog.ReminderFragmentDialog;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public abstract class TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @Transaction
    public abstract void insert(Task task);

    @Delete
    public abstract void delete(Task task);

    @Update
    @Transaction
    public abstract void update(Task task);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    public abstract void insertAll(Task... tasks);

    @Query("DELETE FROM task_table")
    public abstract void deleteAll();

    @Transaction
    public void deleteAndPopulate() {

        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        long one_second = 1000L;
        long one_minute = 60 * one_second;
        long ten_minute = 10 * one_minute;
        long half_hour = 30 * one_minute;
        long one_hour = 60 * one_minute;
        long two_hour = 2 * one_hour;
        long five_hour = 5 * one_hour;
        long ten_hour = 10 * one_hour;
        long one_day = 24 * one_hour;

        Task[] tasks = new Task[] {
                new Task(1, "Task name #1", "Task description #1", now + one_day, false, Task.PRIORITY_NONE, now, (now + one_day - ReminderFragmentDialog.getData().get("10 minute before")), false, "Personal"),
                /*new Task(2, "Task name #2", "Task description #2", now + one_day*2, false, Task.PRIORITY_HIGH, now, (now + one_day*2), false, "Shopping"),
                new Task(3, "Task name #3", "Task description #3", now + one_day*3, false, Task.PRIORITY_LOW, now, (now + one_day), false, "Work"),
                new Task(4, "Task name #4", "Task description #4", -1, true, Task.PRIORITY_MEDIUM, now, (now + one_day), false, "Sport"),
                new Task(5, "Task name #5", "Task description #5", -1, true, Task.PRIORITY_NONE, now, (now + two_hour), true, "Movies to watch"),
                new Task(6, "Task name #6", "Task description #6", now + one_day, false, Task.PRIORITY_NONE, now, (now + one_day), false, "Personal"),
                new Task(7, "Task name #7", "Task description #7", now + one_day*4, false, Task.PRIORITY_LOW, now, (now + one_day), false, "Shopping"),
                new Task(8, "Task name #8", "Task description #8", now + one_day, true, Task.PRIORITY_NONE, now, (now + five_hour), false, "Work"),
                new Task(9, "Task name #9", "Task description #9", -1, false, Task.PRIORITY_HIGH, now, (now + one_day), false, "Sport"),
                new Task(10, "Task name #10", "Task description #10", now + one_day, false, Task.PRIORITY_NONE, now, (now + ten_hour), true, "Movies to watch"),
                new Task(11, "Task name #11", "Task description #11", now + one_day*3, false, Task.PRIORITY_NONE, now, (now + one_day), false, "Personal"),
                new Task(12, "Task name #12", "Task description #12", now + one_day*4, false, Task.PRIORITY_NONE, now, (now + one_day), false, "Shopping"),
                new Task(13, "Task name #13", "Task description #13", now + one_day, true, Task.PRIORITY_MEDIUM, now, (now + one_day), false, "Work"),
                new Task(14, "Task name #14", "Task description #14", now + one_day*5, false, Task.PRIORITY_NONE, now, (now + one_day), true, "Sport"),
                new Task(15, "Task name #15", "Task description #15", now + one_day, true, Task.PRIORITY_NONE, now, (now + five_hour), false, "Movies to watch"),*/

        };


        deleteAll();
        insertAll(tasks);

    }



    @Query("SELECT * FROM task_table")
    public abstract Observable<List<Task>> getTasksObservable();

    @Query("SELECT * FROM task_table WHERE task_category_name = :categoryName")
    public abstract Observable<List<Task>> getCategoryTasksObservable(String categoryName);

    @Query("SELECT * FROM task_table WHERE created_at = :createdAt")
    public abstract Single<Task> getTaskByCreationSingle(long createdAt);




    /* None */

    @Query("SELECT * FROM task_table ORDER BY task_id")
    public abstract LiveData<List<Task>> getTasksLiveData();

    @Query("SELECT * FROM task_table WHERE task_category_name = :categoryName ORDER BY task_id")
    public abstract LiveData<List<Task>> getCategoryTasksLiveData(String categoryName);

    /* Priority */

    @Query("SELECT * FROM task_table ORDER BY priority_code DESC")
    public abstract LiveData<List<Task>> getTasksByPriorityLiveData();

    @Query("SELECT * FROM task_table WHERE task_category_name = :category ORDER BY priority_code DESC")
    public abstract LiveData<List<Task>> getCategoryTasksByPriorityLiveData(String category);

    /* Favorite */

    @Query("SELECT * FROM task_table ORDER BY favorite DESC")
    public abstract LiveData<List<Task>> getTasksByFavoriteLiveData();

    @Query("SELECT * FROM task_table WHERE task_category_name = :category ORDER BY favorite DESC")
    public abstract LiveData<List<Task>> getCategoryTasksByFavoriteLiveData(String category);

    /* Alphabetical */

    @Query("SELECT * FROM task_table ORDER BY task_name ASC, task_description ASC")
    public abstract LiveData<List<Task>> getAlphabeticalTasksLiveData();

    @Query("SELECT * FROM task_table WHERE task_category_name = :category ORDER BY task_name ASC, task_description ASC")
    public abstract LiveData<List<Task>> getAlphabeticalCategoryTasksLiveData(String category);

    /* Alphabetical Inverse */

    @Query("SELECT * FROM task_table ORDER BY task_name DESC, task_description DESC")
    public abstract LiveData<List<Task>> getInverseAlphabeticalTasksLiveData();

    @Query("SELECT * FROM task_table WHERE task_category_name = :category ORDER BY task_name DESC, task_description DESC")
    public abstract LiveData<List<Task>> getInverseAlphabeticalCategoryTasksLiveData(String category);

    /* Created */

    @Query("SELECT * FROM task_table ORDER BY created_at ASC")
    public abstract LiveData<List<Task>> getTasksByCreatedLiveData();

    @Query("SELECT * FROM task_table WHERE task_category_name = :category ORDER BY created_at ASC")
    public abstract LiveData<List<Task>> getCategoryTasksByCreatedLiveData(String category);

    /* Created Inverse */

    @Query("SELECT * FROM task_table ORDER BY created_at DESC")
    public abstract LiveData<List<Task>> getTasksByCreatedInverseLiveData();

    @Query("SELECT * FROM task_table WHERE task_category_name = :category ORDER BY created_at DESC")
    public abstract LiveData<List<Task>> getCategoryTasksByCreatedInverseLiveData(String category);

    /* Due Date */

    @Query("SELECT * FROM task_table ORDER BY due_date ASC")
    public abstract LiveData<List<Task>> getTasksByDueDateLiveData();

    @Query("SELECT * FROM task_table WHERE task_category_name = :category ORDER BY due_date ASC")
    public abstract LiveData<List<Task>> getCategoryTasksByDueDateLiveData(String category);

    /* Due Date Inverse */

    @Query("SELECT * FROM task_table WHERE task_category_name = :category ORDER BY due_date DESC")
    public abstract LiveData<List<Task>> getCategoryTasksByDueDateInverseLiveData(String category);

    @Query("SELECT * FROM task_table ORDER BY due_date DESC")
    public abstract LiveData<List<Task>> getTasksByDueDateInverseLiveData();










    /* None */

    @Query("SELECT * FROM task_table")
    public abstract Single<List<Task>> getTasksSingle();

    @Query("SELECT * FROM task_table WHERE task_category_name = :categoryName")
    public abstract Single<List<Task>> getCategoryTasksSingle(String categoryName);

    /* Priority */

    @Query("SELECT * FROM task_table ORDER BY priority_code DESC")
    public abstract Single<List<Task>> getTasksByPrioritySingle();

    @Query("SELECT * FROM task_table WHERE task_category_name = :category ORDER BY priority_code DESC")
    public abstract Single<List<Task>> getCategoryTasksByPrioritySingle(String category);


    /* Favorite */

    @Query("SELECT * FROM task_table ORDER BY favorite DESC")
    public abstract Single<List<Task>> getTasksByFavoriteSingle();

    @Query("SELECT * FROM task_table WHERE task_category_name = :category ORDER BY favorite DESC")
    public abstract Single<List<Task>> getCategoryTasksByFavoriteSingle(String category);

    /* Alphabetical */

    @Query("SELECT * FROM task_table ORDER BY task_name ASC, task_description ASC")
    public abstract Single<List<Task>> getAlphabeticalTasksSingle();

    @Query("SELECT * FROM task_table WHERE task_category_name = :category ORDER BY task_name ASC, task_description ASC")
    public abstract Single<List<Task>> getAlphabeticalCategoryTasksSingle(String category);

    /* Alphabetical Inverse */

    @Query("SELECT * FROM task_table ORDER BY task_name DESC, task_description DESC")
    public abstract Single<List<Task>> getInverseAlphabeticalTasksSingle();

    @Query("SELECT * FROM task_table WHERE task_category_name = :category ORDER BY task_name DESC, task_description DESC")
    public abstract Single<List<Task>> getInverseAlphabeticalCategoryTasksSingle(String category);

    /* Created */

    @Query("SELECT * FROM task_table ORDER BY created_at ASC")
    public abstract Single<List<Task>> getTasksByCreatedSingle();

    @Query("SELECT * FROM task_table WHERE task_category_name = :category ORDER BY created_at ASC")
    public abstract Single<List<Task>> getCategoryTasksByCreatedSingle(String category);

    /* Created Inverse */

    @Query("SELECT * FROM task_table ORDER BY created_at DESC")
    public abstract Single<List<Task>> getTasksByCreatedInverseSingle();

    @Query("SELECT * FROM task_table WHERE task_category_name = :category ORDER BY created_at DESC")
    public abstract Single<List<Task>> getCategoryTasksByCreatedInverseSingle(String category);

    /* Due Date */

    @Query("SELECT * FROM task_table ORDER BY due_date ASC")
    public abstract Single<List<Task>> getTasksByDueDateSingle();

    @Query("SELECT * FROM task_table WHERE task_category_name = :category ORDER BY due_date ASC")
    public abstract Single<List<Task>> getCategoryTasksByDueDateSingle(String category);

    /* Due Date Inverse */

    @Query("SELECT * FROM task_table WHERE task_category_name = :category ORDER BY due_date DESC")
    public abstract Single<List<Task>> getCategoryTasksByDueDateInverseSingle(String category);

    @Query("SELECT * FROM task_table ORDER BY due_date DESC")
    public abstract Single<List<Task>> getTasksByDueDateInverseSingle();


    @Transaction
    public void swapTasks(Task from, Task to) {
        long from_id = from.getId();
        long to_id = to.getId();

        delete(from);
        delete(to);
        from.setId(to_id);
        to.setId(from_id);
        insert(from);
        insert(to);

    }
}
