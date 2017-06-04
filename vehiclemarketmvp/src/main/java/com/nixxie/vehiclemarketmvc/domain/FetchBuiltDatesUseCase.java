package com.nixxie.vehiclemarketmvc.domain;

import com.nixxie.vehiclemarketmvc.mvp.model.ApiResponseWrapper;
import com.nixxie.vehiclemarketmvc.network.VehicleRepository;

import io.reactivex.Single;

/**
 * Created by nikolahristovski on 5/31/17.
 */

public class FetchBuiltDatesUseCase {

    private VehicleRepository repo;

    public FetchBuiltDatesUseCase(VehicleRepository repo) {
        this.repo = repo;
    }

    public Single<ApiResponseWrapper> execute(int manufacturerId, String model){
        return repo.getModelReleaseDates(manufacturerId, model);
    }
}
