package com.nixxie.vehiclemarket.utils;

import android.content.Context;
import android.support.v4.content.Loader;

import com.nixxie.vehiclemarket.base.PresenterFactory;
import com.nixxie.vehiclemarketmvc.mvp.presenter.BasePresenter;
import com.nixxie.vehiclemarketmvc.mvp.view.BaseView;

import javax.inject.Inject;

/**
 * Created by nikolahristovski on 6/2/17.
 */

public class PresenterLoader <T extends BasePresenter> extends Loader<T> {
    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */

    private final PresenterFactory<T> factory;
    private T presenter;

    public PresenterLoader(Context context, PresenterFactory<T> factory) {
        super(context);
        this.factory = factory;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }
        forceLoad();
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
        presenter = factory.create();
        deliverResult(presenter);
    }


    @Override
    public void deliverResult(T data) {
        super.deliverResult(data);
    }

    @Override
    protected void onStopLoading() {
    }

    @Override
    protected void onReset() {
        if (presenter != null) {
            presenter.onDestroy();
            presenter = null;
        }
    }

    public T getPresenter() {
        return presenter;
    }

}
