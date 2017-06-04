package com.nixxie.vehiclemarket.di.modules;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.nixxie.vehiclemarket.adapters.DataAdapter;
import com.nixxie.vehiclemarket.di.scopes.PerFragment;
import com.nixxie.vehiclemarket.utils.DividerItemDecoration;
import com.nixxie.vehiclemarket.utils.VehiclePresenterFactory;
import com.nixxie.vehiclemarketmvc.domain.FetchBuiltDatesUseCase;
import com.nixxie.vehiclemarketmvc.domain.FetchManufacturerModelsUseCase;
import com.nixxie.vehiclemarketmvc.domain.FetchManufacturersUseCase;
import com.nixxie.vehiclemarketmvc.mvp.model.BuiltDateModel;
import com.nixxie.vehiclemarketmvc.mvp.model.Manufacturer;
import com.nixxie.vehiclemarketmvc.mvp.model.VehicleModel;
import com.nixxie.vehiclemarketmvc.mvp.presenter.VehicleSelectorPresenter;
import com.nixxie.vehiclemarketmvc.network.VehicleRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class VehiclesModule {

    private static final int VEHICLE_PRESENTER_ID = 1091;
    @PerFragment
    @Provides
    FetchManufacturersUseCase provideManufacturersUseCase(VehicleRepository repo){
        return new FetchManufacturersUseCase(repo);
    }

    @PerFragment
    @Provides
    FetchManufacturerModelsUseCase provideManufacturersModelsUseCase(VehicleRepository repo){
        return new FetchManufacturerModelsUseCase(repo);
    }

    @PerFragment
    @Provides
    FetchBuiltDatesUseCase provideBuiltDatesUseCase(VehicleRepository repo){
        return new FetchBuiltDatesUseCase(repo);
    }
    @PerFragment
    @Provides
    VehicleSelectorPresenter provideVehicleSelectorPresenter(FetchManufacturersUseCase manufacturersUseCase,
                                                             FetchManufacturerModelsUseCase manufacturerModelsUseCase,
                                                             FetchBuiltDatesUseCase builtDatesUseCase){


        /*
        VehicleSelectorPresenter presenter = (VehicleSelectorPresenter) context.getPresenterCacher().getCachedPresenter(VEHICLE_PRESENTER_ID);
        if(presenter != null){
            return presenter;
        }*/
        VehicleSelectorPresenter p = new VehicleSelectorPresenter(manufacturersUseCase, manufacturerModelsUseCase, builtDatesUseCase);
        //context.getPresenterCacher().addPresenter(p, VEHICLE_PRESENTER_ID);

        return p;
    }

    @PerFragment
    @Provides
    VehiclePresenterFactory provideVehiclePresenterFactory(VehicleSelectorPresenter p){

        return new VehiclePresenterFactory(p);
    }

    @Provides
    LinearLayoutManager providesLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }


    @PerFragment
    @Provides
    DataAdapter<Manufacturer> providesManufacturersAdapter(){
        return new DataAdapter<Manufacturer>();
    }

    @PerFragment
    @Provides
    DataAdapter<VehicleModel> providesManufacturerModelsAdapter(){
        return new DataAdapter<VehicleModel>();
    }

    @PerFragment
    @Provides
    DataAdapter<BuiltDateModel> providesBuiltDatesAdapter(){
        return new DataAdapter<BuiltDateModel>();
    }

    @PerFragment
    @Provides
    DividerItemDecoration providesItemDecorator(Context context){
        return new DividerItemDecoration(context);
    }
}
