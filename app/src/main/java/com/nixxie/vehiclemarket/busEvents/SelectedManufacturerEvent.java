package com.nixxie.vehiclemarket.busEvents;

import com.nixxie.vehiclemarketmvc.mvp.model.Manufacturer;

/**
 * Created by nikolahristovski on 6/3/17.
 */

public class SelectedManufacturerEvent {

    private Manufacturer manufacturer;

    public SelectedManufacturerEvent(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }
}
