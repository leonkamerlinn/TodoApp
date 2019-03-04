package com.kamerlin.leon.todoapp.worker;

import android.annotation.SuppressLint;
import android.content.Context;

import com.kamerlin.leon.todoapp.db.TodoRoomDatabase;
import com.kamerlin.leon.todoapp.db.task.Task;
import com.kamerlin.leon.todoapp.db.task.TaskService;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import io.reactivex.schedulers.Schedulers;

public class TaskCleanerWorker extends Worker {
    private final TodoRoomDatabase database;

    public TaskCleanerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        database = TodoRoomDatabase.getInstance(context);
    }

    @SuppressLint("CheckResult")
    @NonNull
    @Override
    public Result doWork() {
        database.taskDao().getTasksObservable().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(tasks -> {

                    for (Task task: tasks) {
                        if (task.isCompleted()) {
                            if (task.hasWorkId()) {
                                WorkManager.getInstance().cancelWorkById(UUID.fromString(task.getWorkerId()));
                            }

                            TaskService.delete(getApplicationContext(), task);
                        }



                    }
                });
        return Result.success();
    }
}
