package com.nixxie.vehiclemarket.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nixxie.vehiclemarket.R;
import com.nixxie.vehiclemarket.VehicleMarketApplication;
import com.nixxie.vehiclemarket.adapters.DataAdapter;
import com.nixxie.vehiclemarket.busEvents.SelectedManufacturerEvent;
import com.nixxie.vehiclemarket.busEvents.SelectedModelEvent;
import com.nixxie.vehiclemarket.di.components.DaggerVehiclesComponent;
import com.nixxie.vehiclemarket.di.components.VehiclesComponent;
import com.nixxie.vehiclemarket.di.modules.ActivityModule;
import com.nixxie.vehiclemarket.di.modules.VehiclesModule;
import com.nixxie.vehiclemarket.utils.DividerItemDecoration;
import com.nixxie.vehiclemarket.utils.PresenterLoader;
import com.nixxie.vehiclemarket.utils.VehiclePresenterFactory;
import com.nixxie.vehiclemarketmvc.mvp.model.BuiltDateModel;
import com.nixxie.vehiclemarketmvc.mvp.model.Manufacturer;
import com.nixxie.vehiclemarketmvc.mvp.model.VehicleModel;
import com.nixxie.vehiclemarketmvc.mvp.presenter.VehicleSelectorPresenter;
import com.nixxie.vehiclemarketmvc.mvp.view.VehiclesViewContract;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

public class BuiltDatesFragment extends Fragment implements VehiclesViewContract{

    private static final String TAG = BuiltDatesFragment.class.getName();
    private static final int LOADER_ID = 1005;

    @BindView(R.id.built_dates_rv)
    RecyclerView rv;

    @Inject
    DividerItemDecoration itemDecoration;
    @Inject
    DataAdapter<BuiltDateModel> adapter;
    @Inject
    VehiclePresenterFactory presenterFactory;

    private VehicleSelectorPresenter presenter;
    private VehiclesComponent vehiclesComponent;
    private int manufacturerId;
    private String modelName;

    Unbinder unbinder;

    public BuiltDatesFragment() {
    }

    public static BuiltDatesFragment getInstance(){
        return new BuiltDatesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        vehiclesComponent = DaggerVehiclesComponent.builder()
                .applicationComponent(((VehicleMarketApplication) getActivity().getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule((AppCompatActivity) getActivity()))
                .vehiclesModule(new VehiclesModule())
                .build();
        vehiclesComponent.inject(this);

        SelectedManufacturerEvent m = EventBus.getDefault().getStickyEvent(SelectedManufacturerEvent.class);
        if (m != null) {
            manufacturerId = Integer.valueOf(m.getManufacturer().getId());
        }

        SelectedModelEvent m1 = EventBus.getDefault().getStickyEvent(SelectedModelEvent.class);
        if (m1 != null) {
            modelName = m1.getModel().getName();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_built_dates, container, false);
        unbinder = ButterKnife.bind(this, view);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.addItemDecoration(itemDecoration);
        rv.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Loader<VehicleSelectorPresenter> loader = getLoaderManager().getLoader(LOADER_ID);
        if (loader == null) {
            initLoader();
        } else {
            this.presenter = ((PresenterLoader<VehicleSelectorPresenter>) loader).getPresenter();
            onPresenterCreatedOrRestored(presenter);
        }
    }

    private void initLoader() {
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<VehicleSelectorPresenter>() {
            @Override
            public final Loader<VehicleSelectorPresenter> onCreateLoader(int id, Bundle args) {
                return new PresenterLoader<>(getContext(), presenterFactory);
            }

            @Override
            public final void onLoadFinished(Loader<VehicleSelectorPresenter> loader, VehicleSelectorPresenter presenter) {
                BuiltDatesFragment.this.presenter = presenter;
                onPresenterCreatedOrRestored(presenter);
            }

            @Override
            public final void onLoaderReset(Loader<VehicleSelectorPresenter> loader) {
                BuiltDatesFragment.this.presenter = null;
            }
        });
    }

    protected void onPresenterCreatedOrRestored(@NonNull VehicleSelectorPresenter presenter) {
        this.presenter = presenter;
        this.presenter.attachView(this);
        if(adapter.getItemCount() == 0){
            this.presenter.loadBuiltDates(manufacturerId, modelName);
        }
    }

    @Override
    public void displayError(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayProgressDialog(boolean isVisible) {

    }

    @Override
    public void onManufacturersLoaded(List<Manufacturer> lsManufacturers) {

    }

    @Override
    public void onVehicleModelsLoaded(List<VehicleModel> lsVehicleModels) {

    }

    @Override
    public void onBuiltDatesLoaded(List<BuiltDateModel> lsBuiltDates) {
        if (adapter != null) {
            adapter.notifyDataChanged(lsBuiltDates);
        }
    }
}
