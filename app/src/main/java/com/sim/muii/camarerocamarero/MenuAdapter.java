package com.sim.muii.camarerocamarero;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class MenuAdapter extends BaseAdapter {

    private final Context context;
    private static LayoutInflater inflater = null;

    public MenuAdapter (Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Menu.menu.size();
    }

    @Override
    public MenuItem getItem(int position) {
        return Menu.menu.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

        nametv.setText(Menu.menu.get(position).getName());
        pricetv.setText(Menu.menu.get(position).getPriceString() +
                context.getString(R.string.currency));

        return vi;
    }
}
