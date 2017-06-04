package com.nixxie.vehiclemarket;

import android.app.Application;

import com.nixxie.vehiclemarket.di.components.ApplicationComponent;
import com.nixxie.vehiclemarket.di.components.DaggerApplicationComponent;
import com.nixxie.vehiclemarket.di.modules.ApplicationModule;
import com.nixxie.vehiclemarket.di.modules.NetworkModule;
import com.nixxie.vehiclemarket.utils.PresentersCacher;


public class VehicleMarketApplication extends Application{

    private ApplicationComponent applicationComponent;
    private PresentersCacher presentersCacher;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule(BuildConfig.baseUrl))
                .build();

        presentersCacher = PresentersCacher.getInstance();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public PresentersCacher getPresenterCacher(){
        return presentersCacher;
    }
}
