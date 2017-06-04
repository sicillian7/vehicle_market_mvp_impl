package com.nixxie.vehiclemarketmvc.mvp.model;

/**
 * Created by nikolahristovski on 6/2/17.
 */

public abstract class ModelWrapper {

    protected String id, name;

    public ModelWrapper() {
    }

    public ModelWrapper(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract String getId();

    public abstract void setId(String id);

    public abstract String getName();

    public abstract void setName(String name);
}
