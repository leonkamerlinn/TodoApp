package com.kamerlin.leon.todoapp.di.modules;


import com.kamerlin.leon.todoapp.ui.dialog.CategoryFragmentDialog;
import com.kamerlin.leon.todoapp.ui.dialog.PriorityFragmentDialog;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module()
public abstract class FragmentDialogBindingModule {
    @ContributesAndroidInjector()
    abstract PriorityFragmentDialog priorityFragmentDialog();

    @ContributesAndroidInjector()
    abstract CategoryFragmentDialog categoryFragmentDialog();

}
