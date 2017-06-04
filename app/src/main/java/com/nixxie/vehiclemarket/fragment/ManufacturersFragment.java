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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nixxie.vehiclemarket.R;
import com.nixxie.vehiclemarket.VehicleMarketApplication;
import com.nixxie.vehiclemarket.adapters.DataAdapter;
import com.nixxie.vehiclemarket.di.components.DaggerVehiclesComponent;
import com.nixxie.vehiclemarket.di.components.VehiclesComponent;
import com.nixxie.vehiclemarket.di.modules.ActivityModule;
import com.nixxie.vehiclemarket.di.modules.VehiclesModule;
import com.nixxie.vehiclemarket.utils.DividerItemDecoration;
import com.nixxie.vehiclemarket.utils.EndlessRecyclerViewScrollListener;
import com.nixxie.vehiclemarket.utils.PresenterLoader;
import com.nixxie.vehiclemarket.utils.VehiclePresenterFactory;
import com.nixxie.vehiclemarketmvc.mvp.model.BuiltDateModel;
import com.nixxie.vehiclemarketmvc.mvp.model.Manufacturer;
import com.nixxie.vehiclemarketmvc.mvp.model.VehicleModel;
import com.nixxie.vehiclemarketmvc.mvp.presenter.VehicleSelectorPresenter;
import com.nixxie.vehiclemarketmvc.mvp.view.VehiclesViewContract;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ManufacturersFragment extends Fragment implements VehiclesViewContract{

    private static final String TAG = ManufacturersFragment.class.getName();
    private static final int LOADER_ID = 1001;

    public ManufacturersFragment() {
    }

    public static ManufacturersFragment getInstance(){
        return new ManufacturersFragment();
    }

    @BindView(R.id.manufacturers_rv)
    RecyclerView rv;
    @BindView(R.id.progress_bar)
    ProgressBar pBar;


    @Inject
    DividerItemDecoration itemDecoration;
    @Inject
    DataAdapter<Manufacturer> adapter;
    @Inject
    VehiclePresenterFactory presenterFactory;

    private VehicleSelectorPresenter presenter;
    private VehiclesComponent vehiclesComponent;

    Unbinder unbinder;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manufacturers, container, false);
        unbinder = ButterKnife.bind(this, view);
        displayProgressDialog(false);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void displayError(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayProgressDialog(boolean isVisible) {
        if (isVisible) {
            pBar.setVisibility(View.VISIBLE);
        }else {
            pBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onManufacturersLoaded(List<Manufacturer> lsManufacturers) {
        if (adapter != null) {
            adapter.notifyDataChanged(lsManufacturers);
        }
    }

    @Override
    public void onVehicleModelsLoaded(List<VehicleModel> lsVehicleModels) {

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
                ManufacturersFragment.this.presenter = presenter;
                onPresenterCreatedOrRestored(presenter);
            }

            @Override
            public final void onLoaderReset(Loader<VehicleSelectorPresenter> loader) {
                ManufacturersFragment.this.presenter = null;
            }
        });
    }

    protected void onPresenterCreatedOrRestored(@NonNull VehicleSelectorPresenter presenter) {
        this.presenter = presenter;
        this.presenter.attachView(this);
        if(adapter.getItemCount() == 0){
            this.presenter.loadManufacturers();
        }
        rv.addOnScrollListener(new EndlessRecyclerViewScrollListenerImp((LinearLayoutManager) rv.getLayoutManager()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    private class EndlessRecyclerViewScrollListenerImp extends EndlessRecyclerViewScrollListener {

        private WeakReference<VehicleSelectorPresenter> weakPresenter;

        public EndlessRecyclerViewScrollListenerImp(LinearLayoutManager layoutManager) {
            super(layoutManager);
            weakPresenter = new WeakReference<VehicleSelectorPresenter>(presenter);
        }

        @Override
        public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            VehicleSelectorPresenter p = weakPresenter.get();
            if (p != null) {
                Log.d(TAG, "onLoadMore");
                p.loadManufacturers();
            }
        }
    }
}
