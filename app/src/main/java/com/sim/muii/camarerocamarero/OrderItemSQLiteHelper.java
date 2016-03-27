package com.sim.muii.camarerocamarero;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

class OrderItemSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_ORDER_ITEMS = "order_items";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_ITEM_ID = "item_id";
    public static final String COLUMN_AMOUNT = "amount";

    private static final String DATABASE_NAME = "orderItems.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_ORDER_ITEMS + "("
            + COLUMN_ORDER_ID + " integer not null, "
            + COLUMN_ITEM_ID + " integer not null, "
            + COLUMN_AMOUNT + " integer not null, "
            + " foreign key (" + COLUMN_ORDER_ID + ") references "
                + OrderSQLiteHelper.TABLE_ORDERS + " ( " + OrderSQLiteHelper.COLUMN_ID + " ) "
                + "on delete cascade , "
            + " foreign key (" + COLUMN_ITEM_ID + ") references "
                + MenuSQLiteHelper.TABLE_MENU_ITEMS + " ( " + MenuSQLiteHelper.COLUMN_ID + " ) "
                + "on delete cascade , "
            + " primary key ( " + COLUMN_ORDER_ID + " , " + COLUMN_ITEM_ID + " ) " + " );";

    public OrderItemSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("DIEGO", "Constructor " + DATABASE_NAME);
        Log.d("DIEGO", Environment.getDataDirectory() + "/data/" +
                context.getString(R.string.app_name) + "/databases/" + DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d("DIEGO", "Crear " + DATABASE_NAME);
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DIEGO", "Actualizar " + DATABASE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        onCreate(db);
    }

}
