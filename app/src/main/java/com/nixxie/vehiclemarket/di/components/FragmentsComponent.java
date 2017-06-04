package com.nixxie.vehiclemarket.di.components;

import com.nixxie.vehiclemarket.activity.MainActivity;
import com.nixxie.vehiclemarket.di.modules.FragmentsModule;
import com.nixxie.vehiclemarket.di.scopes.PerFragment;

import dagger.Component;


@PerFragment
@Component(modules = {FragmentsModule.class})
public interface FragmentsComponent {
    void inject(MainActivity mainActivity);
}
