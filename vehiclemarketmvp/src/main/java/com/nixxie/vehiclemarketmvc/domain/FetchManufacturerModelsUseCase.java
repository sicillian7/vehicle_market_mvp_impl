package com.nixxie.vehiclemarketmvc.domain;

import com.nixxie.vehiclemarketmvc.mvp.model.ApiResponseWrapper;
import com.nixxie.vehiclemarketmvc.network.VehicleRepository;

import io.reactivex.Single;

/**
 * Created by nikolahristovski on 5/31/17.
 */

public class FetchManufacturerModelsUseCase {

    private VehicleRepository repo;

    public FetchManufacturerModelsUseCase(VehicleRepository repo) {
        this.repo = repo;
    }

    public Single<ApiResponseWrapper> execute(int manufacturerId, int page, int pageSize){
        return repo.getBrandModels(manufacturerId, page, pageSize);
    }
}
