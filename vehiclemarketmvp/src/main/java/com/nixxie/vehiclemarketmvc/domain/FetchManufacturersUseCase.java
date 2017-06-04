package com.nixxie.vehiclemarketmvc.domain;

import com.nixxie.vehiclemarketmvc.mvp.model.ApiResponseWrapper;
import com.nixxie.vehiclemarketmvc.network.VehicleRepository;

import io.reactivex.Single;

/**
 * Created by nikolahristovski on 5/30/17.
 */

public class FetchManufacturersUseCase {

    private VehicleRepository repo;

    public FetchManufacturersUseCase(VehicleRepository repo) {
        this.repo = repo;
    }

    public Single<ApiResponseWrapper> execute(int page, int pageSize){
        return repo.getManufacturers(page, pageSize);
    }
}
