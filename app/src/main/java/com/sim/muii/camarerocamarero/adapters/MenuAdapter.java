package com.sim.muii.camarerocamarero.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sim.muii.camarerocamarero.commons.MenuItem;
import com.sim.muii.camarerocamarero.R;

import java.util.List;

public class MenuAdapter extends BaseAdapter {

    private final Context context;
    private static LayoutInflater inflater = null;
    private final List<MenuItem> menu;

    public MenuAdapter (Context context, List<MenuItem> menu) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.menu = menu;
    }

    @Override
    public int getCount() {
        return menu.size();
    }

    @Override
    public MenuItem getItem(int position) {
        return menu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return menu.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.menu_item_list_row, null);
        }
        TextView nametv, pricetv;
        nametv = (TextView) vi.findViewById(R.id.item_name_in_list);
        pricetv = (TextView) vi.findViewById(R.id.item_price_in_list);

        nametv.setText(menu.get(position).getName());
        pricetv.setText(String.format("%.02f" + context.getString(R.string.currency),
                menu.get(position).getPrice()));

        return vi;
    }

    public void remove (MenuItem menuItem) {
        menu.remove(menuItem);
        notifyDataSetChanged();
    }

    public void add (MenuItem menuItem) {
        menu.add(menuItem);
        notifyDataSetChanged();
    }
}
