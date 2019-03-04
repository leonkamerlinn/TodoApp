package com.kamerlin.leon.todoapp.ui.activity.main;

import android.app.Activity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.kamerlin.leon.todoapp.R;
import com.kamerlin.leon.todoapp.databinding.ActivityMainBinding;
import com.kamerlin.leon.todoapp.databinding.ActivityTaskBinding;
import com.kamerlin.leon.todoapp.di.scope.ActivityScoped;
import com.kamerlin.leon.todoapp.ui.activity.task.TaskActivity;
import com.kamerlin.leon.todoapp.ui.activity.task.TaskViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Leon on 2.2.2018..
 */

@Module
public abstract class MainActivityModule {

    @Binds
    abstract Activity provideActivity(MainActivity mainActivity);


    @Provides
    @ActivityScoped
    static MainViewModel provideMainViewModel(MainActivity activity, MainViewModelFactory mainViewModelFactory) {

        return ViewModelProviders.of(activity, mainViewModelFactory).get(MainViewModel.class);

    }

    @Provides
    @ActivityScoped
    static ActivityMainBinding provideViewBinding(MainActivity activity, MainViewModel viewModel) {
        ActivityMainBinding binding = DataBindingUtil.setContentView(activity, R.layout.activity_main);
        binding.setLifecycleOwner(activity);
        binding.setModel(viewModel);
        return binding;
    }
}