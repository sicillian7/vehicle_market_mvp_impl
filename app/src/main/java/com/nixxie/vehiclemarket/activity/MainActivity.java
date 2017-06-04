package com.nixxie.vehiclemarket.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;
import android.widget.Toast;

import com.nixxie.vehiclemarket.AppConstants;
import com.nixxie.vehiclemarket.R;
import com.nixxie.vehiclemarket.base.BaseActivity;
import com.nixxie.vehiclemarket.busEvents.SelectedBuiltDate;
import com.nixxie.vehiclemarket.busEvents.SelectedManufacturerEvent;
import com.nixxie.vehiclemarket.busEvents.SelectedModelEvent;
import com.nixxie.vehiclemarket.customViews.SelectionDetailsView;
import com.nixxie.vehiclemarket.fragment.BuiltDatesFragment;
import com.nixxie.vehiclemarket.fragment.ManufacturerModelsFragment;
import com.nixxie.vehiclemarket.fragment.ManufacturersFragment;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity{



    @BindView(R.id.selection_details_view)
    SelectionDetailsView selectionDetailsView;
    @BindView(R.id.btn_choose)
    TextView btnChoose;

    private ManufacturersFragment manufacturersFragment;
    private ManufacturerModelsFragment modelsFragment;
    private BuiltDatesFragment datesFragment;

    private String activeFragmentTag;

    @Override
    protected void onResume() {
        super.onResume();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if(savedInstanceState != null){
            String fragmentTag = savedInstanceState.getString(AppConstants.ACTIVE_FRAGMENT, AppConstants.MANUFACTURERS_FRAGMENT_TAG);

            switch (fragmentTag){
                case AppConstants.MANUFACTURERS_FRAGMENT_TAG:
                    manufacturersFragment = (ManufacturersFragment)getSupportFragmentManager().findFragmentByTag(AppConstants.MANUFACTURERS_FRAGMENT_TAG);
                    ft.replace(R.id.container,manufacturersFragment,AppConstants.MANUFACTURERS_FRAGMENT_TAG);
                    ft.commit();
                    activeFragmentTag = AppConstants.MANUFACTURERS_FRAGMENT_TAG;
                    btnChoose.setText(getResources().getString(R.string.next));
                    break;

                case AppConstants.MODELS_FRAGMENT_TAG:
                    modelsFragment = (ManufacturerModelsFragment)getSupportFragmentManager().findFragmentByTag(AppConstants.MODELS_FRAGMENT_TAG);
                    ft.replace(R.id.container, modelsFragment, AppConstants.MODELS_FRAGMENT_TAG);
                    ft.commit();
                    activeFragmentTag = AppConstants.MODELS_FRAGMENT_TAG;

                    btnChoose.setText(getResources().getString(R.string.next));
                    break;

                case AppConstants.BUILT_DATES_FRAGMENT_TAG:
                    datesFragment = (BuiltDatesFragment)getSupportFragmentManager().findFragmentByTag(AppConstants.BUILT_DATES_FRAGMENT_TAG);
                    ft.replace(R.id.container, datesFragment, AppConstants.BUILT_DATES_FRAGMENT_TAG);
                    ft.commit();
                    activeFragmentTag = AppConstants.BUILT_DATES_FRAGMENT_TAG;

                    btnChoose.setText(getResources().getString(R.string.summary));
                    break;
            }
            SelectedManufacturerEvent selectedManufacturerEvent = EventBus.getDefault().getStickyEvent(SelectedManufacturerEvent.class);
            SelectedModelEvent selectedModelEvent = EventBus.getDefault().getStickyEvent(SelectedModelEvent.class);
            SelectedBuiltDate selectedDateEvent = EventBus.getDefault().getStickyEvent(SelectedBuiltDate.class);

            if (selectedManufacturerEvent != null) {
                selectionDetailsView.setManufacturer(selectedManufacturerEvent.getManufacturer().getName());
            }

            if (selectedModelEvent != null) {
                selectionDetailsView.setModel(selectedModelEvent.getModel().getName());
            }

            if (selectedDateEvent != null) {
                selectionDetailsView.setYear(selectedDateEvent.getDate().getName());
            }

        }else{
            manufacturersFragment = ManufacturersFragment.getInstance();
            ft.add(R.id.container,manufacturersFragment,AppConstants.MANUFACTURERS_FRAGMENT_TAG);
            ft.commit();
            activeFragmentTag = AppConstants.MANUFACTURERS_FRAGMENT_TAG;
        }
    }

    @Override
    protected void resolveDaggerDependencies() {
        super.resolveDaggerDependencies();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    public void onEventMainThread(SelectedManufacturerEvent e){
        if (e.getManufacturer() != null) {
            selectionDetailsView.setManufacturer(e.getManufacturer().getName());
        }
    }

    public void onEventMainThread(SelectedModelEvent e){
        if (e.getModel() != null) {
            selectionDetailsView.setModel(e.getModel().getName());
        }
    }

    public void onEventMainThread(SelectedBuiltDate e){
        if (e.getDate() != null) {
            selectionDetailsView.setYear(e.getDate().getName());
        }
    }

    @OnClick(R.id.btn_choose)
    void onChooseClick(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        SelectedManufacturerEvent selectedManufacturer = EventBus.getDefault().getStickyEvent(SelectedManufacturerEvent.class);
        SelectedModelEvent selectedModel = EventBus.getDefault().getStickyEvent(SelectedModelEvent.class);
        SelectedBuiltDate selectedDate = EventBus.getDefault().getStickyEvent(SelectedBuiltDate.class);

        switch (activeFragmentTag){
            case AppConstants.MANUFACTURERS_FRAGMENT_TAG:

                if (selectedManufacturer != null) {
                    ft.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim,
                            R.anim.exit_anim, R.anim.enter_anim);
                    ft.replace(R.id.container, ManufacturerModelsFragment.getInstance(), AppConstants.MODELS_FRAGMENT_TAG);
                    ft.commit();
                    activeFragmentTag = AppConstants.MODELS_FRAGMENT_TAG;
                }else{
                    Toast.makeText(this, getResources().getString(R.string.select_manufacturer_msg), Toast.LENGTH_SHORT).show();
                }
                break;

            case AppConstants.MODELS_FRAGMENT_TAG:


                if(selectedManufacturer != null && selectedModel != null){
                    ft.setCustomAnimations(R.anim.enter_anim, R.anim.exit_anim,
                            R.anim.exit_anim, R.anim.enter_anim);
                    ft.replace(R.id.container, BuiltDatesFragment.getInstance(), AppConstants.BUILT_DATES_FRAGMENT_TAG);
                    ft.commit();
                    activeFragmentTag = AppConstants.BUILT_DATES_FRAGMENT_TAG;
                    btnChoose.setText(getResources().getString(R.string.summary));
                }else{
                    Toast.makeText(this, getResources().getString(R.string.select_model_msg), Toast.LENGTH_SHORT).show();
                }
                break;

            case AppConstants.BUILT_DATES_FRAGMENT_TAG:
                if (selectedDate != null) {
                    EventBus.getDefault().removeStickyEvent(SelectedManufacturerEvent.class);
                    EventBus.getDefault().removeStickyEvent(SelectedModelEvent.class);
                    EventBus.getDefault().removeStickyEvent(SelectedBuiltDate.class);

                    Intent i = new Intent(MainActivity.this, SummaryActivity.class);
                    i.putExtra(AppConstants.MANUFACTURER, selectedManufacturer.getManufacturer().getName());
                    i.putExtra(AppConstants.MODEL, selectedModel.getModel().getName());
                    i.putExtra(AppConstants.BUILT_DATE, selectedDate.getDate().getName());
                    startActivity(i);
                }else{
                    Toast.makeText(this, getResources().getString(R.string.select_built_date_msg), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AppConstants.ACTIVE_FRAGMENT, activeFragmentTag);
    }
}