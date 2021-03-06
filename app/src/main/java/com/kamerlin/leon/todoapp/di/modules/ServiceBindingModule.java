package com.kamerlin.leon.todoapp.di.modules;


import com.kamerlin.leon.todoapp.db.category.CategoryService;
import com.kamerlin.leon.todoapp.db.task.TaskService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceBindingModule {
    @ContributesAndroidInjector()
    abstract CategoryService categoryService();

    @ContributesAndroidInjector()
    abstract TaskService taskService();

}
