package com.nixxie.vehiclemarket.utils;

import com.nixxie.vehiclemarketmvc.mvp.presenter.BasePresenter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nikolahristovski on 6/2/17.
 */

public class PresentersCacher {

    private static PresentersCacher instance;
    private Map<Integer, BasePresenter> cachedPresenters = new HashMap<>();

    private PresentersCacher(){
    }

    public static PresentersCacher getInstance(){
        if (instance == null) {
            return new PresentersCacher();
        }
        return instance;
    }

    public void addPresenter(BasePresenter p, int id){
        cachedPresenters.put(id, p);
    }

    public void removePresenter(int id){
        Iterator<Map.Entry<Integer, BasePresenter>> iterator = cachedPresenters.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<Integer, BasePresenter> entry = iterator.next();
            if(id == entry.getKey()){
                iterator.remove();
            }
        }
    }

    public BasePresenter getCachedPresenter(int id){
        for(Map.Entry<Integer, BasePresenter> entry : cachedPresenters.entrySet()){
            if(id == entry.getKey()){
                return entry.getValue();
            }
        }
        return null;
    }
}
