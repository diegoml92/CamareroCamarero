package com.sim.muii.camarerocamarero;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class OrderAdapter extends BaseAdapter {

    private final Context context;
    private final Order order;
    private static LayoutInflater inflater = null;

    public OrderAdapter (Context context, Order order) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.order = order;
    }

    @Override
    public int getCount() {
        return this.order.getItems().size();
    }

    @Override
    public OrderItem getItem(int position) {
        return this.order.getItems().get(position);
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

        amounttv.setText(Integer.toString(this.order.getItems().get(position).getAmount()));
        nametv.setText(this.order.getItems().get(position).getItem().getName());
        pricetv.setText(this.order.getItems().get(position).getItem().getPriceString() +
                context.getString(R.string.currency));

        return vi;
    }
}
