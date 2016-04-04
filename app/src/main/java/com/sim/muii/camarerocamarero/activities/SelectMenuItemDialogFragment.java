package com.sim.muii.camarerocamarero.activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sim.muii.camarerocamarero.commons.MenuItem;
import com.sim.muii.camarerocamarero.commons.OrderItem;
import com.sim.muii.camarerocamarero.R;
import com.sim.muii.camarerocamarero.adapters.OrderAdapter;
import com.sim.muii.camarerocamarero.database.MenuItemsDataSource;
import com.sim.muii.camarerocamarero.database.OrderItemsDataSource;
import com.sim.muii.camarerocamarero.database.OrdersDataSource;

import java.util.List;

public class SelectMenuItemDialogFragment extends DialogFragment {

    private OrdersDataSource orderList;
    private OrderItemsDataSource orderItems;
    private MenuItemsDataSource menu;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        menu = new MenuItemsDataSource(getContext());
        orderList = new OrdersDataSource(getContext());
        orderItems = new OrderItemsDataSource(getContext());
        menu.open();
        orderList.open();
        orderItems.open();

        final long orderId = getArguments().getLong("order_index");
        List<OrderItem> items = orderItems.getAllOrderItems(orderId);
        final ListAdapter listAdapter = new ArrayAdapter<>
                (getContext(), android.R.layout.simple_list_item_1, menu.getNotInOrderItems(items));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (!listAdapter.isEmpty()) {
            builder.setTitle(getString(R.string.select_menu_item_dialog_title))
                    .setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MenuItem menuItem = (MenuItem) listAdapter.getItem(which);
                            orderItems.createOrderItem(orderId, menuItem.get_id());
                            OrderAdapter adapter = ((OrderAdapter) ((ListView) getActivity().
                                    findViewById(R.id.order_items_listview)).getAdapter());
                            adapter.add(new OrderItem(orderId, menuItem.get_id()));
                            adapter.notifyDataSetChanged();
                            orderList.recalculatePrice(orderId, getContext());
                            TextView totalPrice = (TextView) getActivity().
                                    findViewById(R.id.total_price_textview);
                            totalPrice.setText(String.format("%.02f" + getString(R.string.currency),
                                    orderList.getOrderById(orderId).getPrice()));
                        }
                    });
        } else {
            builder.setTitle(getString(R.string.select_menu_item_dialog_empty_title))
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                        }
                    });
        }

        return builder.create();
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
