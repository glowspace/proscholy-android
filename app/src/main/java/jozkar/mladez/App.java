package jozkar.mladez;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.regex.Pattern;

import jozkar.mladez.DataStructures.DBHelper;
import jozkar.mladez.DataStructures.Record;
import jozkar.mladez.DataStructures.SongBook;
import jozkar.mladez.DataStructures.SongBooksDb;
import jozkar.mladez.DataStructures.SongDb;
import jozkar.mladez.DataStructures.SupportDb;

public class App extends Application {

    public static SongDb songs;
    public static SupportDb authors, tags;
    public static SongBooksDb songbooks;
    public static DBHelper mydb;
    public static Pattern pattern;
    public static SharedPreferences SP;
    public static final int byNumber = 0, byName = 1, byGroup = 2, searchResultNumber = 3, searchResultString = 4;

    @Override
    public void onCreate() {
        super.onCreate();

        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        pattern = Pattern.compile("[\\p{InCombiningDiacriticalMarks}]+");
        mydb = new DBHelper(this);
        loadAuthors();
        loadTags();
        loadSongBooks();
        loadSongs();
    }

    private void loadSongs() {
        songs = new SongDb(mydb.getRows(), this);
        if (mydb.getSongRowsCount() < 1) {
            InputStream input = getResources().openRawResource(R.raw.songs);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    String[] val = line.split("::");
                    songs.add(new Record(val[0], val[1], val[2], val[3], val[4], val[5], val[6], val[7], val[8], val[9]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // read it from DB
            mydb.getSongRows(songs);
        }

        songs.controlFavorites();

    }

    private void loadTags() {
        tags = new SupportDb();
        if (mydb.getTagsRowsCount() < 1) {
            InputStream input = getResources().openRawResource(R.raw.tags);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    String[] val = line.split("::");
                    Record r = new Record(val[0], val[1], val[2]);
                    tags.add(r);
                    mydb.insertTagRow(val[0], val[1], val[2]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // read it from DB
            mydb.getTagRows(tags);
        }
    }

    private void loadAuthors() {
        authors = new SupportDb();
        if (mydb.getAuthorsRowsCount() < 1) {
            InputStream input = getResources().openRawResource(R.raw.authors);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    String[] val = line.split("::");
                    Record r = new Record(val[0], val[1]);
                    authors.add(r);
                    mydb.insertAuthorRow(val[0], val[1]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // read it from DB
            mydb.getAuthorRows(authors);
        }
    }

    private void loadSongBooks() {
        songbooks = new SongBooksDb();
        if (mydb.getSongBooksRowsCount() < 1) {
            InputStream input = getResources().openRawResource(R.raw.songbooks);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    String[] val = line.split("::");
                    SongBook sb = new SongBook(val[0], val[1], val[3], val[2]);
                    songbooks.add(sb);
                    mydb.insertSongBookRow(val[0], val[1], val[3], val[2]);
                }
                Collections.sort(songbooks.db, SongBooksDb.byName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // read it from DB
            mydb.getSongBookRows(songbooks);
        }
    }
}
