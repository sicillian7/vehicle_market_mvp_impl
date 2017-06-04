package com.nixxie.vehiclemarketmvc.mvp.presenter;

import com.nixxie.vehiclemarketmvc.mvp.view.BaseView;

/**
 * Created by nikolahristovski on 4/17/17.
 */

public interface BasePresenter<T extends BaseView> {

    void onStart();

    void onStop();

    void onPause();

    void onResume();

    void onDestroy();

    void attachView(T view);

    void detachView();
}
