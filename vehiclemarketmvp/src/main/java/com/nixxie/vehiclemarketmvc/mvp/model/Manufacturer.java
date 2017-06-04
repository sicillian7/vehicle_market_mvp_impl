package com.nixxie.vehiclemarketmvc.mvp.model;

/**
 * Created by nikolahristovski on 5/31/17.
 */

public class Manufacturer extends ModelWrapper{

    public Manufacturer() {
    }

    public Manufacturer(String id, String name) {
        super(id, name);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
