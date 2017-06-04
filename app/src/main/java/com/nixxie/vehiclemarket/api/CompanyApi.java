package com.nixxie.vehiclemarket.api;

import com.nixxie.vehiclemarket.BuildConfig;
import com.nixxie.vehiclemarketmvc.mvp.model.ApiResponseWrapper;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface CompanyApi {
    @GET(BuildConfig.manufacturersEndpoint)
    Single<ApiResponseWrapper> getManufacturers(@Query("page") int page, @Query("pageSize") int pageSize);

    @GET(BuildConfig.mainTypeEndpoint)
    Single<ApiResponseWrapper> getBrandModels(@Query("manufacturer") int manufacturerId, @Query("page") int page, @Query("pageSize") int pageSize);

    @GET(BuildConfig.builtDatesEndpoint)
    Single<ApiResponseWrapper> getModelReleaseDates(@Query("manufacturer") int manufacturerId, @Query("main-type") String model);
}
