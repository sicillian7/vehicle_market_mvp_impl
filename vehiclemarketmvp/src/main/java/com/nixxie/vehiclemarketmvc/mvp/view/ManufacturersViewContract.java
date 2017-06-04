package com.nixxie.vehiclemarketmvc.mvp.view;

import com.nixxie.vehiclemarketmvc.mvp.model.Manufacturer;

import java.util.List;

/**
 * Created by nikolahristovski on 6/2/17.
 */

public interface ManufacturersViewContract {
    void onManufacturersLoaded(List<Manufacturer> lsManufacturers);
}
