package com.nixxie.vehiclemarket.di.modules;

import com.nixxie.vehiclemarket.di.scopes.PerFragment;
import com.nixxie.vehiclemarket.fragment.ManufacturersFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentsModule {

    @PerFragment
    @Provides
    ManufacturersFragment provideManufacturersFragment(){
        return ManufacturersFragment.getInstance();
    }
}
