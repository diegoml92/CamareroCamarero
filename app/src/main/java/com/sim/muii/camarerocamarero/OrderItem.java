package com.sim.muii.camarerocamarero;


public class OrderItem {

    private MenuItem item;
    private int amount;

    public OrderItem (MenuItem item) {
        this.item = item;
        this.amount = 1;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public MenuItem getItem() {
        return item;
    }

    public void setItem(MenuItem item) {
        this.item = item;
    }

    public void incrementAmount () {
        this.amount++;
    }
}
