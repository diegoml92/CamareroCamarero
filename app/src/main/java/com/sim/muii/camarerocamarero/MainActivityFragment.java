package com.sim.muii.camarerocamarero;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context context = this.getContext();

        //!!REV
        if (OrderList.orderList.isEmpty()) {
            OrderList.orderList.add(new Order("Mesa1"));
            OrderList.orderList.add(new Order("Mesa2"));
            com.sim.muii.camarerocamarero.Menu.menu.add(new com.sim.muii.camarerocamarero.MenuItem("Coke", 2.0f));
            com.sim.muii.camarerocamarero.Menu.menu.add(new com.sim.muii.camarerocamarero.MenuItem("Sandwich", 3.75f));
        }

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListAdapter orderAdapter = new ArrayAdapter<Order>
                (getActivity(), android.R.layout.simple_list_item_1, OrderList.orderList);

        final ListView orderListView = (ListView) rootView.findViewById(R.id.listview_pedidos);

        orderListView.setEmptyView(rootView.findViewById(R.id.empty_list_view));
        orderListView.setAdapter(orderAdapter);

        orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ViewOrderActivity.class);
                intent.putExtra("order_index", Integer.toString(position));
                startActivity(intent);
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
            OrderList.orderList.remove(info.position);
            ((ArrayAdapter)((ListView)getActivity().findViewById(R.id.listview_pedidos)).
                    getAdapter()).notifyDataSetChanged();
            return true;
        } else if (item.getTitle().toString().
                equalsIgnoreCase(getString(R.string.context_menu_edit))) {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Intent intent = new Intent(getContext(), ViewOrderActivity.class);
            intent.putExtra("order_index", Integer.toString(info.position));
            startActivity(intent);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

}
