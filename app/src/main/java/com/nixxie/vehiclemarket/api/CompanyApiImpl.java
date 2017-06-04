package com.nixxie.vehiclemarket.api;

import com.nixxie.vehiclemarketmvc.mvp.model.ApiResponseWrapper;
import com.nixxie.vehiclemarketmvc.network.VehicleRepository;

import io.reactivex.Single;
import retrofit2.Retrofit;

public class CompanyApiImpl implements VehicleRepository {

    private CompanyApi apiService;

    public CompanyApiImpl(Retrofit retrofit) {
        apiService = retrofit.create(CompanyApi.class);
    }

    @Override
    public Single<ApiResponseWrapper> getManufacturers(int page, int pageSize) {
        return apiService.getManufacturers(page,pageSize);
    }

    @Override
    public Single<ApiResponseWrapper> getBrandModels(int manufacturerId, int page, int pageSize) {
        return apiService.getBrandModels(manufacturerId, page, pageSize);
    }

    @Override
    public Single<ApiResponseWrapper> getModelReleaseDates(int manufacturerId, String model) {
        return apiService.getModelReleaseDates(manufacturerId, model);
    }
}
