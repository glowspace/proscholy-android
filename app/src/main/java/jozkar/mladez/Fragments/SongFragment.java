package jozkar.mladez.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import jozkar.mladez.App;
import jozkar.mladez.BottomSheet.SongBottomSheet;
import jozkar.mladez.DataStructures.Favorites;
import jozkar.mladez.DataStructures.Record;
import jozkar.mladez.MainScreen;
import jozkar.mladez.R;
import jozkar.mladez.ScaleListener;
import jozkar.mladez.Song;
import jozkar.mladez.Splash;
import jozkar.mladez.Translation;

public class SongFragment extends Fragment {

    Record r;
    ScaleGestureDetector sgd;
    TextView tv, notes;
    View view;
    int id, rotation;
    String sloka;
    boolean favorite;
    static int splitId = -1;
    float scale;

    public SongFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //initializing toolbar
        id = getArguments().getInt("index");
        favorite = getArguments().getBoolean("favorite");
        scale = PreferenceManager.getDefaultSharedPreferences(getContext()).getFloat("textScale", 17.0f);
        sloka = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("sloka", "0");
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().remove("sloka").apply();

        try{

            if(favorite){
                Favorites f = App.songs.favorites.get(id);
                r = App.songs.getById(f.getNumber());
            }else{
                r = Song.songlist.db.get(id);
            }

        }catch(Exception e){
            Intent k = new Intent(getContext(), Splash.class);
            startActivity(k);

        }

        rotation = getResources().getConfiguration().orientation;

        if(rotation == Configuration.ORIENTATION_LANDSCAPE && getResources().getConfiguration().smallestScreenWidthDp >= 600 && !r.getNoty().equals("")) {
            splitId = id;
        }

        if(splitId == id){
            view = inflater.inflate(R.layout.split_fragment_song, container, false);
        }else{
            view = inflater.inflate(R.layout.fragment_song, container, false);
        }

        setHasOptionsMenu(true);

        if(splitId == id && !r.getNoty().equals("")){


            tv = (TextView) view.findViewById(R.id.text);
            notes = (TextView) view.findViewById(R.id.notes);
            sgd = new ScaleGestureDetector(inflater.getContext(),new ScaleListener(tv,getContext()));
            if(MainScreen.serif){
                tv.setTypeface(Typeface.SERIF);
            }

            tv.setText(Html.fromHtml(r.getSoloText(), new Image(false, getContext()), null));

            notes.setText(Html.fromHtml(r.getNoty(), new Image(true, getContext()), null));

            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                    if(rotation == Configuration.ORIENTATION_LANDSCAPE) {
                        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth() / 2;
                        if(notes.getWidth() > width) {
                            view.findViewById(R.id.nested).setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
                        }

                    }else{
                        int height = getActivity().getWindowManager().getDefaultDisplay().getHeight() / 2;
                        if(notes.getHeight() > height) {
                            view.findViewById(R.id.nested).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height));
                        }

                    }
                }
            });


            view.findViewById(R.id.ntext).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getPointerCount() == 2) {
                        sgd.onTouchEvent(event);
                        return false;
                    }

                    return false;
                }
            });
        }else{
            tv = (TextView) view.findViewById(R.id.text);
            sgd = new ScaleGestureDetector(inflater.getContext(),new ScaleListener(tv,getContext()));
            if(MainScreen.serif){
                tv.setTypeface(Typeface.SERIF);
            }

            Spannable htmls = (Spannable) Html.fromHtml((MainScreen.note?(r.getNoty().equals("")?(MainScreen.acord?r.getAcordText():r.getSoloText()):r.getText()):(MainScreen.acord?r.getAcordText():r.getSoloText())), new Image(true, getContext()), null);
            tv.setText(htmls);

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getPointerCount() == 2) {
                        sgd.onTouchEvent(event);
                        return false;
                    }
                    return false;
                }
            });
            onRestoreInstanceState(savedInstanceState, sloka);
        }

        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, scale);


        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if(tv != null && scale > 1.0f) {
                scale = PreferenceManager.getDefaultSharedPreferences(getContext()).getFloat("textScale", 17.0f);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, scale);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        NestedScrollView scrollView = (NestedScrollView) view.findViewById(R.id.scroller);
        if(scrollView == null){
            scrollView = (NestedScrollView) view.findViewById(R.id.ntext);
        }
        if(tv != null && scrollView != null) {
            try {
                final int firstVisableLineOffset = tv.getLayout().getLineForVertical(scrollView.getScrollY());
                final int firstVisableCharacterOffset = tv.getLayout().getLineStart(firstVisableLineOffset);
                outState.putInt("scrollState", firstVisableCharacterOffset);
            } catch (Exception e){
                //noop
            }
        }
    }



    protected void onRestoreInstanceState(Bundle savedInstanceState, final String sloka) {
        final NestedScrollView scrollView = (NestedScrollView) view.findViewById(R.id.scroller);
        if(savedInstanceState == null){
            if(MainScreen.remote && !sloka.equals("0")){
                scrollView.post(new Runnable() {
                    public void run() {
                        final int firstVisableLineOffset = tv.getLayout().getLineForOffset(tv.getText().toString().indexOf("\n" + sloka + ". "));
                        final int pixelOffset = tv.getLayout().getLineTop(firstVisableLineOffset);
                        scrollView.scrollTo(0, pixelOffset);
                    }
                });
            }
        } else {
            final int firstVisableCharacterOffset = savedInstanceState.getInt("scrollState");
            savedInstanceState.clear();
            scrollView.post(new Runnable() {
                public void run() {
                    try {
                        final int firstVisableLineOffset = tv.getLayout().getLineForOffset(firstVisableCharacterOffset);

                        final int pixelOffset = tv.getLayout().getLineTop(firstVisableLineOffset);
                        scrollView.scrollTo(0, pixelOffset);
                    }catch (Exception e){
                        Log.d("ERROR-SONGFRAGMENT", e.getMessage());
                    }
                }
            });
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        try{

            menuInflater.inflate(R.menu.translation_menu, menu);

            if(favorite){
                    if(App.songs.favorites.get(id).getRemove()){
                        menuInflater.inflate(R.menu.menu_main_off, menu);
                    }else{
                        menuInflater.inflate(R.menu.menu_main_on, menu);
                    }
            }else{
                    Record rd = Song.songlist.db.get(id);
                    if (App.songs.checkFavorite(rd.getId())) {
                        menuInflater.inflate(R.menu.menu_main_on, menu);
                    } else {
                        menuInflater.inflate(R.menu.menu_main_off, menu);
                    }

            }

            if(MainScreen.note && !r.getNoty().equals("")){

                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                        && getResources().getConfiguration().smallestScreenWidthDp >= 600) {

                } else {
                    if(splitId == id){
                        menuInflater.inflate(R.menu.lock_on, menu);
                    }else{
                        menuInflater.inflate(R.menu.lock_off, menu);
                    }
                }
            }


            menuInflater.inflate(R.menu.song_menu, menu);

        }catch(Exception e){
            Intent k = new Intent(getContext(), Splash.class);
            startActivity(k);
        }
        super.onCreateOptionsMenu(menu, menuInflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu){
        try{

            switch (menu.getItemId()){
                case R.id.action_lock:
                    if(splitId == id){
                        menu.setIcon(R.mipmap.unlock);
                        splitId = -1;
                    }else{
                        menu.setIcon(R.mipmap.lock);
                        splitId = id;
                    }
                    getActivity().recreate();
                    break;
                case R.id.action_favorite:
                    if(favorite) {

                        if(App.songs.favorites.get(id).getRemove()){
                            App.songs.favorites.get(id).remove();
                            menu.setIcon(R.mipmap.star_on);
                        }else{
                            App.songs.favorites.get(id).remove();
                            menu.setIcon(R.mipmap.star_off);
                        }

                    }else{
                        Record rd = Song.songlist.db.get(id);
                        if (App.songs.checkFavorite(rd.getId())) {
                            menu.setIcon(R.mipmap.star_off);
                            App.songs.unSetFavorite(rd.getId());
                        } else {
                            menu.setIcon(R.mipmap.star_on);
                            App.songs.setFavorite(rd.getId());
                        }

                    }
                    break;
                case R.id.translation:
                    Intent i =  new Intent(getContext(), Translation.class);
                    i.putExtra("song", r.getId());
                    startActivity(i);
                    break;
                case R.id.songMenu:
                    SongBottomSheet sbs = new SongBottomSheet();
                    sbs.show(getParentFragmentManager(), "song");
                    break;
            }
        }catch(Exception e){
            Intent k = new Intent(getContext(), Splash.class);
            startActivity(k);
        }

        return true;
    }

    public class Image implements Html.ImageGetter {

        boolean notes;
        Context c;


        public Image (boolean drawNote, Context c){
            this.c = c;
            this.notes = drawNote;
        }

        public Drawable getDrawable(String source) {
            int idd;

            idd = this.c.getResources().getIdentifier(source, "drawable", c.getPackageName());
            if (idd == 0 || !MainScreen.note || !notes) {
                // prevent a crash if the resource can't be found
                return getResources().getDrawable(R.drawable.none);
            } else {
                Display display = getActivity().getWindowManager().getDefaultDisplay();

                Drawable d;
                try{
                    d = c.getResources().getDrawable(idd);
                }catch (OutOfMemoryError e){
                    d = c.getResources().getDrawable(R.drawable.memory);
                }

                float mult = 0.8f;

                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    mult = 0.9f;
                }

                if(splitId == id && (rotation == Configuration.ORIENTATION_LANDSCAPE)) {
                    mult = mult * (display.getWidth() / 2) / d.getIntrinsicWidth();
                }else{
                    mult = mult * display.getWidth() / d.getIntrinsicWidth();
                }
                if(mult > 2.5f){
                    mult = 2.5f;
                }
                d.setBounds(0, 0, (int) (d.getIntrinsicWidth() * mult), (int) (d.getIntrinsicHeight() * mult));

                if(MainScreen.night) {
                    d.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                }else{
                    d.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
                }

                return d;
            }
        }
    }
}
