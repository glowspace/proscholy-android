package jozkar.mladez.DataStructures;

import android.util.Log;

import java.util.ArrayList;

public class SupportDb {
    public ArrayList<Record> db;
    public int noChecked = 0;

    public SupportDb() {
        db = new ArrayList<Record>();
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

    public String [] getArray(){
        String [] res = new String[db.size()];
        for(int i=0; i<db.size();i++){
            res[i]=db.get(i).name;
        }
        return res;
    }

    public String [] getCategories(){
        ArrayList<String> cat = new ArrayList<>();
        String [] retval;
        for(int i=0; i<db.size();i++){

            if(db.get(i).getParent().equals("")){
                cat.add(db.get(i).getId());
            }
        }

        retval = new String[cat.size()];
        cat.toArray(retval);

        return retval;
    }

    public ArrayList<String> getFilter(){
        ArrayList<String> filter = new ArrayList<>();
        for(int i=0; i<db.size();i++){
            if(db.get(i).getChecked()){
                filter.add(db.get(i).getId());
            }
        }
        return filter;
    }
}
