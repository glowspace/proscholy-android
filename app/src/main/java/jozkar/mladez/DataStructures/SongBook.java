package jozkar.mladez.DataStructures;

import android.graphics.Color;

/**
 * Created by Jozkar on 11.11.2015.
 */
public class SongBook {
    String id, name, shortcut;
    int color;

    public SongBook(String id, String name, String shortcut, String color){
        this.id = id;
        this.name = name;
        this.shortcut = shortcut;

        this.color = Color.parseColor(color);
    }

    public SongBook(String id, String number){
        this.id = id;
        this.name = number;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return name;
    }

    public String getShortcut() {
        return shortcut;
    }

    public int getColor() {
        return color;
    }
}
