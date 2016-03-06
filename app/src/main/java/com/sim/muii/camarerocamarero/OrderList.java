package com.sim.muii.camarerocamarero;


import java.util.ArrayList;
import java.util.List;

class OrderList {

    public static final List<Order> orderList = new ArrayList<Order>();

    public static void add (Order order) {
        orderList.add(order);
    }

    public static boolean orderExists (String name) {
        boolean result = false;
        int i = 0;
        while (!result && i<orderList.size()) {
            result = name.equalsIgnoreCase(orderList.get(i).getName());
            i++;
        }
        return result;
    }

    public static float getRevenue () {
        float revenue = 0;
        for (Order order : orderList) {
            if (order.isFinished()) {
                revenue += order.getPrice();
            }
        }
        return revenue;
    }

    public static int getCompletedAmount () {
        int total = 0;
        for (Order order : orderList) {
            if (order.isFinished()) {
                total++;
            }
        }
        return total;
    }
}
