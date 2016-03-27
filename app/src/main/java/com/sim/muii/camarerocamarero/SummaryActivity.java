package com.sim.muii.camarerocamarero;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class SummaryActivity extends AppCompatActivity {

    private OrdersDataSource orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        orderList = new OrdersDataSource(this);
        orderList.open();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView revenuestv = (TextView) findViewById(R.id.revenues_value);
        revenuestv.setText(String.format("%.02f" + getString(R.string.currency),
                        getRevenue(orderList.getAllOrders())));
        TextView completedtv = (TextView) findViewById(R.id.completed_orders_amount);
        completedtv.setText(String.format("%d", getCompletedAmount(orderList.getAllOrders())));

    }

    private static float getRevenue(List<Order> orderList) {
        float revenue = 0;
        for (Order order : orderList) {
            if (order.isFinished()) {
                revenue += order.getPrice();
            }
        }
        return revenue;
    }

    private static int getCompletedAmount(List<Order> orderList) {
        int total = 0;
        for (Order order : orderList) {
            if (order.isFinished()) {
                total++;
            }
        }
        return total;
    }

    @Override
    public void onResume() {
        orderList.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        orderList.close();
        super.onPause();
    }
}
