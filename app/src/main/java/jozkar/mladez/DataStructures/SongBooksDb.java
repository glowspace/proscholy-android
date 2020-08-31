package jozkar.mladez.DataStructures;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public class SongBooksDb {
    public ArrayList<SongBook> db;

    public SongBooksDb() {
        db = new ArrayList<SongBook>();
    }

    public void add(SongBook r) {
        db.add(r);

    }

    public SongBook getById(String id) {
        for (SongBook r : db) {
            if (r.id.equals(id)) {
                return r;
            }
        }
        return null;
    }

    public String getIdByShortcut(String shortcut) {
        for (SongBook r : db) {
            if (r.getShortcut().equals(shortcut)) {
                return r.getId();
            }
        }
        return null;
    }

    public static Comparator<SongBook> byName = new Comparator<SongBook>() {

        public int compare(SongBook r1, SongBook r2) {
            String name1 = r1.name;
            String name2 = r2.name;

            Collator coll = Collator.getInstance(new Locale("cs"));
            coll.setStrength(Collator.PRIMARY);
            //ascending order
            return coll.compare(name1,name2);

            //descending order
            //return name2.compareTo(name1);
        }
    };
}
