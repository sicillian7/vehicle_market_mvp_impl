package com.nixxie.vehiclemarket.di.components;

import android.content.Context;

import com.nixxie.vehiclemarket.di.modules.ActivityModule;
import com.nixxie.vehiclemarket.di.modules.VehiclesModule;
import com.nixxie.vehiclemarket.di.scopes.PerFragment;
import com.nixxie.vehiclemarket.fragment.BuiltDatesFragment;
import com.nixxie.vehiclemarket.fragment.ManufacturerModelsFragment;
import com.nixxie.vehiclemarket.fragment.ManufacturersFragment;

import dagger.Component;


@PerFragment
@Component(dependencies = {ApplicationComponent.class}, modules = {ActivityModule.class, VehiclesModule.class})
public interface VehiclesComponent {
    Context context();
    void inject(ManufacturersFragment manufacturersFragment);
    void inject(ManufacturerModelsFragment modelsFragment);
    void inject(BuiltDatesFragment builtDatesFragment);
}
