package jozkar.mladez.DataStructures;

import android.content.Context;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import jozkar.mladez.App;
import jozkar.mladez.MainScreen;

/**
 * Created by Jozkar on 11.11.2015.
 */
public class SongDb{

    public ArrayList<Record> db;
    public ArrayList<Favorites> favorites;
    DBHelper mydb;

    public SongDb(){
        db = new ArrayList<Record>();
    }

    public SongDb(ArrayList<Favorites> f, Context context) {
        db = new ArrayList<Record>();
        favorites = f;
        Collections.sort(favorites, SongDb.CompareById);
        mydb = new DBHelper(context);
    }

    public void add(Record r) {
        db.add(r);

    }

    public Record getById(String id) {
        for (Record r : db) {
            if (r.id.equals(id)) {
                return r;
            }
        }
        return null;
    }

    public int getIndexByName(String name){
        for(int i = 0; i < db.size(); i++){
            if(db.get(i).id.equals(name)){
                return i;
            }
        }
        return -1;
    }

    public boolean checkFavorite(String number){
        for(int i = 0; i < favorites.size(); i++){
            Favorites f = favorites.get(i);
            if(f.getNumber().equals(number)){
                return true;
            }
        }
        return false;
    }

    public int getFavoriteId(String number){
        for(int i = 0; i < favorites.size(); i++){
            Favorites f = favorites.get(i);
            if(f.getNumber().equals(number)){
                return i;
            }
        }
        return -1;
    }

    public void controlFavorites(){
        for(int i = 0, id = -1; i < favorites.size(); i++){
            id = getIndexByName(favorites.get(i).getNumber());
            if(id < 0){
                unSetFavorite(favorites.get(i).getNumber());
            }
        }

    }

    public void setFavorite(String number){
        if(!checkFavorite(number)){
            favorites.add(new Favorites(number));
        }
        mydb.insertRow(number);
        Collections.sort(favorites, SongDb.CompareById);
    }

    public void unSetFavorite(String number) {
        int i = getFavoriteId(number);
        favorites.remove(i);
        mydb.deleteRow(number, "favorites");
        Collections.sort(favorites, SongDb.CompareById);
    }

    public void sorting(int by){

        switch (by){
            case App.byName:
                Collections.sort(db, SongDb.CompareByName);
                break;
             default:
                Collections.sort(db, SongDb.CompareByName);
        }
    }

    public static Comparator<Record> CompareByNumber = new Comparator<Record>() {

        public int compare(Record r1, Record r2) {
            String number1 = r1.id;
            String number2 = r2.id;

            //ascending order
            return number1.compareTo(number2);

            //descending order
            //return number2.compareTo(number1);
        }
    };

    public static Comparator<Record> CompareByName = new Comparator<Record>() {

        public int compare(Record r1, Record r2) {
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

    /*public static Comparator<Record> CompareByGroupAndNumber = new Comparator<Record>() {

        public int compare(Record r1, Record r2) {
            String group1 = r1.skupina;
            String group2 = r2.skupina;
            int dateComparison = group1.compareTo(group2);
            return dateComparison == 0 ? r1.id.compareTo(r2.id) : dateComparison;
        }
    };*/


    public static Comparator<Favorites> CompareById = new Comparator<Favorites>() {

        public int compare(Favorites r1, Favorites r2) {
            String group1 = r1.getNumber();
            String group2 = r2.getNumber();
            int dateComparison = group1.compareTo(group2);
            return dateComparison;
        }
    };
}
