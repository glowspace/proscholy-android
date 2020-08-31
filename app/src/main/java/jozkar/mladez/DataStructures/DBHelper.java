package jozkar.mladez.DataStructures;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Jozkar on 4.4.2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "Favorites.db";
    public static final String TABLE = "favorites";
    public static final String ID = "id";
    public static final int DBVERSION = 1;
    static boolean works = true;


    public DBHelper(Context c){
        super(c, DB_NAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + TABLE + " (" + ID + " TEXT)");
        db.execSQL("create table songs (id TEXT, name TEXT, text TEXT, acordText TEXT, soloText TEXT, language TEXT, alternatives TEXT, original INTEGER, tags TEXT, songbooks TEXT, lid TEXT, search TEXT, searchName TEXT)");
        db.execSQL("create table authors (id INTEGER, name TEXT)");
        db.execSQL("create table tags (id INTEGER, parent TEXT, name TEXT)");
        db.execSQL("create table songbooks (id INTEGER, name TEXT, color TEXT, shortcut TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV){
        addMissingTables(db, oldV, newV);
    }

    public void addMissingTables(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(oldVersion++; oldVersion<=newVersion; oldVersion++){
            switch(oldVersion){
                case 2:
                    db.execSQL("create table songs (id TEXT, name TEXT, text TEXT, acordText TEXT, soloText TEXT, language TEXT, alternatives TEXT, original INTEGER, tags TEXT, songbooks TEXT, lid TEXT, search TEXT, searchName TEXT)");
                    db.execSQL("create table authors (id INTEGER, name TEXT)");
                    db.execSQL("create table tags (id INTEGER, parent, TEXT, name TEXT)");
                    db.execSQL("create table songbooks (id INTEGER, name TEXT, color TEXT, shortcut TEXT)");
                    break;
            }
        }
    }


    public boolean insertRow(String id){
        if(works){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(ID, id);
            db.insert(TABLE, null, cv);
        }
        return true;
    }

    public void insertSongRow(String id, String name, String text, String acordText, String soloText, String language, String alternatives, Boolean original, String tags, String songbooks, String lid, String search, String searchName){
        if(works){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("id", id);
            cv.put("name", name);
            cv.put("text",text);
            cv.put("acordText", acordText);
            cv.put("soloText",soloText);
            cv.put("language",language);
            cv.put("alternatives",alternatives);
            cv.put("original",(original?1:0));
            cv.put("tags",tags);
            cv.put("songbooks",songbooks);
            cv.put("lid",lid);
            cv.put("search", search);
            cv.put("searchName", searchName);
            db.insert("songs", null, cv);
        }
    }

    public void insertSongBookRow(String id, String name, String shortcut, String color){
        if(works){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("id", id);
            cv.put("name", name);
            cv.put("shortcut", shortcut);
            cv.put("color", color);
            db.insert("songbooks", null, cv);
        }
    }

    public void insertAuthorRow(String id, String name){
        if(works){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("id", id);
            cv.put("name", name);
            db.insert("authors", null, cv);
        }
    }

    public void insertTagRow(String id, String parent, String name){
        if(works){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("id", id);
            cv.put("parent", parent);
            cv.put("name", name);
            db.insert("tags", null, cv);
        }
    }

    public void deleteRow(String id, String table){
        if(works) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(table, "id = ?", new String[]{id});

        }
    }

    public int getSongRowsCount(){
        if(works){
            try {
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor res = db.rawQuery("select count(*) from songs", null);
                res.moveToFirst();
                int retVal = res.getInt(0);
                res.close();
                Log.d("RETVAL", retVal+"");
                return retVal;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int getTagsRowsCount(){
        if(works){
            try {
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor res = db.rawQuery("select count(*) from tags", null);
                res.moveToFirst();
                int retVal = res.getInt(0);
                res.close();
                return retVal;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int getAuthorsRowsCount(){
        if(works){
            try {
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor res = db.rawQuery("select count(*) from authors", null);
                res.moveToFirst();
                int retVal = res.getInt(0);
                res.close();
                return retVal;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int getSongBooksRowsCount(){
        if(works){
            try {
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor res = db.rawQuery("select count(*) from songbooks", null);
                res.moveToFirst();
                int retVal = res.getInt(0);
                res.close();
                return retVal;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return 0;
    }


    public void getSongRows(SongDb songs){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select id, name, text, acordText, soloText, language, alternatives, original, tags, songbooks, lid, search, searchName from songs order by id", null);
            res.moveToFirst();
            while (!res.isAfterLast()) {
                String id = res.getString(res.getColumnIndex("id")),
                       name = res.getString(res.getColumnIndex("name")),
                       text = res.getString(res.getColumnIndex("text")),
                       acordText = res.getString(res.getColumnIndex("acordText")),
                       soloText = res.getString(res.getColumnIndex("soloText")),
                       language = res.getString(res.getColumnIndex("language")),
                       alternatives = res.getString(res.getColumnIndex("alternatives")),
                       tags = res.getString(res.getColumnIndex("tags")),
                       songbooks = res.getString(res.getColumnIndex("songbooks")),
                       lid = res.getString(res.getColumnIndex("lid")),
                       search = res.getString(res.getColumnIndex("search")),
                       searchName = res.getString(res.getColumnIndex("searchName"));
                int original = res.getInt(res.getColumnIndex("original"));
                songs.add(new Record(id, name, text, acordText, soloText, language, alternatives, tags, songbooks, lid, search, searchName, original));
                res.moveToNext();
            }
            res.close();

            works = true;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getSongBookRows(SongBooksDb sb){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select id, name, color, shortcut from songbooks order by id", null);
            res.moveToFirst();
            while (!res.isAfterLast()) {
                String id = res.getString(res.getColumnIndex("id")),
                        name = res.getString(res.getColumnIndex("name")),
                        color = res.getString(res.getColumnIndex("color")),
                        shortcut = res.getString(res.getColumnIndex("shortcut"));
                        Log.d("COLOR", color);
                sb.add(new SongBook(id, name, shortcut, color));
                res.moveToNext();
            }
            res.close();

            works = true;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getAuthorRows(SupportDb a){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select id, name from authors order by id", null);
            res.moveToFirst();
            while (!res.isAfterLast()) {
                String id = res.getString(res.getColumnIndex("id")),
                        name = res.getString(res.getColumnIndex("name"));
                a.add(new Record(id, name));
                res.moveToNext();
            }
            res.close();

            works = true;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getTagRows(SupportDb t){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select id, parent, name from tags order by id", null);
            res.moveToFirst();
            while (!res.isAfterLast()) {
                String id = res.getString(res.getColumnIndex("id")),
                        name = res.getString(res.getColumnIndex("name")),
                        parent = res.getString(res.getColumnIndex("parent"));
                t.add(new Record(id, parent, name));
                res.moveToNext();
            }
            res.close();

            works = true;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Favorites> getRows(){
        ArrayList<Favorites> ar = new ArrayList<Favorites>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select " + ID + " from " + TABLE, null);
            res.moveToFirst();
            while (!res.isAfterLast()) {
                String id = res.getString(res.getColumnIndex(ID));
                ar.add(new Favorites(id));
                res.moveToNext();
            }
            res.close();

            works = true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return ar;
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}
