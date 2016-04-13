package com.sim.muii.camarerocamarero.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sim.muii.camarerocamarero.R;
import com.sim.muii.camarerocamarero.database.OrdersDataSource;

public class NewOrderActivity extends AppCompatActivity {

    private OrdersDataSource orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        orderList = new OrdersDataSource(this);

        final Context context = this;

        EditText editText = (EditText) findViewById(R.id.order_name_edittext);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if (createNewOrder(v.getText().toString())) {
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    handled = true;
                }
                return handled;
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.send_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.order_name_edittext);
                if (createNewOrder(editText.getText().toString())) {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean createNewOrder(String name) {
        name = name.trim();
        if (!name.isEmpty() && !orderList.orderExists(name)) {
            orderList.createOrder(name);
            return true;
        } else {
            Toast.makeText(this, R.string.order_creation_error, Toast.LENGTH_SHORT).show();
            return false;
        }
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
