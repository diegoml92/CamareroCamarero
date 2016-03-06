package com.sim.muii.camarerocamarero;

import java.util.ArrayList;
import java.util.List;

class Order {

    private String name;
    private List<OrderItem> items;
    private float price;
    private boolean isFinished;

    public Order(String name) {
        this.name = name;
        this.items = new ArrayList<>();
        this.price = calculatePrice();
        this.isFinished = false;
        //!!REV
        addItem(new OrderItem(new MenuItem("Water", 1.50f)));
        addItem(new OrderItem(new MenuItem("Burger", 5.55f)));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void addItem (OrderItem item) {
        this.items.add(item);
        recalculatePrice();
    }

    public void removeItem (int position) {
        this.items.remove(position);
        recalculatePrice();
    }

    public void recalculatePrice () {
        this.price = calculatePrice();
    }

    private float calculatePrice () {
        float total = 0;
        for(OrderItem item : this.items) {
            total += item.getItem().getPrice() * item.getAmount();
        }
        return total;
    }

    @Override
    public String toString () {
        return name;
    }
}
