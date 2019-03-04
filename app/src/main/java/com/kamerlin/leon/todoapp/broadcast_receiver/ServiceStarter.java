package com.kamerlin.leon.todoapp.broadcast_receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.kamerlin.leon.todoapp.db.TodoRoomDatabase;
import com.kamerlin.leon.todoapp.db.task.Task;
import com.kamerlin.leon.todoapp.db.task.TaskService;
import com.kamerlin.leon.todoapp.worker.ReminderWorker;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import dagger.android.DaggerBroadcastReceiver;
import io.reactivex.schedulers.Schedulers;

public class ServiceStarter extends DaggerBroadcastReceiver {
    @Inject
    TodoRoomDatabase database;


    @SuppressLint("CheckResult")
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        database.taskDao().getTasksObservable().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(tasks -> {
                    for (Task task : tasks) {
                        if (!task.isScheduled()) {
                            task.scheduleReminder();
                        }
                    }
                });





    }


}
