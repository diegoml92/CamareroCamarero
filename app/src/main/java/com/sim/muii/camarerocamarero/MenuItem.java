package com.sim.muii.camarerocamarero;

public class MenuItem {

    private String name;
    private float price;

    public MenuItem (String name, float price) {
        this.name = name;
        this.price = price;
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
