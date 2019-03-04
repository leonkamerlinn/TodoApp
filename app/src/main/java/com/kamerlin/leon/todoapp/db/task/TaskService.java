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
    public static final String ACTION_SWAP = TAG + ".SWAP";
    public static final String ACTION_DELETE_AND_POPULATE = TAG + ".DELETE_AND_POPULATE";

    // Intent extras
    private static final String EXTRA_TASK = "extra_task";
    private static final String EXTRA_TASK_FROM = "extra_task_from";
    private static final String EXTRA_TASK_TO = "extra_task_to";


    @Inject
    TaskDao taskDao;


    public TaskService() {
        super(TAG);
    }

    public static void swapTask(Context context, Task from, Task to) {
        Intent intent = new Intent(context, TaskService.class);
        intent.setAction(ACTION_SWAP);
        intent.putExtra(EXTRA_TASK_FROM, from);
        intent.putExtra(EXTRA_TASK_TO, to);
        context.startService(intent);
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
        } else if (ACTION_SWAP.equals(action)) {
            handleSwap(intent);
        }
    }

    private void handleSwap(Intent intent) {
        Task from = intent.getParcelableExtra(EXTRA_TASK_FROM);
        Task to = intent.getParcelableExtra(EXTRA_TASK_TO);

        taskDao.swapTasks(from, to);
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
