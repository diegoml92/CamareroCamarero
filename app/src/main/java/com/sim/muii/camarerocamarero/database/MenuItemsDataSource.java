package com.sim.muii.camarerocamarero.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sim.muii.camarerocamarero.commons.MenuItem;
import com.sim.muii.camarerocamarero.commons.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class MenuItemsDataSource {

    private SQLiteDatabase database;
    private final MenuSQLiteHelper dbHelper;
    private static final String[] allColumns = { MenuSQLiteHelper.COLUMN_ID,
            MenuSQLiteHelper.COLUMN_NAME, MenuSQLiteHelper.COLUMN_PRICE };

    public MenuItemsDataSource (Context context) {
        dbHelper = new MenuSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createMenuItem(String name, float price) {
        ContentValues values = new ContentValues();
        values.put(MenuSQLiteHelper.COLUMN_NAME, name);
        values.put(MenuSQLiteHelper.COLUMN_PRICE, price);
        long insertId = database.insert(MenuSQLiteHelper.TABLE_MENU_ITEMS, null, values);
        Cursor cursor = database.query(MenuSQLiteHelper.TABLE_MENU_ITEMS,
                allColumns, MenuSQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        MenuItem newMenuItem = cursorToMenuItem(cursor);
        cursor.close();
    }

    public void updateMenuItem(MenuItem menuItem) {
        ContentValues values = new ContentValues();
        values.put(MenuSQLiteHelper.COLUMN_NAME, menuItem.getName());
        values.put(MenuSQLiteHelper.COLUMN_PRICE, menuItem.getPrice());
        database.update(MenuSQLiteHelper.TABLE_MENU_ITEMS, values, MenuSQLiteHelper.COLUMN_ID +
                "=" + menuItem.get_id(), null);
    }

    public void deleteMenuItem(MenuItem menuItem) {
        long id = menuItem.get_id();
        database.delete(MenuSQLiteHelper.TABLE_MENU_ITEMS, MenuSQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public boolean isEmpty() {
        Cursor cursor = database.rawQuery("select count (*) from " +
                MenuSQLiteHelper.TABLE_MENU_ITEMS, null);
        cursor.moveToFirst();
        boolean result = cursor.getInt(0) == 0;
        cursor.close();
        return result;
    }

    public List<MenuItem> getNotInOrderItems(List<OrderItem> items) {
        List<MenuItem> menu = getAllMenuItems();

        if (!items.isEmpty()) {
            for(OrderItem orderItem : items) {
                boolean found = false;
                int i = 0;
                while (!found && i < menu.size()) {
                    found = orderItem.getMenuItemId() == menu.get(i).get_id();
                    if (found) {
                        menu.remove(i);
                    }
                    i++;
                }
            }
        }

        return menu;
    }

    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> menu = new ArrayList<>();

        Cursor cursor = database.query(MenuSQLiteHelper.TABLE_MENU_ITEMS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MenuItem menuItem = cursorToMenuItem(cursor);
            menu.add(menuItem);
            cursor.moveToNext();
        }
        cursor.close();
        return menu;
    }

    public boolean menuItemExists (String name) {
        Cursor cursor = database.query(MenuSQLiteHelper.TABLE_MENU_ITEMS, allColumns,
                "name = '" + name + "'", null, null, null, null);
        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }

    public MenuItem getMenuItem (long _id) {
        Cursor cursor = database.query(MenuSQLiteHelper.TABLE_MENU_ITEMS, allColumns,
                MenuSQLiteHelper.COLUMN_ID + "=" + _id,
                null, null, null, null);
        if (!cursor.moveToFirst()) {
            return null;
        } else {
            return cursorToMenuItem(cursor);
        }
    }

    private MenuItem cursorToMenuItem(Cursor cursor) {
        MenuItem menuItem = new MenuItem();
        menuItem.set_id(cursor.getLong(cursor.getColumnIndex(MenuSQLiteHelper.COLUMN_ID)));
        menuItem.setName(cursor.getString(cursor.getColumnIndex(MenuSQLiteHelper.COLUMN_NAME)));
        menuItem.setPrice(cursor.getFloat(cursor.getColumnIndex(MenuSQLiteHelper.COLUMN_PRICE)));
        return menuItem;
    }

}
