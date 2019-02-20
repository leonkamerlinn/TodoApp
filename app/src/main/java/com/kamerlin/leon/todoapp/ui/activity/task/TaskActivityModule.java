package com.kamerlin.leon.todoapp.ui.activity.task;

import android.app.Activity;

import com.kamerlin.leon.todoapp.R;
import com.kamerlin.leon.todoapp.databinding.ActivityTaskBinding;
import com.kamerlin.leon.todoapp.di.scope.ActivityScoped;
import com.kamerlin.leon.todoapp.ui.activity.main.MainActivity;
import com.kamerlin.leon.todoapp.ui.activity.main.MainViewModel;
import com.kamerlin.leon.todoapp.ui.activity.main.MainViewModelFactory;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Leon on 2.2.2018..
 */

@Module
public abstract class TaskActivityModule {

    @Binds
    abstract Activity provideActivity(TaskActivity taskActivity);


    @Provides
    @ActivityScoped
    static TaskViewModel provideTaskViewModel(TaskActivity activity, TaskViewModelFactory taskViewModelFactory) {

        return ViewModelProviders.of(activity, taskViewModelFactory).get(TaskViewModel.class);

    }

    @Provides
    @ActivityScoped
    static ActivityTaskBinding provideViewBinding(TaskActivity activity, TaskViewModel viewModel) {
        ActivityTaskBinding binding = DataBindingUtil.setContentView(activity, R.layout.activity_task);
        binding.setLifecycleOwner(activity);
        binding.setModel(viewModel);

        return binding;
    }

    @Provides
    @ActivityScoped
    static TaskContract.View provideViewContract(TaskActivity activity) {
        return activity;
    }

    @Provides
    @ActivityScoped
    static TaskContract.Model provideModelContract(TaskViewModel taskViewModel) {
        return taskViewModel;
    }
}