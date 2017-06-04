package com.nixxie.vehiclemarket.busEvents;

import com.nixxie.vehiclemarketmvc.mvp.model.BuiltDateModel;

/**
 * Created by nikolahristovski on 6/4/17.
 */

public class SelectedBuiltDate {

    private BuiltDateModel date;

    public SelectedBuiltDate(BuiltDateModel date) {
        this.date = date;
    }

    public BuiltDateModel getDate() {
        return date;
    }

    public void setDate(BuiltDateModel date) {
        this.date = date;
    }
}
