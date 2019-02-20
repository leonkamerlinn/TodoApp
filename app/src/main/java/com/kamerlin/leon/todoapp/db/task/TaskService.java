package com.kamerlin.leon.todoapp.db.task;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.kamerlin.leon.todoapp.db.TodoRoomDatabase;
import com.kamerlin.leon.todoapp.db.category.Category;

import javax.inject.Inject;

import dagger.android.DaggerIntentService;

public class TaskService extends DaggerIntentService {
    private static final String TAG = TaskService.class.getSimpleName();
    //Intent actions
    public static final String ACTION_INSERT = TAG + ".INSERT";
    public static final String ACTION_UPDATE = TAG + ".UPDATE";
    public static final String ACTION_DELETE = TAG + ".DELETE";
    public static final String ACTION_DELETE_AND_POPULATE = TAG + ".DELETE_AND_POPULATE";

    // Intent extras
    private static final String EXTRA_TASK = "extra_task";


    @Inject
    TaskDao taskDao;


    public TaskService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) return;
        String action = intent.getAction();
        if (ACTION_INSERT.equals(action)) {
            handleInsert(intent);
        } else if (ACTION_DELETE.equals(action)) {
            handleDelete(intent);
        } else if (ACTION_UPDATE.equals(action)) {
            handleUpdate(intent);
        } else if (ACTION_DELETE_AND_POPULATE.equals(action)) {
            handleDeleteAndPopulate();
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    public static void insert(Context context, Task task) {
        Intent intent = new Intent(context, TaskService.class);
        intent.setAction(ACTION_INSERT);
        intent.putExtra(EXTRA_TASK, task);
        context.startService(intent);
    }

    public static void delete(Context context, Task task) {
        Intent intent = new Intent(context, TaskService.class);
        intent.setAction(ACTION_DELETE);
        intent.putExtra(EXTRA_TASK, task);
        context.startService(intent);
    }

    public static void update(Context context, Task task) {
        Intent intent = new Intent(context, TaskService.class);
        intent.setAction(ACTION_UPDATE);
        intent.putExtra(EXTRA_TASK, task);
        context.startService(intent);
    }

    public static void deleteAndPopulate(Context context) {
        Intent intent = new Intent(context, TaskService.class);
        intent.setAction(ACTION_DELETE_AND_POPULATE);
        context.startService(intent);
    }

    private void handleInsert(Intent intent) {
        Task task = intent.getParcelableExtra(EXTRA_TASK);
        taskDao.insert(task);
    }

    private void handleDelete(Intent intent) {
        Task task = intent.getParcelableExtra(EXTRA_TASK);
        taskDao.delete(task);
    }

    private void handleUpdate(Intent intent) {
        Task task = intent.getParcelableExtra(EXTRA_TASK);
        taskDao.update(task);
    }

    private void handleDeleteAndPopulate() {
        taskDao.deleteAndPopulate();
    }
}
