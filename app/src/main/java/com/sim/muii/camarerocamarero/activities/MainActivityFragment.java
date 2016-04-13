package com.sim.muii.camarerocamarero.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


import com.sim.muii.camarerocamarero.R;
import com.sim.muii.camarerocamarero.commons.Order;
import com.sim.muii.camarerocamarero.commons.OrderItem;
import com.sim.muii.camarerocamarero.database.MenuItemsDataSource;
import com.sim.muii.camarerocamarero.database.OrderItemsDataSource;
import com.sim.muii.camarerocamarero.database.OrdersDataSource;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private OrdersDataSource orderList;
    private OrderItemsDataSource orderItems;
    private MenuItemsDataSource menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Context context = this.getContext();

        orderList = new OrdersDataSource(getContext());
        menu = new MenuItemsDataSource(getContext());
        orderItems = new OrderItemsDataSource(getContext());
        orderItems.open();
        orderList.open();
        menu.open();

        for(Order order : orderList.getActiveOrders()) {
            for(OrderItem orderItem: orderItems.getAllOrderItems(order.get_id())) {
                com.sim.muii.camarerocamarero.commons.MenuItem item = menu.getMenuItem(orderItem.getMenuItemId());
            }
        }

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListAdapter orderAdapter = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_list_item_1, orderList.getActiveOrders());

        final ListView orderListView = (ListView) rootView.findViewById(R.id.listview_pedidos);

        orderListView.setEmptyView(rootView.findViewById(R.id.empty_list_view));
        orderListView.setAdapter(orderAdapter);

        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Order> adapter = (ArrayAdapter<Order>) ((ListView) getActivity().
                        findViewById(R.id.listview_pedidos)).getAdapter();
                Order order = adapter.getItem(position);
                Intent intent = new Intent(context, ViewOrderActivity.class);
                intent.putExtra("order_index", Long.toString(order.get_id()));
                startActivity(intent);
                getActivity().finish();
            }
        });

        registerForContextMenu(orderListView);

        return rootView;
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listview_pedidos) {
            menu.add(getString(R.string.context_menu_edit));
            menu.add(getString(R.string.context_menu_delete));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.context_menu_delete))) {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            ArrayAdapter<Order> adapter = (ArrayAdapter<Order>) ((ListView) getActivity().
                    findViewById(R.id.listview_pedidos)).getAdapter();
            Order order = adapter.getItem(info.position);
            orderList.deleteOrder(order);
            adapter.remove(order);
            return true;
        } else if (item.getTitle().toString().
                equalsIgnoreCase(getString(R.string.context_menu_edit))) {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            ArrayAdapter<Order> adapter = (ArrayAdapter<Order>) ((ListView) getActivity().
                    findViewById(R.id.listview_pedidos)).getAdapter();
            Order order = adapter.getItem(info.position);
            Intent intent = new Intent(getContext(), ViewOrderActivity.class);
            intent.putExtra("order_index", Long.toString(order.get_id()));
            startActivity(intent);
            getActivity().finish();
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        orderList.open();
        menu.open();
        orderItems.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        orderList.close();
        menu.close();
        orderItems.close();
        super.onPause();
    }

}
