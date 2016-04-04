package com.sim.muii.camarerocamarero.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sim.muii.camarerocamarero.R;
import com.sim.muii.camarerocamarero.commons.MenuItem;
import com.sim.muii.camarerocamarero.database.MenuItemsDataSource;
import com.sim.muii.camarerocamarero.adapters.MenuAdapter;
import com.sim.muii.camarerocamarero.database.OrdersDataSource;

public class MenuActivity extends AppCompatActivity {

    private MenuItemsDataSource menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menu = new MenuItemsDataSource(this);
        menu.open();

        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MenuAdapter menuAdapter = new MenuAdapter (this, menu.getAllMenuItems());

        ListView menulv = (ListView) findViewById(R.id.menu_listview);

        menulv.setEmptyView(findViewById(R.id.empty_list_view));
        menulv.setAdapter(menuAdapter);

        final Context context = this;

        menulv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, NewMenuItemActivity.class);
                intent.putExtra("item_index", Long.toString(id));
                startActivity(intent);
                finish();
            }
        });

        registerForContextMenu(menulv);
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.menu_listview) {
            OrdersDataSource orderList = new OrdersDataSource(getApplicationContext());
            orderList.open();
            boolean activeOrders = !orderList.allOrdersFinished();
            orderList.close();
            if(!activeOrders) {
                menu.add(getString(R.string.context_menu_edit));
            }
            menu.add(getString(R.string.context_menu_duplicate));
            if(!activeOrders) {
                menu.add(getString(R.string.context_menu_delete));
            }
        }
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.context_menu_edit))) {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            MenuAdapter adapter = (MenuAdapter) ((ListView) findViewById(R.id.menu_listview)).
                    getAdapter();
            Intent intent = new Intent(this, NewMenuItemActivity.class);
            intent.putExtra("item_index", Long.toString(adapter.getItemId(info.position)));
            startActivity(intent);
            finish();
            return true;
        } else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.context_menu_delete))) {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            MenuAdapter adapter = (MenuAdapter) ((ListView) findViewById(R.id.menu_listview)).
                    getAdapter();
            MenuItem menuItem = adapter.getItem(info.position);
            menu.deleteMenuItem(menuItem);
            adapter.remove(menuItem);
            return true;
        } else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.context_menu_duplicate))) {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            MenuAdapter adapter = (MenuAdapter) ((ListView) findViewById(R.id.menu_listview)).
                    getAdapter();
            MenuItem menuItem = adapter.getItem(info.position);
            int n = 1;
            String name;
            do{
                name = menuItem.getName() + "_" + n;
                n++;
            } while (menu.menuItemExists(name));
            menu.createMenuItem(name, menuItem.getPrice());
            adapter.add(new MenuItem (name, menuItem.getPrice()));
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void addNewMenuItem (View view) {
        Intent intent = new Intent(this, NewMenuItemActivity.class);
        startActivity(intent);
        finish();
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
