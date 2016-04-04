package com.sim.muii.camarerocamarero.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sim.muii.camarerocamarero.R;
import com.sim.muii.camarerocamarero.adapters.OrderAdapter;
import com.sim.muii.camarerocamarero.commons.Order;
import com.sim.muii.camarerocamarero.commons.OrderItem;
import com.sim.muii.camarerocamarero.database.MenuItemsDataSource;
import com.sim.muii.camarerocamarero.database.OrderItemsDataSource;
import com.sim.muii.camarerocamarero.database.OrdersDataSource;


public class ViewOrderActivity extends AppCompatActivity {

    private long orderId;

    private OrdersDataSource orderList;
    private MenuItemsDataSource menu;
    private OrderItemsDataSource orderItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderList = new OrdersDataSource(this);
        menu = new MenuItemsDataSource(this);
        orderItems = new OrderItemsDataSource(this);
        orderList.open();
        menu.open();
        orderItems.open();

        Intent intent = getIntent();
        orderId = Long.parseLong(intent.getStringExtra("order_index"));

        Log.d("DIEGO", "orderId: " + orderId);
        Order order = orderList.getOrderById(orderId);

        setTitle(order.toString());

        setContentView(R.layout.activity_view_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        OrderAdapter orderAdapter =
                new OrderAdapter(this, order, orderItems.getAllOrderItems(order.get_id()));

        ListView orderlv = (ListView) findViewById(R.id.order_items_listview);
        orderlv.setEmptyView(findViewById(R.id.empty_list_view));
        orderlv.setAdapter(orderAdapter);

        TextView totalPrice = (TextView) findViewById(R.id.total_price_textview);
        totalPrice.setText(String.format("%.02f" + getString(R.string.currency),
                        order.getPrice()));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menu.isEmpty()) {
                    Snackbar.make(view, getString(R.string.no_menu_items), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    SelectMenuItemDialogFragment selectItem = new SelectMenuItemDialogFragment();
                    Bundle args = new Bundle();
                    args.putLong("order_index", orderId);
                    selectItem.setArguments(args);
                    selectItem.show(getSupportFragmentManager(),
                            getString(R.string.select_menu_item_dialog_tag));
                }
            }
        });

        registerForContextMenu(orderlv);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    public void onBackPressed() {
        Log.d("DIEGO", "CREAMOS EL INTENT PARA VOLVER A MAIN ACTIVITY DESDE VIEW ORDER");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.order_items_listview) {
            menu.add(getString(R.string.context_menu_delete));
        }
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.context_menu_delete))) {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            OrderAdapter adapter = (OrderAdapter)
                    ((ListView) findViewById(R.id.order_items_listview)).getAdapter();

            OrderItem orderItem = adapter.getItem(info.position);
            orderItems.deleteOrderItem(orderItem);
            adapter.remove(orderItem);
            adapter.notifyDataSetChanged();
            orderList.recalculatePrice(orderId, this);

            TextView totalPrice = (TextView) findViewById(R.id.total_price_textview);
            totalPrice.setText(String.format("%.02f" + getString(R.string.currency),
                    orderList.getOrderById(orderId).getPrice()));
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    public void incrementItemCounter(View view) {
        ListView listView = (ListView)findViewById(R.id.order_items_listview);
        int pos = listView.getPositionForView(view);
        OrderAdapter adapter = ((OrderAdapter) ((ListView) findViewById
                (R.id.order_items_listview)).getAdapter());
        OrderItem orderItem = adapter.getItem(pos);
        orderItems.incrementAmount(orderItem);
        adapter.incrementItemAmount(pos);
        orderList.recalculatePrice(orderId, getApplicationContext());
        TextView totalPrice = (TextView) findViewById(R.id.total_price_textview);
        totalPrice.setText(String.format("%.02f" + getString(R.string.currency),
                orderList.getOrderById(orderId).getPrice()));
    }

    public void completeOrder (View view) {
        orderList.setOrderCompleted(orderId);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    public void onResume() {
        menu.open();
        orderList.open();
        orderItems.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        menu.close();
        orderList.close();
        orderItems.close();
        super.onPause();
    }

}
