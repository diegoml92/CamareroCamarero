package com.sim.muii.camarerocamarero;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class NewMenuItemActivity extends AppCompatActivity {

    private MenuItemsDataSource menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_menu_item);

        menu = new MenuItemsDataSource(this);
        menu.open();

        final Context context = this;

        EditText priceet = (EditText) findViewById(R.id.item_price_input);
        priceet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    EditText nameet = (EditText) findViewById(R.id.item_name_edittext);
                    if (createNewMenuItem(nameet.getText().toString(), v.getText().toString())) {
                        Intent intent = new Intent(context, MenuActivity.class);
                        startActivity(intent);
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
                EditText priceet = (EditText) findViewById(R.id.item_price_input);
                EditText nameet = (EditText) findViewById(R.id.item_name_edittext);
                if (createNewMenuItem(nameet.getText().toString(), priceet.getText().toString())) {
                    Intent intent = new Intent(context, MenuActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = NavUtils.getParentActivityIntent(this);
        NavUtils.navigateUpTo(this,intent);
    }

    private boolean createNewMenuItem(String name, String price) {
        if (!name.isEmpty() && !price.isEmpty() && !menu.menuItemExists(name)) {
            float pricef;
            try {
                pricef = Float.parseFloat(price);
            } catch (Exception e) {
                Toast.makeText(this, R.string.incorrect_price_input, Toast.LENGTH_SHORT).show();
                return false;
            }
            name = name.trim();
            menu.createMenuItem(name, pricef);
            return true;
        } else {
            Toast.makeText(this, R.string.item_creation_error, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onResume() {
        menu.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        menu.close();
        super.onPause();
    }
}
