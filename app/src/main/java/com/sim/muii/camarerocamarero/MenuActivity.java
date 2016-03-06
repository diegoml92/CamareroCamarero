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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MenuAdapter menuAdapter = new MenuAdapter (this);

        ListView menulv = (ListView) findViewById(R.id.menu_listview);

        menulv.setEmptyView(findViewById(R.id.empty_list_view));
        menulv.setAdapter(menuAdapter);

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
            menu.add(getString(R.string.context_menu_edit));
            menu.add(getString(R.string.context_menu_duplicate));
            menu.add(getString(R.string.context_menu_delete));
        }
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.context_menu_delete))) {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Menu.menu.remove(info.position);
            ((MenuAdapter) ((ListView) findViewById(R.id.menu_listview)).
                    getAdapter()).notifyDataSetChanged();
            return true;
        } else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.context_menu_duplicate))) {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            MenuItem menuItem = Menu.menu.get(info.position);
            int n = 1;
            String name;
            do{
                name = menuItem.getName() + "_" + n;
                n++;
            } while (Menu.menuItemExists(name));
            Menu.add(new MenuItem(name, menuItem.getPrice()));
            ((MenuAdapter) ((ListView) findViewById(R.id.menu_listview)).
                    getAdapter()).notifyDataSetChanged();
            return true;
        } else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.context_menu_edit))) {
            //TODO - Editar producto del menu
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

}
