package com.nixxie.vehiclemarketmvc.mvp.view;

import com.nixxie.vehiclemarketmvc.mvp.model.BuiltDateModel;
import com.nixxie.vehiclemarketmvc.mvp.model.Manufacturer;
import com.nixxie.vehiclemarketmvc.mvp.model.VehicleModel;

import java.util.List;

/**
 * Created by nikolahristovski on 5/29/17.
 */

public interface VehiclesViewContract extends BaseView{

    void onManufacturersLoaded(List<Manufacturer> lsManufacturers);
    void onVehicleModelsLoaded(List<VehicleModel> lsVehicleModels);
    void onBuiltDatesLoaded(List<BuiltDateModel> lsBuiltDates);
}
