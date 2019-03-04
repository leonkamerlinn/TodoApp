package com.kamerlin.leon.todoapp;

import android.content.Context;

import com.kamerlin.leon.todoapp.db.TodoRoomDatabase;
import com.kamerlin.leon.todoapp.db.task.Task;
import com.kamerlin.leon.todoapp.ui.dialog.ReminderFragmentDialog;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    @Test
    public void taskTest() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        TodoRoomDatabase database = TodoRoomDatabase.getInstance(appContext);


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
                new Task(100, "Task name #1", "Task description #1", now + one_day, false, Task.PRIORITY_NONE, now, (now + one_day - ReminderFragmentDialog.getData().get("10 minute before")), false, "Personal"),
                new Task(99, "Task name #2", "Task description #2", now + one_day*2, false, Task.PRIORITY_HIGH, now, (now + one_day*2), false, "Shopping"),
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
                new Task(15, "Task name #15", "Task description #15", now + one_day, true, Task.PRIORITY_NONE, now, (now + five_hour), false, "Movies to watch"),

        };

        database.clearAllTables();

        database.categoryDao().deleteAndPopulate();

        Task testTask = tasks[0];
        Task testTask2 = tasks[1];
        Task testTask3 = tasks[2];

        database.taskDao().insert(testTask);

        Task givenTask = database.taskDao().getTaskById(testTask.getId());
        assertEquals(testTask, givenTask);

        int count = database.taskDao().getNumberOfTasksById(testTask.getId());
        assertEquals(count, 1);

        database.taskDao().delete(testTask);

        int count2 = database.taskDao().getNumberOfTasksById(testTask.getId());
        assertEquals(count2, 0);


        database.taskDao().insertAll(testTask, testTask2, testTask3);


        int count3 = database.taskDao().getNumberOfTasksById(testTask3.getId());
        assertEquals(count3, 1);

        Task taskBefore = database.taskDao().getTaskById(testTask.getId());
        assertEquals(taskBefore.getId(), 100);


        database.taskDao().swapTasks(testTask, testTask3);

        Task taskLater = database.taskDao().getTaskById(testTask.getId());
        assertEquals(taskLater.getId(), 3);


    }
}
