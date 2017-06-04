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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.nixxie.vehiclemarket.R;
import com.nixxie.vehiclemarket.VehicleMarketApplication;
import com.nixxie.vehiclemarket.adapters.DataAdapter;
import com.nixxie.vehiclemarket.busEvents.SelectedManufacturerEvent;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;

public class ManufacturerModelsFragment extends Fragment implements VehiclesViewContract{

    private static final String TAG = ManufacturerModelsFragment.class.getName();
    private static final int LOADER_ID = 1003;

    @BindView(R.id.search)
    EditText edtSearch;
    @BindView(R.id.manufacturer_models_rv)
    RecyclerView rv;

    @Inject
    DividerItemDecoration itemDecoration;

    @Inject
    DataAdapter<VehicleModel> adapter;
    @Inject
    VehiclePresenterFactory presenterFactory;

    private VehicleSelectorPresenter presenter;
    private VehiclesComponent vehiclesComponent;
    private int manufacturerId;

    Unbinder unbinder;


    public ManufacturerModelsFragment() {
    }

    public static ManufacturerModelsFragment getInstance(){
        return new ManufacturerModelsFragment();
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

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manufacturer_models, container, false);
        unbinder = ButterKnife.bind(this, view);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.addItemDecoration(itemDecoration);
        rv.setAdapter(adapter);
        edtSearch.addTextChangedListener(new SearchTextWatcher(this));
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
        if (adapter != null) {
            adapter.notifyDataChanged(lsVehicleModels);
        }
    }

    @Override
    public void onBuiltDatesLoaded(List<BuiltDateModel> lsBuiltDates) {

    }

    private void initLoader() {
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<VehicleSelectorPresenter>() {
            @Override
            public final Loader<VehicleSelectorPresenter> onCreateLoader(int id, Bundle args) {
                return new PresenterLoader<>(getContext(), presenterFactory);
            }

            @Override
            public final void onLoadFinished(Loader<VehicleSelectorPresenter> loader, VehicleSelectorPresenter presenter) {
                ManufacturerModelsFragment.this.presenter = presenter;
                onPresenterCreatedOrRestored(presenter);
            }

            @Override
            public final void onLoaderReset(Loader<VehicleSelectorPresenter> loader) {
                ManufacturerModelsFragment.this.presenter = null;
            }
        });
    }

    protected void onPresenterCreatedOrRestored(@NonNull VehicleSelectorPresenter presenter) {
        this.presenter = presenter;
        this.presenter.attachView(this);
        if(adapter.getItemCount() == 0){
            this.presenter.loadVehicleModels(manufacturerId);
        }
    }



    private void filterModels(String query){
        Log.d(TAG, "filterModels query=" + query);
        List<VehicleModel> filteredContacts = new ArrayList<>();
        for (VehicleModel model : adapter.getLsItems()) {
            String name = model.getName();
            if (name.toLowerCase().contains(query.toLowerCase())) {
                Log.d(TAG, "adding model= " + model.getName());
                filteredContacts.add(model);
            }
        }

        if (!query.equals("")) {
            Log.d(TAG, "list refreshing with new filtered models");
            adapter.refreshDataList(filteredContacts);
        } else {
            Log.d(TAG, "list refreshing with old models");
            adapter.refreshDataList(adapter.getLsItems());
        }

    }


    public static class SearchTextWatcher implements TextWatcher {
        WeakReference<ManufacturerModelsFragment> handlerWeakReference;

        public SearchTextWatcher(ManufacturerModelsFragment f) {
            this.handlerWeakReference = new WeakReference<>(f);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(final Editable s) {
            ManufacturerModelsFragment f = handlerWeakReference.get();
            if (f != null) {
                f.filterModels(s.toString().trim());
            }
        }
    }
}
