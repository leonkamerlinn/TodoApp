package com.kamerlin.leon.todoapp.application;

import android.app.Application;

import com.kamerlin.leon.todoapp.di.modules.ActivityBindingModule;
import com.kamerlin.leon.todoapp.di.modules.ApplicationBindingModule;
import com.kamerlin.leon.todoapp.di.modules.FragmentDialogBindingModule;
import com.kamerlin.leon.todoapp.di.modules.ServiceBindingModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by Leon on 2.2.2018..
 */

@Singleton
@Component(modules = {ApplicationBindingModule.class, ActivityBindingModule.class, ServiceBindingModule.class, FragmentDialogBindingModule.class, AndroidSupportInjectionModule.class})
public interface ApplicationComponent extends AndroidInjector<MainApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        ApplicationComponent.Builder application(Application application);
        ApplicationComponent build();
    }
}
