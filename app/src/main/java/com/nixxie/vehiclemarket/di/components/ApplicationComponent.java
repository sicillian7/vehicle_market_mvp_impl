package com.nixxie.vehiclemarket.di.components;

import com.nixxie.vehiclemarket.VehicleMarketApplication;
import com.nixxie.vehiclemarket.di.modules.ApplicationModule;
import com.nixxie.vehiclemarket.di.modules.NetworkModule;
import com.nixxie.vehiclemarketmvc.network.VehicleRepository;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {
    VehicleMarketApplication exposeContext();
    VehicleRepository exposeVehicleRepo();
}
