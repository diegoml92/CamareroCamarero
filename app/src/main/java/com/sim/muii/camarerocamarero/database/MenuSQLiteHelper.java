package com.sim.muii.camarerocamarero.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.sim.muii.camarerocamarero.R;

class MenuSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_MENU_ITEMS = "menu_items";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";

    private static final String DATABASE_NAME = "menu.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_MENU_ITEMS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text unique not null, "
            + COLUMN_PRICE + " real unsigned not null );";

    public MenuSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU_ITEMS);
        onCreate(db);
    }

}
