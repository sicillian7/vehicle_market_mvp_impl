package com.nixxie.vehiclemarketmvc.mvp.presenter;

import android.util.Log;

import com.nixxie.vehiclemarketmvc.domain.FetchBuiltDatesUseCase;
import com.nixxie.vehiclemarketmvc.domain.FetchManufacturerModelsUseCase;
import com.nixxie.vehiclemarketmvc.domain.FetchManufacturersUseCase;
import com.nixxie.vehiclemarketmvc.mvp.model.ApiResponseWrapper;
import com.nixxie.vehiclemarketmvc.mvp.model.BuiltDateModel;
import com.nixxie.vehiclemarketmvc.mvp.model.Manufacturer;
import com.nixxie.vehiclemarketmvc.mvp.model.VehicleModel;
import com.nixxie.vehiclemarketmvc.mvp.view.VehiclesViewContract;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by nikolahristovski on 5/29/17.
 */

public class VehicleSelectorPresenter implements BasePresenter<VehiclesViewContract> {

    private static final String TAG = VehicleSelectorPresenter.class.getName();
    private static final int PAGE_SIZE = 15;
    private int manufacturerPage = 0, manufacturersTotalPageCount = 1;

    private FetchManufacturersUseCase fetchManufacturersUseCase;
    private FetchManufacturerModelsUseCase fetchManufacturerModelsUseCase;
    private FetchBuiltDatesUseCase fetchBuiltDatesUseCase;

    private List<Disposable> lsDisposables = new ArrayList<>();
    private WeakReference<VehiclesViewContract> weakView;
    private VehiclesViewContract mView;


    public VehicleSelectorPresenter(FetchManufacturersUseCase fetchManufacturersUseCase,
                                    FetchManufacturerModelsUseCase fetchManufacturerModelsUseCase,
                                    FetchBuiltDatesUseCase fetchBuiltDatesUseCase) {
        this.fetchManufacturersUseCase = fetchManufacturersUseCase;
        this.fetchManufacturerModelsUseCase = fetchManufacturerModelsUseCase;
        this.fetchBuiltDatesUseCase = fetchBuiltDatesUseCase;
        Log.d("VEHICLES MODULE", "presenter instantiated");
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        for (Disposable d : lsDisposables) {
            d.dispose();
        }
    }

    @Override
    public void attachView(VehiclesViewContract view) {
        weakView = new WeakReference<VehiclesViewContract>(view);
        mView = weakView.get();
    }

    @Override
    public void detachView() {

    }

    public void loadManufacturers(){
        if((manufacturerPage < manufacturersTotalPageCount)){
            Log.d(TAG, "loading data with page = " + manufacturerPage);
            if (mView != null) {
                mView.displayProgressDialog(true);
            }
            fetchManufacturersUseCase.execute(manufacturerPage, PAGE_SIZE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new FetchManufacturerUseCaseHandler(weakView, this));
        }else{
            if (mView != null) {
                mView.displayProgressDialog(false);
            }
        }
    }

    public void loadVehicleModels(int manufacturerId){
        fetchManufacturerModelsUseCase.execute(manufacturerId, 0, PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FetchManufacturerModelsUseCaseHandler(weakView, this));
    }


    public void loadBuiltDates(int manufacturerId, String model){
        fetchBuiltDatesUseCase.execute(manufacturerId, model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FetchBuiltDatesUseCaseHandler(weakView, this));
    }

    public static class FetchManufacturerUseCaseHandler implements SingleObserver<ApiResponseWrapper> {
        private WeakReference<VehiclesViewContract> weakViewRef;
        private WeakReference<VehicleSelectorPresenter> weakPresenterRef;

        public FetchManufacturerUseCaseHandler(WeakReference<VehiclesViewContract> weakViewRef, VehicleSelectorPresenter presenter) {
            this.weakViewRef = weakViewRef;
            weakPresenterRef = new WeakReference<VehicleSelectorPresenter>(presenter);
        }


        @Override
        public void onSubscribe(Disposable d) {
            VehicleSelectorPresenter p = weakPresenterRef.get();
            if (p != null) {
                p.lsDisposables.add(d);
            }
        }

        @Override
        public void onSuccess(ApiResponseWrapper apiResponseWrapper) {
            VehiclesViewContract v = weakViewRef.get();
            VehicleSelectorPresenter p = weakPresenterRef.get();

            if (v != null && p != null) {
                if (apiResponseWrapper != null) {
                    p.manufacturersTotalPageCount = apiResponseWrapper.getTotalPageCount();
                    if(p.manufacturerPage < p.manufacturersTotalPageCount){
                        p.manufacturerPage++;
                    }

                    if (apiResponseWrapper.getResult() != null) {
                        List<Manufacturer> lsItems = new ArrayList<>();
                        for (Map.Entry<String, String> entry : apiResponseWrapper.getResult().entrySet()) {
                            lsItems.add(new Manufacturer(entry.getKey(), entry.getValue()));
                        }
                        Log.d(TAG, "data loaded");
                        v.onManufacturersLoaded(lsItems);
                        v.displayProgressDialog(false);
                    }
                }
            }
        }

        @Override
        public void onError(Throwable e) {
            VehiclesViewContract v = weakViewRef.get();
            if (v != null) {
                v.displayError(e.getMessage());
            }
        }
    }


    public static final class FetchManufacturerModelsUseCaseHandler extends FetchManufacturerUseCaseHandler implements SingleObserver<ApiResponseWrapper>{

        public FetchManufacturerModelsUseCaseHandler(WeakReference<VehiclesViewContract> weakViewRef, VehicleSelectorPresenter presenter) {
            super(weakViewRef, presenter);
        }

        @Override
        public void onSuccess(ApiResponseWrapper apiResponseWrapper) {
            VehiclesViewContract v = super.weakViewRef.get();

            if (v != null) {
                if (apiResponseWrapper != null) {
                    if (apiResponseWrapper.getResult() != null) {
                        List<VehicleModel> lsItems = new ArrayList<>();
                        for (Map.Entry<String, String> entry : apiResponseWrapper.getResult().entrySet()) {
                            lsItems.add(new VehicleModel(entry.getKey(), entry.getValue()));
                        }

                        v.onVehicleModelsLoaded(lsItems);
                        v.displayProgressDialog(false);
                    }
                }
            }
        }
    }


    public static class FetchBuiltDatesUseCaseHandler extends FetchManufacturerUseCaseHandler implements SingleObserver<ApiResponseWrapper>{


        public FetchBuiltDatesUseCaseHandler(WeakReference<VehiclesViewContract> weakViewRef, VehicleSelectorPresenter presenter) {
            super(weakViewRef, presenter);
        }

        @Override
        public void onSuccess(ApiResponseWrapper apiResponseWrapper) {
            VehiclesViewContract v = super.weakViewRef.get();

            if (v != null) {
                if (apiResponseWrapper != null) {
                    if (apiResponseWrapper.getResult() != null) {
                        List<BuiltDateModel> lsItems = new ArrayList<>();
                        for (Map.Entry<String, String> entry : apiResponseWrapper.getResult().entrySet()) {
                            lsItems.add(new BuiltDateModel(entry.getKey(), entry.getValue()));
                        }

                        v.onBuiltDatesLoaded(lsItems);
                        v.displayProgressDialog(false);
                    }
                }
            }
        }
    }
}
