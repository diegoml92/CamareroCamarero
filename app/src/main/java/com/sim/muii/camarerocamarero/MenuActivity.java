package com.sim.muii.camarerocamarero;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

        // TODO - Editar elemento del menu
        /*menulv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ViewMenuItemActivity.class);
                intent.putExtra("item_index", Integer.toString(position));
                startActivity(intent);
            }
        });*/

        registerForContextMenu(menulv);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.menu_listview) {
            OrdersDataSource orderList = new OrdersDataSource(getApplicationContext());
            orderList.open();
            boolean activeOrders = !orderList.allOrdersFinished();
            orderList.close();
            menu.add(getString(R.string.context_menu_duplicate));
            if(!activeOrders) {
                menu.add(getString(R.string.context_menu_delete));
            }
        }
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.context_menu_delete))) {
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
        Intent intent = NavUtils.getParentActivityIntent(this);
        NavUtils.navigateUpTo(this,intent);
    }

    public void addNewMenuItem (View view) {
        Intent intent = new Intent(this, NewMenuItemActivity.class);
        startActivity(intent);
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
