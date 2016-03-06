package com.sim.muii.camarerocamarero;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SelectMenuItemDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ListAdapter listAdapter = new ArrayAdapter<MenuItem>
                (getContext(), android.R.layout.simple_list_item_1, Menu.menu);

        final int orderIndex = getArguments().getInt("order_index");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.select_menu_item_dialog_title))
                .setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OrderList.orderList.get(orderIndex).
                                addItem(new OrderItem(Menu.menu.get(which)));
                        ((OrderAdapter) ((ListView) getActivity().
                                findViewById(R.id.order_items_listview)).
                                        getAdapter()).notifyDataSetChanged();
                        TextView totalPrice = (TextView) getActivity().
                                findViewById(R.id.total_price_textview);
                        totalPrice.setText(String.format("%.02f", OrderList.orderList.
                                get(orderIndex).getPrice()) + getString(R.string.currency));
                    }
                });

        return builder.create();
    }
}
