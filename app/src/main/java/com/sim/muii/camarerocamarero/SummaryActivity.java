package com.sim.muii.camarerocamarero;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView revenuestv = (TextView) findViewById(R.id.revenues_value);
        revenuestv.setText(String.format("%.02f", OrderList.getRevenue()) +
                getString(R.string.currency));
        TextView completedtv = (TextView) findViewById(R.id.completed_orders_amount);
        completedtv.setText(Integer.toString(OrderList.getCompletedAmount()));

    }
}
