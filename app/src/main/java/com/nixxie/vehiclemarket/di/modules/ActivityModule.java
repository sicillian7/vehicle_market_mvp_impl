package com.nixxie.vehiclemarket.di.modules;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import com.nixxie.vehiclemarket.di.scopes.PerFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    @PerFragment
    public Context provideContext(){
        return activity;
    }
}
