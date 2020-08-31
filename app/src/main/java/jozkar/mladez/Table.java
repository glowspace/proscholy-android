package jozkar.mladez;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.futuremind.recyclerviewfastscroll.FastScroller;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jozkar.mladez.Adapters.RecyclerViewAdapter;
import jozkar.mladez.DataStructures.Record;
import jozkar.mladez.DataStructures.RowData;
import jozkar.mladez.DataStructures.SongBook;

public class Table extends AppCompatWrapper {

    Toolbar toolbar;
    SearchView searchView;
    public static String songbook, songbookShortCut,search;
    public static RecyclerView recyclerView;
    public static RecyclerViewAdapter adapter;
    public static List<RowData> rowListItem, searchList;
    public static Context context;
    public static FastScroller fastScroller;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_table);

        songbook = getIntent().getStringExtra("songbook");
        search = getIntent().getStringExtra("search");

        //initializing toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (SearchView) findViewById(R.id.search);

        rowListItem = new ArrayList<>();

        fastScroller = (FastScroller) findViewById(R.id.fastscroll);
        if(songbook.equals("")) {
            if(search != null){
                rowListItem = getSearch(search);
                toolbar.setTitle(String.format(getString(R.string.searchResult), rowListItem.size()));
                searchView.setVisibility(View.GONE);
            } else {
                toolbar.setTitle(R.string.alphabet);
                rowListItem = getAlphabeticalOrder();
            }
            toolbar.setBackgroundColor(getResources().getColor(R.color.gray));
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(ContextCompat.getColor(Table.this, R.color.gray));
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
        }else{
            toolbar.setTitle(App.songbooks.getById(songbook).getName());
            toolbar.setBackgroundColor(App.songbooks.getById(songbook).getColor());
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(App.songbooks.getById(songbook).getColor());
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
            rowListItem = getSongBook(songbook);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(this, rowListItem, false, false);
        recyclerView.setAdapter(adapter);
        fastScroller.setRecyclerView(recyclerView);

        if(!toolbar.getTitle().equals(getString(R.string.alphabet))){
            fastScroller.setVisibility(View.GONE);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(s == null || s.isEmpty()) {
                    adapter = new RecyclerViewAdapter(context, rowListItem, false, false);
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.isEmpty()){
                    adapter = new RecyclerViewAdapter(context, rowListItem, false, false);
                    recyclerView.setAdapter(adapter);
                    if(songbook.equals("")){
                        fastScroller.setVisibility(View.VISIBLE);
                    } else {
                        fastScroller.setVisibility(View.GONE);
                    }
                } else {
                    searchList = getSearch(s, (songbook.isEmpty()));
                    adapter = new RecyclerViewAdapter(context, searchList, false, false);
                    recyclerView.setAdapter(adapter);
                    fastScroller.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }

    public ArrayList<RowData> getAlphabeticalOrder(){
        App.songs.sorting(App.byName);
        ArrayList<RowData> r = new ArrayList<>();
        for(Record rec : App.songs.db){
            r.add(new RowData("Zp" + rec.getId(), rec.getName(), "", rec.getId()));
        }
        return r;
    }

    public ArrayList<RowData> getSongBook(String songbook){
        ArrayList<RowData> r = new ArrayList<>();
        for(Record rec : App.songs.db){
            if(!rec.getSongbooks().isEmpty()) {
                for (SongBook s : rec.getSongbooks()) {
                    if (s.getId().equals(songbook)) {
                        songbookShortCut = App.songbooks.getById(s.getId()).getShortcut();
                        r.add(new RowData(songbookShortCut + s.getNumber(), rec.getName(), songbook, rec.getId()));
                    }
                }
            }
        }

        Collections.sort(r, Table.CompareByNumber);
        return r;
    }

    public ArrayList<RowData> getSearch(String search){
        App.songs.sorting(App.byName);
        ArrayList<RowData> rNumber = new ArrayList<>(), rStartName = new ArrayList<>(), rName = new ArrayList<>(), rContent = new ArrayList<>();

        try{

            String normSearch = Normalizer.normalize(search, Normalizer.Form.NFD);
            normSearch = App.pattern.matcher(normSearch).replaceAll("");


            for(Record rec : App.songs.db){
                boolean set = false;
                StringBuilder sb;
                for(SongBook s : rec.getSongbooks()){
                    sb = new StringBuilder();
                    sb.append(App.songbooks.getById(s.getId()).getShortcut());
                    sb.append(s.getNumber());

                    String nums = Normalizer.normalize(sb.toString(), Normalizer.Form.NFD);
                    nums = App.pattern.matcher(nums).replaceAll("");

                    if(!set && nums.matches("(?i:.*" + normSearch + ".*)")){
                        rNumber.add(new RowData(App.songbooks.getById(s.getId()).getShortcut() + s.getNumber(), rec.getName(), normSearch, rec.getId(), true));
                        set = true;
                    }
                }

                if(!set){
                    sb = new StringBuilder();
                    sb.append("Zp");
                    sb.append(rec.getId());

                    String nums = Normalizer.normalize(sb.toString(), Normalizer.Form.NFD);
                    nums = App.pattern.matcher(nums).replaceAll("");

                    if(nums.matches("(?i:.*" + normSearch + ".*)")){
                        rNumber.add(new RowData(sb.toString(), rec.getName(), normSearch, rec.getId(), true));
                        set = true;
                    }
                }

                if(!set) {
                    if (rec.getSearchName().matches("(?i:" + normSearch + ".*)")) {
                        rStartName.add(new RowData(rec.getId(), rec.getName(), normSearch, rec.getId(), true));
                        set = true;
                    }
                }

                if(!set) {
                    if (rec.getSearchName().matches("(?i:.*" + normSearch + ".*)")) {
                        rName.add(new RowData(rec.getId(), rec.getName(), normSearch, rec.getId(), true));
                        set = true;
                    }
                }

                if(!set) {
                    if (rec.getSearch().matches("(?i:.*" + normSearch + ".*)")) {
                        rContent.add(new RowData(rec.getId(), rec.getName(), normSearch, rec.getId(), true));
                    }
                }
            }
        }catch(Exception e){
            Intent k = new Intent(this, Splash.class);
            startActivity(k);
        }

        Collections.sort(rNumber, CompareByNumber);
        rNumber.addAll(rStartName);
        rNumber.addAll(rName);
        rNumber.addAll(rContent);

        return rNumber;
    }

    public ArrayList<RowData> getSearch(String search, boolean all){
        App.songs.sorting(App.byName);
        ArrayList<RowData> rNumber = new ArrayList<>(), rStartName = new ArrayList<>(), rName = new ArrayList<>(), rContent = new ArrayList<>();

        try{

            String normSearch = Normalizer.normalize(search, Normalizer.Form.NFD);
            normSearch = App.pattern.matcher(normSearch).replaceAll("");

            if(all){
                for(Record rec : App.songs.db){
                    boolean set = false;
                    StringBuilder sb;
                    for(SongBook s : rec.getSongbooks()){
                        sb = new StringBuilder();
                        sb.append(App.songbooks.getById(s.getId()).getShortcut());
                        sb.append(s.getNumber());

                        String nums = Normalizer.normalize(sb.toString(), Normalizer.Form.NFD);
                        nums = App.pattern.matcher(nums).replaceAll("");

                        if(!set && nums.matches("(?i:.*" + normSearch + ".*)")){
                            rNumber.add(new RowData(App.songbooks.getById(s.getId()).getShortcut() + s.getNumber(), rec.getName(), search, rec.getId(), true));
                            set = true;
                        }
                    }

                    if(!set){
                        sb = new StringBuilder();
                        sb.append(rec.getId());

                        String nums = Normalizer.normalize(sb.toString(), Normalizer.Form.NFD);
                        nums = App.pattern.matcher(nums).replaceAll("");

                        if(nums.matches("(?i:.*" + normSearch + ".*)")){
                            rNumber.add(new RowData(sb.toString(), rec.getName(), search, rec.getId(), true));
                            set = true;
                        }
                    }

                    if(!set) {
                        if (rec.getSearchName().matches("(?i:" + normSearch + ".*)")) {
                            rStartName.add(new RowData(rec.getId(), rec.getName(), search, rec.getId(), true));
                            set = true;
                        }
                    }

                    if(!set) {
                        if (rec.getSearchName().matches("(?i:.*" + normSearch + ".*)")) {
                            rName.add(new RowData(rec.getId(), rec.getName(), search, rec.getId(), true));
                            set = true;
                        }
                    }

                    if(!set) {
                        if (rec.getSearch().matches("(?i:.*" + normSearch + ".*)")) {
                            rContent.add(new RowData(rec.getId(), rec.getName(), search, rec.getId(), true));
                        }
                    }
                }
            } else {
                for (Record rec : App.songs.db) {
                    if (!rec.getSongbooks().isEmpty()) {
                        for (SongBook s : rec.getSongbooks()) {
                            if (s.getId().equals(songbook)) {
                                boolean set = false;

                                StringBuilder sb = new StringBuilder();
                                sb.append(App.songbooks.getById(s.getId()).getShortcut());
                                sb.append(s.getNumber());

                                String nums = Normalizer.normalize(sb.toString(), Normalizer.Form.NFD);
                                nums = App.pattern.matcher(nums).replaceAll("");

                                if (nums.matches("(?i:.*" + normSearch + ".*)")) {
                                    rNumber.add(new RowData(App.songbooks.getById(s.getId()).getShortcut() + s.getNumber(), rec.getName(), songbook, rec.getId()));
                                    set = true;
                                }

                                if (!set) {
                                    if (rec.getSearchName().matches("(?i:" + normSearch + ".*)")) {
                                        rStartName.add(new RowData(App.songbooks.getById(s.getId()).getShortcut() + s.getNumber(), rec.getName(), songbook, rec.getId()));
                                        set = true;
                                    }
                                }

                                if (!set) {
                                    if (rec.getSearchName().matches("(?i:.*" + normSearch + ".*)")) {
                                        rName.add(new RowData(App.songbooks.getById(s.getId()).getShortcut() + s.getNumber(), rec.getName(), songbook, rec.getId()));
                                        set = true;
                                    }
                                }

                                if (!set) {
                                    if (rec.getSearch().matches("(?i:.*" + normSearch + ".*)")) {
                                        rContent.add(new RowData(App.songbooks.getById(s.getId()).getShortcut() + s.getNumber(), rec.getName(), songbook, rec.getId()));
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }catch(Exception e){
            Intent k = new Intent(this, Splash.class);
            startActivity(k);
        }

        Collections.sort(rNumber, CompareByNumber);
        rNumber.addAll(rStartName);
        rNumber.addAll(rName);
        rNumber.addAll(rContent);

        return rNumber;
    }

    public static Comparator<RowData> CompareByNumber = new Comparator<RowData>() {

        public int compare(RowData r1, RowData r2) {
            String number1 = r1.getNumber().replaceAll("\\D","");
            String number2 = r2.getNumber().replaceAll("\\D","");

            int num1 = number1.isEmpty()?0:Integer.parseInt(number1),
            num2 = number2.isEmpty()?0:Integer.parseInt(number2);

            //ascending order
            return num1-num2;

            //descending order
            //return number2.compareTo(number1);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int Id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (Id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}