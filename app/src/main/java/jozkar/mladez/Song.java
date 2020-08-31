package jozkar.mladez;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jozkar.mladez.DataStructures.Favorites;
import jozkar.mladez.DataStructures.Record;
import jozkar.mladez.DataStructures.SongBook;
import jozkar.mladez.DataStructures.SongDb;
import jozkar.mladez.Fragments.SongFragment;

import static jozkar.mladez.App.SP;

public class Song extends AppCompatWrapper {

    Toolbar toolbar;
    ViewPager viewPager;
    SongPagerAdapter pagerAdapter;
    static boolean favorite;
    String id;
    public static String songbook;
    static TextView rbm, bbm, gbm, gobm, pbm, grbm;
    public static Record rec;
    public static SongDb songlist;
    ArrayList<String> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(fullscreenSong) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_song);

        id = getIntent().getStringExtra("id");
        songbook = getIntent().getStringExtra("songbook");
        favorite = getIntent().getBooleanExtra("favorites", false);
        list = getIntent().getStringArrayListExtra("list");


        pagerAdapter = new SongPagerAdapter(getSupportFragmentManager(), getApplicationContext(), favorite);

        //initializing toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(songbook == null || songbook.isEmpty()){
            App.songs.sorting(App.byName);
            songlist = new SongDb();
            toolbar.setBackgroundColor(getResources().getColor(R.color.gray));
            if(list != null && !list.isEmpty()){
                for(String rec : list){
                    Record r = App.songs.getById(rec);
                    songlist.add(r);
                }
                Toast.makeText(this,R.string.searchresults,Toast.LENGTH_LONG).show();
            } else {
                for (Record rec : App.songs.db) {
                    songlist.add(rec);
                }
            }
        } else {
            toolbar.setBackgroundColor(App.songbooks.getById(songbook).getColor());
            songlist = new SongDb();
            for(Record rec : App.songs.db){
                if(!rec.getSongbooks().isEmpty()) {
                    for (SongBook s : rec.getSongbooks()) {
                        if (s.getId().equals(songbook)) {
                            songlist.add(rec);
                        }
                    }
                }
            }

            Collections.sort(songlist.db, CompareByNumber);
        }

        int index = -1;

        try {
            if (favorite) {
                index = App.songs.getFavoriteId(id);
            } else {
                index = songlist.getIndexByName(id);
            }

        } catch (Exception e) {
            Intent k = new Intent(this, Splash.class);
            startActivity(k);
        }

        if (index < 0) {
            index = 0;
        }

        //initializing ViewPager
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(index);

        rec = changeTitle(index);

        if(!favorite){
            try {
                String number = SP.getString("redBookmark", "000"),
                       songb =  SP.getString("redBookmarkSongbook",""),
                       songn = SP.getString("redBookmarkSongNumber","");

                rbm = (TextView) findViewById(R.id.redBookmark);

                if (App.songs.getIndexByName(number) >= 0) {
                    rbm.setText(songb + songn);
                    rbm.setVisibility(View.VISIBLE);
                } else {
                    rbm.setText("000");
                    rbm.setVisibility(View.GONE);
                }

                rbm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ID = SP.getString("redBookmark", "000");
                        int ind = songlist.getIndexByName(ID);
                        if(ind >= 0) {
                            viewPager.setCurrentItem(ind, true);
                        } else {
                            String s = SP.getString("redBookmarkSongbook", "");
                            String sb = App.songbooks.getIdByShortcut(s);
                            Intent i = new Intent(getApplicationContext(), Song.class);
                            i.putExtra("id", ID);
                            i.putExtra("songbook", sb);
                            finish();
                            startActivity(i);
                        }
                    }
                });
                number = SP.getString("blueBookmark", "000");
                songb =  SP.getString("blueBookmarkSongbook","");
                songn = SP.getString("blueBookmarkSongNumber","");

                bbm = (TextView) findViewById(R.id.blueBookmark);

                if (App.songs.getIndexByName(number) >= 0) {
                    bbm.setText(songb + songn);
                    bbm.setVisibility(View.VISIBLE);
                } else {
                    bbm.setText("000");
                    bbm.setVisibility(View.GONE);
                }

                bbm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ID = SP.getString("blueBookmark", "000");

                        int ind = songlist.getIndexByName(ID);
                        if(ind >= 0) {
                            viewPager.setCurrentItem(ind, true);
                        } else {
                            String s = SP.getString("blueBookmarkSongbook", "");
                            String sb = App.songbooks.getIdByShortcut(s);
                            Intent i = new Intent(getApplicationContext(), Song.class);
                            i.putExtra("id", ID);
                            i.putExtra("songbook", sb);
                            finish();
                            startActivity(i);
                        }
                    }
                });
                number = SP.getString("greenBookmark", "000");
                songb =  SP.getString("greenBookmarkSongbook","");
                songn = SP.getString("greenBookmarkSongNumber","");

                gbm = (TextView) findViewById(R.id.greenBookmark);

                if (App.songs.getIndexByName(number) >= 0) {
                    gbm.setText(songb+songn);
                    gbm.setVisibility(View.VISIBLE);
                } else {
                    gbm.setText("000");
                    gbm.setVisibility(View.GONE);
                }

                gbm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ID = SP.getString("greenBookmark", "000");
                        int ind = songlist.getIndexByName(ID);
                        if(ind >= 0) {
                            viewPager.setCurrentItem(ind, true);
                        } else {
                            String s = SP.getString("greenBookmarkSongbook", "");
                            String sb = App.songbooks.getIdByShortcut(s);
                            Intent i = new Intent(getApplicationContext(), Song.class);
                            i.putExtra("id", ID);
                            i.putExtra("songbook", sb);
                            finish();
                            startActivity(i);
                        }
                    }
                });
                number = SP.getString("goldBookmark", "000");
                songb =  SP.getString("goldBookmarkSongbook","");
                songn = SP.getString("goldBookmarkSongNumber","");

                gobm = (TextView) findViewById(R.id.goldBookmark);

                if (App.songs.getIndexByName(number) >= 0) {
                    gobm.setText(songb+songn);
                    gobm.setVisibility(View.VISIBLE);
                } else {
                    gobm.setText("000");
                    gobm.setVisibility(View.GONE);
                }

                gobm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ID = SP.getString("goldBookmark", "000");
                        int ind = songlist.getIndexByName(ID);
                        if(ind >= 0) {
                            viewPager.setCurrentItem(ind, true);
                        } else {
                            String s = SP.getString("goldBookmarkSongbook", "");
                            String sb = App.songbooks.getIdByShortcut(s);
                            Intent i = new Intent(getApplicationContext(), Song.class);
                            i.putExtra("id", ID);
                            i.putExtra("songbook", sb);
                            finish();
                            startActivity(i);
                        }
                    }
                });

                number = SP.getString("purpleBookmark", "000");
                songb =  SP.getString("purpleBookmarkSongbook","");
                songn = SP.getString("purpleBookmarkSongNumber","");

                pbm = (TextView) findViewById(R.id.purpleBookmark);

                if (App.songs.getIndexByName(number) >= 0) {
                    pbm.setText(songb+songn);
                    pbm.setVisibility(View.VISIBLE);
                } else {
                    pbm.setText("000");
                    pbm.setVisibility(View.GONE);
                }

                pbm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ID = SP.getString("purpleBookmark", "000");
                        int ind = songlist.getIndexByName(ID);
                        if(ind >= 0) {
                            viewPager.setCurrentItem(ind, true);
                        } else {
                            String s = SP.getString("purpleBookmarkSongbook", "");
                            String sb = App.songbooks.getIdByShortcut(s);
                            Intent i = new Intent(getApplicationContext(), Song.class);
                            i.putExtra("id", ID);
                            i.putExtra("songbook", sb);
                            finish();
                            startActivity(i);
                        }
                    }
                });

                number = SP.getString("grayBookmark", "000");
                songb =  SP.getString("grayBookmarkSongbook","");
                songn = SP.getString("grayBookmarkSongNumber","");

                grbm = (TextView) findViewById(R.id.grayBookmark);

                if (App.songs.getIndexByName(number) >= 0) {
                    grbm.setText(songb+songn);
                    grbm.setVisibility(View.VISIBLE);
                } else {
                    grbm.setText("000");
                    grbm.setVisibility(View.GONE);
                }

                grbm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String ID = SP.getString("grayBookmark", "000");
                        int ind = songlist.getIndexByName(ID);
                        if(ind >= 0) {
                            viewPager.setCurrentItem(ind, true);
                        } else {
                            String s = SP.getString("grayBookmarkSongbook", "");
                            String sb = App.songbooks.getIdByShortcut(s);
                            Intent i = new Intent(getApplicationContext(), Song.class);
                            i.putExtra("id", ID);
                            i.putExtra("songbook", sb);
                            finish();
                            startActivity(i);
                        }
                    }
                });

            }catch (Exception e){
                Intent k = new Intent(this, Splash.class);
                k.putExtra("null", true);
                finish();
                startActivity(k);
            }

        } else {
            findViewById(R.id.bookmarklist).setVisibility(View.GONE);
        }


        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
               rec = changeTitle(position);

            }
        });
    }

    public static void updateBookmarks(){
        if(favorite){
            return;
        }
        String number = SP.getString("redBookmark", "000"),
                songb =  SP.getString("redBookmarkSongbook",""),
                songn = SP.getString("redBookmarkSongNumber","");

        if (App.songs.getIndexByName(number) >= 0) {
            rbm.setText(songb + songn);
            rbm.setVisibility(View.VISIBLE);
        } else {
            rbm.setText("000");
            rbm.setVisibility(View.GONE);
        }

        number = SP.getString("blueBookmark", "000");
        songb =  SP.getString("blueBookmarkSongbook","");
        songn = SP.getString("blueBookmarkSongNumber","");

        if (App.songs.getIndexByName(number) >= 0) {
            bbm.setText(songb + songn);
            bbm.setVisibility(View.VISIBLE);
        } else {
            bbm.setText("000");
            bbm.setVisibility(View.GONE);
        }

        number = SP.getString("greenBookmark", "000");
        songb =  SP.getString("greenBookmarkSongbook","");
        songn = SP.getString("greenBookmarkSongNumber","");

        if (App.songs.getIndexByName(number) >= 0) {
            gbm.setText(songb+songn);
            gbm.setVisibility(View.VISIBLE);
        } else {
            gbm.setText("000");
            gbm.setVisibility(View.GONE);
        }

        number = SP.getString("goldBookmark", "000");
        songb =  SP.getString("goldBookmarkSongbook","");
        songn = SP.getString("goldBookmarkSongNumber","");

        if (App.songs.getIndexByName(number) >= 0) {
            gobm.setText(songb+songn);
            gobm.setVisibility(View.VISIBLE);
        } else {
            gobm.setText("000");
            gobm.setVisibility(View.GONE);
        }

        number = SP.getString("purpleBookmark", "000");
        songb =  SP.getString("purpleBookmarkSongbook","");
        songn = SP.getString("purpleBookmarkSongNumber","");


        if (App.songs.getIndexByName(number) >= 0) {
            pbm.setText(songb+songn);
            pbm.setVisibility(View.VISIBLE);
        } else {
            pbm.setText("000");
            pbm.setVisibility(View.GONE);
        }

        number = SP.getString("grayBookmark", "000");
        songb =  SP.getString("grayBookmarkSongbook","");
        songn = SP.getString("grayBookmarkSongNumber","");

        if (App.songs.getIndexByName(number) >= 0) {
            grbm.setText(songb+songn);
            grbm.setVisibility(View.VISIBLE);
        } else {
            grbm.setText("000");
            grbm.setVisibility(View.GONE);
        }
    }

    public static Comparator<Record> CompareByNumber = new Comparator<Record>() {

        public int compare(Record r1, Record r2) {
            String number1 = "", number2 = "";

            if(!r1.getSongbooks().isEmpty()) {
                for (SongBook s : r1.getSongbooks()) {
                    if (s.getId().equals(songbook)) {
                        number1 = s.getNumber().replaceAll("\\D","");
                    }
                }
            }
            if(!r2.getSongbooks().isEmpty()) {
                for (SongBook s : r2.getSongbooks()) {
                    if (s.getId().equals(songbook)) {
                        number2 = s.getNumber().replaceAll("\\D","");
                    }
                }
            }

            int num1 = number1.isEmpty()?0:Integer.parseInt(number1),
                num2 = number2.isEmpty()?0:Integer.parseInt(number2);

            //ascending order
            return num1-num2;

            //descending order
            //return number2.compareTo(number1);
        }
    };

    public Record changeTitle(int position){
        Record r;

        try{

            if(favorite){
                    Favorites f = App.songs.favorites.get(position);
                    r = songlist.getById(f.getNumber());

            }else {
                r = songlist.db.get(position);
            }

            setActionBarTitle(r.getId());

            return r;
        }catch(Exception e){
            Intent k = new Intent(this, Splash.class);
            startActivity(k);
        }
        return null;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }


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

class SongPagerAdapter extends FragmentStatePagerAdapter {
    Fragment fragment = null;
    Context c;
    boolean favor;

    public SongPagerAdapter(FragmentManager fm, Context context, boolean favorite) {
        super(fm);
        c=context;
        favor = favorite;
    }

    @Override
    public Fragment getItem(int position) {
        //Based upon the position you can call the fragment you need here
        //here i have called the same fragment for all the instances
        fragment = new SongFragment();

        Bundle args = new Bundle();
        args.putInt("index", position);
        args.putBoolean("favorite", favor);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public int getCount() {
        // Returns the number of tabs (If you need 4 tabs change it to 4)
        int tabs = 0;
        try{
            if(favor){
                tabs = App.songs.favorites.size();
            }else{
                tabs = Song.songlist.db.size();

            }

        }catch(Exception e){
            Intent k = new Intent(c, Splash.class);
            k.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(k);
        }

        return tabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}
