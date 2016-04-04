package com.sim.muii.camarerocamarero.commons;


public class OrderItem {

    private long orderId;
    private long menuItemId;
    private int amount;

    public OrderItem() {}

    public OrderItem(long orderId, long menuItemId) {
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.amount = 1;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
