package com.sim.muii.camarerocamarero.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sim.muii.camarerocamarero.R;
import com.sim.muii.camarerocamarero.commons.MenuItem;
import com.sim.muii.camarerocamarero.database.MenuItemsDataSource;

public class NewMenuItemActivity extends AppCompatActivity {

    private MenuItemsDataSource menu;
    private MenuItem menuItem;
    private long menuitemId;
    private boolean createItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_menu_item);

        menu = new MenuItemsDataSource(this);
        menu.open();

        Intent intent = getIntent();
        createItem = false;
        try {
            menuitemId = Long.parseLong(intent.getStringExtra("item_index"));
            menuItem = menu.getMenuItem(menuitemId);
            setTitle(R.string.title_activity_new_item2);
            Log.d("DIEGO", menuItem.getName() + " - " + String.format("%.02f", menuItem.getPrice()));
            EditText nameet = (EditText) findViewById(R.id.item_name_edittext);
            nameet.setHint(menuItem.getName());
            EditText priceet = (EditText) findViewById(R.id.item_price_input);
            priceet.setHint(String.format("%.02f", menuItem.getPrice()));

        } catch (Exception e) {
            Log.d("DIEGO", "Ha habido una excepcion");
            createItem = true;
        }

        final Context context = this;

        final EditText priceet = (EditText) findViewById(R.id.item_price_input);
        priceet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    EditText nameet = (EditText) findViewById(R.id.item_name_edittext);
                    if(createItem) {
                        if (createNewMenuItem(nameet.getText().toString(), v.getText().toString())) {
                            Intent intent = new Intent(context, MenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        String name = (nameet.getText().toString().trim().isEmpty()) ?
                                menuItem.getName() : nameet.getText().toString();
                        float price = priceet.getText().toString().trim().isEmpty() ?
                                menuItem.getPrice() : Float.parseFloat(v.getText().toString());
                        if (updateMenuItem(name, price)) {
                            Intent intent = new Intent(context, MenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
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
                if (createItem) {
                    if (createNewMenuItem(nameet.getText().toString(), priceet.getText().toString())) {
                        Intent intent = new Intent(context, MenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    String name = (nameet.getText().toString().trim().isEmpty()) ?
                            menuItem.getName() : nameet.getText().toString();
                    float price = priceet.getText().toString().trim().isEmpty() ?
                            menuItem.getPrice() : Float.parseFloat(priceet.getText().toString());
                    if (updateMenuItem(name, price)) {
                        Intent intent = new Intent(context, MenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
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
        Log.d("DIEGO", "CREAMOS EL INTENT PARA VOLVER A MENUACTIVITY");
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
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

    private boolean updateMenuItem(String name, float price) {
        if (name == menuItem.getName() || !menu.menuItemExists(name)) {
            name = name.trim();
            menu.updateMenuItem(new MenuItem(menuitemId, name, price));
            return true;
        } else {
            Toast.makeText(this, R.string.item_update_error, Toast.LENGTH_SHORT).show();
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
