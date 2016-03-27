package com.sim.muii.camarerocamarero;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class OrderItemsDataSource {

    private SQLiteDatabase database;
    private final OrderItemSQLiteHelper dbHelper;
    private final String[] allColumns = { OrderItemSQLiteHelper.COLUMN_ORDER_ID,
            OrderItemSQLiteHelper.COLUMN_ITEM_ID, OrderItemSQLiteHelper.COLUMN_AMOUNT};

    public OrderItemsDataSource (Context context) {
        dbHelper = new OrderItemSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createOrderItem(long orderId, long menuItemId) {
        ContentValues values = new ContentValues();
        values.put(OrderItemSQLiteHelper.COLUMN_ORDER_ID, orderId);
        values.put(OrderItemSQLiteHelper.COLUMN_ITEM_ID, menuItemId);
        values.put(OrderItemSQLiteHelper.COLUMN_AMOUNT, 1);
        long insertId = database.insert(OrderItemSQLiteHelper.TABLE_ORDER_ITEMS, null, values);
        Log.d("DIEGO", Long.toString(insertId));
        Cursor cursor = database.query(OrderItemSQLiteHelper.TABLE_ORDER_ITEMS,
                allColumns, OrderItemSQLiteHelper.COLUMN_ORDER_ID + "=" + orderId + " and " +
                        OrderItemSQLiteHelper.COLUMN_ITEM_ID + "=" + menuItemId,
                null, null, null, null);
        cursor.moveToFirst();
        cursor.close();
    }

    public void deleteOrderItem(OrderItem orderItem) {
        long orderId = orderItem.getOrderId();
        long menuItemId = orderItem.getMenuItemId();
        database.delete(OrderItemSQLiteHelper.TABLE_ORDER_ITEMS,
                OrderItemSQLiteHelper.COLUMN_ORDER_ID + "=" + orderId + " and " +
                        OrderItemSQLiteHelper.COLUMN_ITEM_ID + "=" + menuItemId, null);
    }

    public List<OrderItem> getAllOrderItems(long orderId) {
        List<OrderItem> orderItemList = new ArrayList<>();

        Cursor cursor = database.query(OrderItemSQLiteHelper.TABLE_ORDER_ITEMS,
                allColumns, OrderItemSQLiteHelper.COLUMN_ORDER_ID + "=" + orderId,
                null, null, null, null);

        for(int i=0; i<cursor.getColumnCount(); i++) {
            Log.d("DIEGO", cursor.getColumnName(i));
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OrderItem orderItem = cursorToOrderItem(cursor);
            orderItemList.add(orderItem);
            cursor.moveToNext();
        }
        cursor.close();
        return orderItemList;
    }

    private OrderItem cursorToOrderItem (Cursor cursor) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(cursor.getLong
                (cursor.getColumnIndex(OrderItemSQLiteHelper.COLUMN_ORDER_ID)));
        orderItem.setMenuItemId(cursor.getLong
                (cursor.getColumnIndex(OrderItemSQLiteHelper.COLUMN_ITEM_ID)));
        orderItem.setAmount(cursor.getInt
                (cursor.getColumnIndex(OrderItemSQLiteHelper.COLUMN_AMOUNT)));
        return orderItem;
    }

    public void incrementAmount (OrderItem orderItem) {
        ContentValues values = new ContentValues();
        values.put(OrderItemSQLiteHelper.COLUMN_AMOUNT, orderItem.getAmount() + 1);
        database.update(OrderItemSQLiteHelper.TABLE_ORDER_ITEMS, values,
                OrderItemSQLiteHelper.COLUMN_ORDER_ID + "=" + orderItem.getOrderId() + " and " +
                OrderItemSQLiteHelper.COLUMN_ITEM_ID + "=" + orderItem.getMenuItemId(), null);
    }

}
