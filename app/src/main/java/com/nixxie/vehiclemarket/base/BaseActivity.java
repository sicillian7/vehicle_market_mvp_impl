package com.nixxie.vehiclemarket.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by nikolahristovski on 4/17/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mDialog;
    protected Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        unbinder = ButterKnife.bind(this);
        onViewReady(savedInstanceState, getIntent());
    }

    protected abstract int getContentView();

    @CallSuper
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        // to be used by child classes
        resolveDaggerDependencies();
    }

    protected void resolveDaggerDependencies() {
    }

    protected void showDialog(String message, boolean isVisible){
        if(isVisible){
            if(mDialog == null){
                mDialog = new ProgressDialog(this);
                mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mDialog.setCancelable(true);
            }
            mDialog.setMessage(message);
            mDialog.show();
        }else{
            if(mDialog != null && mDialog.isShowing()){
                mDialog.dismiss();
            }
        }
    }


    /*

    public ApplicationComponent getApplicationComponent(){
        return ((MVPUtubeApplication) getApplication()).getApplicationComponent();
    } */

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
