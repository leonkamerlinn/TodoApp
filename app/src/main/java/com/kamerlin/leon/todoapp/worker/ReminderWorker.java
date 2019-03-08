package com.kamerlin.leon.todoapp.worker;


import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.kamerlin.leon.todoapp.db.TodoRoomDatabase;
import com.kamerlin.leon.todoapp.db.task.Task;
import com.kamerlin.leon.todoapp.db.task.TaskService;
import com.kamerlin.leon.todoapp.utils.NotificationUtil;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ReminderWorker extends Worker {


    private final TodoRoomDatabase database;

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        database = TodoRoomDatabase.getInstance(context);
    }

    @SuppressLint("CheckResult")
    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        long createdAt = data.getLong(Task.CREATED_AT_TAG, -1);

        database.taskDao().getTaskByCreationSingle(createdAt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(task -> {
                    NotificationUtil.createNotification(data.getString(Task.TITLE_TAG), data.getString(Task.DESCRIPTION_TAG), getApplicationContext());
                    task.setIsScheduled(true);
                    TaskService.update(getApplicationContext(), task);
                }, System.err::println);
        return Worker.Result.success();
    }
}
