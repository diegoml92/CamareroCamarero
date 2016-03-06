package com.sim.muii.camarerocamarero;


import java.util.ArrayList;
import java.util.List;

class Menu {

    public static final List<MenuItem> menu = new ArrayList<MenuItem>();

    public static void add (MenuItem item) {
        menu.add(item);
    }

    public static boolean menuItemExists (String name) {
        boolean result = false;
        int i = 0;
        while (!result && i<menu.size()) {
            result = name.equalsIgnoreCase(menu.get(i).getName());
            i++;
        }
        return result;
    }
}
