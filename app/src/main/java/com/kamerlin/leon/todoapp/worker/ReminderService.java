package com.kamerlin.leon.todoapp.worker;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.kamerlin.leon.todoapp.db.task.Task;
import com.kamerlin.leon.todoapp.db.task.TaskService;

import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class ReminderService extends IntentService {
    public static final String TAG = ReminderService.class.getSimpleName();

    //Intent actions
    public static final String ACTION_SCHEDULE_REMINDER = TAG + ".SCHEDULE_REMINDER";

    private static final String TASK_EXSTRA = "task_extra";


    public ReminderService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null && intent.getAction() != null) {
            if (ACTION_SCHEDULE_REMINDER.equals(intent.getAction()) && intent.hasExtra(TASK_EXSTRA)) {
                Task task = intent.getParcelableExtra(TASK_EXSTRA);
                long delayFromNow = task.getRemindMe() - System.currentTimeMillis();
                Data data = new Data.Builder()
                        .build();

                OneTimeWorkRequest simpleRequest = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                        .setInitialDelay(delayFromNow, TimeUnit.MILLISECONDS)
                        .setInputData(data)
                        .build();



                String uuid = simpleRequest.getId().toString();

                WorkManager.getInstance().enqueue(simpleRequest);
                TaskService.update(getApplicationContext(), task);
            }
        }
    }

    public static void schedulereminder(Context context, Task task) {
        Intent intent = new Intent(context, ReminderService.class);
        intent.putExtra(TASK_EXSTRA, task);
        intent.setAction(ACTION_SCHEDULE_REMINDER);
        context.startService(intent);
    }
}
