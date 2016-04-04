package com.sim.muii.camarerocamarero.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.sim.muii.camarerocamarero.commons.Order;
import com.sim.muii.camarerocamarero.R;
import com.sim.muii.camarerocamarero.commons.OrderItem;
import com.sim.muii.camarerocamarero.database.MenuItemsDataSource;
import com.sim.muii.camarerocamarero.database.OrderItemsDataSource;
import com.sim.muii.camarerocamarero.database.OrdersDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SummaryActivity extends AppCompatActivity {

    private static OrdersDataSource orderList;
    private static OrderItemsDataSource orderItems;
    private static MenuItemsDataSource menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        orderList = new OrdersDataSource(this);
        orderList.open();
        orderItems = new OrderItemsDataSource(this);
        orderItems.open();
        menu = new MenuItemsDataSource(this);
        menu.open();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<Order> allOrders = orderList.getAllOrders();

        TextView revenuestv = (TextView) findViewById(R.id.revenues_value);
        revenuestv.setText(String.format("%.02f" + getString(R.string.currency),
                        getRevenue(allOrders)));
        TextView completedtv = (TextView) findViewById(R.id.completed_orders_amount);
        completedtv.setText(String.format("%d", getCompletedAmount(allOrders)));

        TextView toptv = (TextView) findViewById(R.id.top_items_textview);
        toptv.setText(getTopProducts(allOrders));

    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static float getRevenue(List<Order> orderList) {
        float revenue = 0;
        for (Order order : orderList) {
            if (order.isFinished()) {
                revenue += order.getPrice();
            }
        }
        return revenue;
    }

    private static int getCompletedAmount(List<Order> orderList) {
        int total = 0;
        for (Order order : orderList) {
            if (order.isFinished()) {
                total++;
            }
        }
        return total;
    }

    private static String getTopProducts(List<Order> orderList) {
        List<Order> finished = new ArrayList<>();
        Map<Long, Integer> indexes = new HashMap<>();
        for (Order order : orderList) {
            if (order.isFinished()) {
                finished.add(order);
            }
        }
        Log.d("DIEGO", "HAY " + finished.size() + " PEDIDOS TERMINADOS");
        for (Order order : finished) {
            List<OrderItem> items = orderItems.getAllOrderItems(order.get_id());
            Log.d("DIEGO", "EL PEDIDO TIENE " + items.size() + " ITEMS");
            for(OrderItem item : items) {
                if(indexes.containsKey(item.getMenuItemId())) {
                    Log.d("DIEGO", "YA EXISTE " + item.getMenuItemId() + " SE INCREMENTA -> " +
                            (indexes.get(item.getMenuItemId())+1));
                    Log.d("DIEGO", "ANTES: " + indexes.get(item.getMenuItemId()));
                    indexes.put(item.getMenuItemId(), indexes.get(item.getMenuItemId()) + 1);
                    Log.d("DIEGO", "DESPUES: " + indexes.get(item.getMenuItemId()));
                } else {
                    Log.d("DIEGO", "NO EXISTE, SE AÑADE " + item.getMenuItemId());
                            indexes.put(item.getMenuItemId(), 1);
                }
            }
        }
        String ranking [] = {"", "", "", "", ""};
        int amount [] = {0, 0, 0, 0, 0};
        Iterator it = indexes.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Long, Integer> pair = (Map.Entry<Long, Integer>) it.next();
            Log.d("DIEGO","COMPROBAMOS " + pair.getKey());
            int i = 0;
            boolean improvement = false;
            while (!improvement && i < amount.length) {
                int k = pair.getValue();
                improvement = k > amount[i];
                if (improvement) {
                    Log.d("DIEGO", "HA MEJORADO " + pair.getKey() + "; TIENE VALOR " + k);
                    try {
                        for(int j=amount.length-1; j>i; j--) {
                            amount[j] = amount[j-1];
                        }
                        for(int j=amount.length-1; j>i; j--) {
                            ranking[j] = ranking[j-1];
                        }
                        ranking[i] = menu.getMenuItem(pair.getKey()).getName() +
                                " (" + k + ")";
                        amount[i] = k;
                        Log.d("DIEGO", "Nueva disposición: ");
                        for(int j=0; j<amount.length; j++) {
                            Log.d("DIEGO", "" + amount[j]);
                        }
                    } catch (Exception e) {
                        Log.e("DIEGO", "YA NO EXISTE ESTE PRODUCTO");
                    }
                }
                i++;
            }
        }
        String result = "";
        for(String s : ranking) {
            result += s + "\n";
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        orderList.open();
        orderItems.open();
        menu.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        orderList.close();
        orderItems.close();
        menu.close();
        super.onPause();
    }
}
