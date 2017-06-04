package com.nixxie.vehiclemarket.utils;

import com.nixxie.vehiclemarket.base.PresenterFactory;
import com.nixxie.vehiclemarketmvc.mvp.presenter.VehicleSelectorPresenter;

import javax.inject.Inject;

/**
 * Created by nikolahristovski on 6/2/17.
 */

public class VehiclePresenterFactory implements PresenterFactory<VehicleSelectorPresenter>{

    VehicleSelectorPresenter presenter;

    public VehiclePresenterFactory(VehicleSelectorPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public VehicleSelectorPresenter create() {
        return presenter;
    }
}
