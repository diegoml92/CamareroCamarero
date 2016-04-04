package com.sim.muii.camarerocamarero.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sim.muii.camarerocamarero.commons.MenuItem;
import com.sim.muii.camarerocamarero.database.MenuItemsDataSource;
import com.sim.muii.camarerocamarero.commons.Order;
import com.sim.muii.camarerocamarero.commons.OrderItem;
import com.sim.muii.camarerocamarero.R;

import java.util.List;

public class OrderAdapter extends BaseAdapter {

    private final MenuItemsDataSource menu;

    private final List<OrderItem> orderItems;
    private static LayoutInflater inflater = null;

    public OrderAdapter (Context context, Order order, List<OrderItem> orderItems) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.orderItems = orderItems;
        this.menu = new MenuItemsDataSource(context);
    }

    @Override
    public int getCount() {
        return this.orderItems.size();
    }

    @Override
    public OrderItem getItem(int position) {
        return this.orderItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.order_item_list_row, null);
        }
        TextView amounttv, nametv, pricetv;

        amounttv = (TextView) vi.findViewById(R.id.item_amount_in_list);
        nametv = (TextView) vi.findViewById(R.id.item_name_in_list);
        pricetv = (TextView) vi.findViewById(R.id.item_price_in_list);

        menu.open();
        MenuItem menuItem = menu.getMenuItem(orderItems.get(position).getMenuItemId());
        menu.close();

        int amount = orderItems.get(position).getAmount();
        amounttv.setText(String.format("%d", amount));
        nametv.setText(menuItem.getName());
        pricetv.setText(String.format("%.02f" + vi.getContext().getString(R.string.currency),
                menuItem.getPrice() * amount));

        return vi;
    }

    public void remove (OrderItem orderItem) {
        boolean found = false;
        int i=0;
        while (!found && i < orderItems.size()) {
            OrderItem item = orderItems.get(i);
            found = item.getOrderId() == orderItem.getOrderId() &&
                    item.getMenuItemId() == orderItem.getMenuItemId();
            if(found) {
                orderItems.remove(i);
            }
            i++;
        }
    }

    public void add (OrderItem orderItem) {
        orderItems.add(orderItem);
    }

    public void incrementItemAmount (int pos) {
        orderItems.get(pos).setAmount(orderItems.get(pos).getAmount()+1);
        notifyDataSetChanged();
    }
}
