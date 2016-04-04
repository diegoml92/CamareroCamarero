package com.sim.muii.camarerocamarero.commons;

public class MenuItem {

    private long _id;
    private String name;
    private float price;

    public MenuItem () {}

    public MenuItem (String name, float price) {
        this.name = name;
        this.price = price;
    }

    public MenuItem (long _id, String name, float price) {
        this._id = _id;
        this.name = name;
        this.price = price;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getPriceString () {
        return String.format("%.02f", this.price);
    }

    @Override
    public String toString () {
        return this.name + " - " + String.format("%.02f", this.price) + "€";
    }
}
