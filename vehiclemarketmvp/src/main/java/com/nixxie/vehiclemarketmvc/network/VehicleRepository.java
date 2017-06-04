package com.nixxie.vehiclemarketmvc.network;

import com.nixxie.vehiclemarketmvc.mvp.model.ApiResponseWrapper;

import io.reactivex.Single;

/**
 * Created by nikolahristovski on 5/29/17.
 */

public interface VehicleRepository {

    Single<ApiResponseWrapper> getManufacturers(int page, int pageSize);
    Single<ApiResponseWrapper> getBrandModels(int manufacturerId, int page, int pageSize);
    Single<ApiResponseWrapper> getModelReleaseDates(int manufacturerId, String model);
}
