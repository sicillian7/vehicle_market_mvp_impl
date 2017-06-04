package com.nixxie.vehiclemarket.di.modules;

import android.content.Context;

import com.nixxie.vehiclemarket.VehicleMarketApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class ApplicationModule {


    private VehicleMarketApplication mVMarketApplication;

    public ApplicationModule(VehicleMarketApplication mVMarketApplication) {
        this.mVMarketApplication = mVMarketApplication;
    }

    @Singleton
    @Provides
    VehicleMarketApplication provideApplication(){
        return mVMarketApplication;
    }

    @Singleton
    @Provides
    Context provideContext(){
        return mVMarketApplication;
    }
}
