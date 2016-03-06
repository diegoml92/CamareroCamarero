package com.sim.muii.camarerocamarero;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


public class ViewOrderActivity extends AppCompatActivity {

    private int orderIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        orderIndex = Integer.parseInt(intent.getStringExtra("order_index"));

        setTitle(OrderList.orderList.get(orderIndex).toString());

        setContentView(R.layout.activity_view_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        OrderAdapter orderAdapter = new OrderAdapter(this, OrderList.orderList.get(orderIndex));

        ListView orderlv = (ListView) findViewById(R.id.order_items_listview);
        orderlv.setEmptyView(findViewById(R.id.empty_list_view));
        orderlv.setAdapter(orderAdapter);

        TextView totalPrice = (TextView) findViewById(R.id.total_price_textview);
        totalPrice.setText(String.format("%.02f" + getString(R.string.currency),
                        OrderList.orderList.get(orderIndex).getPrice()));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Menu.menu.isEmpty()) {
                    Snackbar.make(view, getString(R.string.no_menu_items), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    SelectMenuItemDialogFragment selectItem = new SelectMenuItemDialogFragment();
                    Bundle args = new Bundle();
                    args.putInt("order_index", orderIndex);
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
    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(this);
        NavUtils.navigateUpTo(this, intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.order_items_listview) {
            menu.add(getString(R.string.context_menu_edit));
            menu.add(getString(R.string.context_menu_delete));
        }
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.context_menu_delete))) {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            OrderList.orderList.get(getOrderIndex()).removeItem(info.position);
            ((OrderAdapter)((ListView)findViewById(R.id.order_items_listview)).
                    getAdapter()).notifyDataSetChanged();

            TextView totalPrice = (TextView) findViewById(R.id.total_price_textview);
            totalPrice.setText(String.format("%.02f" + getString(R.string.currency),
                    OrderList.orderList.get(getOrderIndex()).getPrice()));
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    public void incrementItemCounter(View view) {
        ListView listView = (ListView)findViewById(R.id.order_items_listview);
        int pos = listView.getPositionForView(view);
        TextView amounttv = (TextView) view;
        Order order = OrderList.orderList.get(orderIndex);
        order.getItems().get(pos).incrementAmount();
        amounttv.setText(String.format("%d", order.getItems().get(pos).getAmount()));
        order.recalculatePrice();
        ((TextView)findViewById(R.id.total_price_textview)).
                setText(String.format("%.02f" + getString(R.string.currency), order.getPrice()));
    }

    private int getOrderIndex() {
        return orderIndex;
    }
}
