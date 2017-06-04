package com.nixxie.vehiclemarket.busEvents;

import com.nixxie.vehiclemarketmvc.mvp.model.VehicleModel;

/**
 * Created by nikolahristovski on 6/4/17.
 */

public class SelectedModelEvent {

    private VehicleModel model;

    public SelectedModelEvent(VehicleModel model) {
        this.model = model;
    }

    public VehicleModel getModel() {
        return model;
    }

    public void setModel(VehicleModel model) {
        this.model = model;
    }
}
