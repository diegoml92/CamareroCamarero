package com.sim.muii.camarerocamarero.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sim.muii.camarerocamarero.commons.MenuItem;
import com.sim.muii.camarerocamarero.commons.Order;
import com.sim.muii.camarerocamarero.commons.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrdersDataSource {

    private SQLiteDatabase database;
    private final OrderSQLiteHelper dbHelper;
    private final String[] allColumns = { OrderSQLiteHelper.COLUMN_ID, OrderSQLiteHelper.COLUMN_NAME,
            OrderSQLiteHelper.COLUMN_PRICE, OrderSQLiteHelper.COLUMN_COMPLETED };

    public OrdersDataSource (Context context) {
        dbHelper = new OrderSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createOrder(String name) {
        Log.d("DIEGO",this.getClass().getName() + "createOrder -> " + name);
        ContentValues values = new ContentValues();
        values.put(OrderSQLiteHelper.COLUMN_NAME, name);
        values.put(OrderSQLiteHelper.COLUMN_PRICE, 0.0);
        values.put(OrderSQLiteHelper.COLUMN_COMPLETED, 0); // false
        long insertId = database.insert(OrderSQLiteHelper.TABLE_ORDERS, null, values);
        Cursor cursor = database.query(OrderSQLiteHelper.TABLE_ORDERS,
                allColumns, OrderSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Order newOrder = cursorToOrder(cursor);
        cursor.close();
        Log.d("DIEGO","PEDIDO AÑADIDO : " + newOrder.getName() + " - " + newOrder.getPrice());
    }

    public void deleteOrder(Order order) {
        long id = order.get_id();
        database.delete(OrderSQLiteHelper.TABLE_ORDERS, OrderSQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();

        Cursor cursor = database.query(OrderSQLiteHelper.TABLE_ORDERS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Order order = cursorToOrder(cursor);
            orderList.add(order);
            cursor.moveToNext();
        }
        cursor.close();
        return orderList;
    }

    public List<Order> getActiveOrders() {
        List<Order> orderList = new ArrayList<>();

        Cursor cursor = database.query(OrderSQLiteHelper.TABLE_ORDERS,
                allColumns, OrderSQLiteHelper.COLUMN_COMPLETED + "=" + 0, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Order order = cursorToOrder(cursor);
            orderList.add(order);
            cursor.moveToNext();
        }
        cursor.close();
        return orderList;
    }

    public boolean allOrdersFinished() {
        List<Order> orders = getAllOrders();
        boolean active = false;
        int i=0;
        while (!active && i<orders.size()) {
            active = !orders.get(i).isFinished();
            i++;
        }
        return !active;
    }

    public Order getOrderById (long orderId) {
        Cursor cursor = database.query(OrderSQLiteHelper.TABLE_ORDERS, allColumns,
                OrderSQLiteHelper.COLUMN_ID + " = " + orderId, null, null, null, null);
        if (cursor.moveToFirst()) {
            return cursorToOrder(cursor);
        } else {
            return null;
        }
    }

    public boolean orderExists (String name) {
        Log.d("DIEGO", "Comprobamos si existe el pedido con nombre : " + name);
        Cursor cursor = database.query(OrderSQLiteHelper.TABLE_ORDERS, allColumns,
                OrderSQLiteHelper.COLUMN_NAME + " = '" + name + "'", null, null, null, null);
        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }

    private Order cursorToOrder (Cursor cursor) {
        Order order = new Order();
        for (int i = 0; i<cursor.getColumnCount(); i++) {
            Log.d("DIEGO", cursor.getColumnName(i));
        }
        order.set_id(cursor.getLong(cursor.getColumnIndex(OrderSQLiteHelper.COLUMN_ID)));
        order.setName(cursor.getString(cursor.getColumnIndex(OrderSQLiteHelper.COLUMN_NAME)));
        order.setPrice(cursor.getFloat(cursor.getColumnIndex(OrderSQLiteHelper.COLUMN_PRICE)));
        order.setIsFinished(cursor.getInt
                (cursor.getColumnIndex(OrderSQLiteHelper.COLUMN_COMPLETED)) != 0);
        return order;
    }

    public void setOrderCompleted (long orderId) {
        ContentValues values = new ContentValues();
        values.put(OrderSQLiteHelper.COLUMN_COMPLETED, 1);
        database.update(OrderSQLiteHelper.TABLE_ORDERS, values,
                OrderSQLiteHelper.COLUMN_ID + " = " + orderId, null);
        Order order = getOrderById(orderId);
        Log.d("DIEGO", order.getName() + " - " + order.getPrice() + "-" + order.isFinished());
    }

    public void recalculatePrice (long orderId, Context context) {
        OrderItemsDataSource orderItems = new OrderItemsDataSource(context);
        MenuItemsDataSource menu = new MenuItemsDataSource(context);
        orderItems.open();
        menu.open();

        float total = 0;
        for(OrderItem orderItem : orderItems.getAllOrderItems(orderId)) {
            long menuItemId = orderItem.getMenuItemId();
            MenuItem menuItem = menu.getMenuItem(menuItemId);
            total += menuItem.getPrice() * orderItem.getAmount();
        }
        orderItems.close();
        menu.close();

        ContentValues values = new ContentValues();
        values.put(OrderSQLiteHelper.COLUMN_PRICE, total);
        database.update(OrderSQLiteHelper.TABLE_ORDERS, values,
                OrderSQLiteHelper.COLUMN_ID + " = " + orderId, null);
    }

}
