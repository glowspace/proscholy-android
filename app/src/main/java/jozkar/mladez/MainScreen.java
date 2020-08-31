package jozkar.mladez;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import jozkar.mladez.DataStructures.Favorites;
import jozkar.mladez.NavigationHelper.Screen;

import static jozkar.mladez.App.SP;
import static jozkar.mladez.App.songs;

public class MainScreen extends AppCompatWrapper{


    public static Screen current;
    public static SearchView search;
    public static boolean oldNight;
    public static NavigationHelper navigationHelper;

    public static BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(night){
                setTheme(R.style.Dark);
            } else {
                setTheme(R.style.Light);
            }

            if(current == Screen.SETTINGS){
                onStart();
            }

            int [][] states = new int[][] {
                    new int[] { android.R.attr.state_checked},
                    new int[] {}
            };

            int[] colors = new int[] {
                    getResources().getColor(R.color.officialBlue),
                    getResources().getColor(R.color.lightGray),
            };

            switch (item.getItemId()) {
                case R.id.home:
                    navigationHelper.navigate(Screen.HOME);
                    colors[0] = getResources().getColor(R.color.officialBlue);
                    navigation.setItemIconTintList(new ColorStateList(states, colors));
                    navigation.setItemTextColor(new ColorStateList(states, colors));
                    current = Screen.HOME;
                    return true;
                case R.id.hymnals:
                    navigationHelper.navigate(Screen.HYMNALS);
                    colors[0] = getResources().getColor(R.color.officialGreen);
                    navigation.setItemIconTintList(new ColorStateList(states, colors));
                    navigation.setItemTextColor(new ColorStateList(states, colors));
                    current = Screen.HYMNALS;
                    return true;
                case R.id.favorits:
                    navigationHelper.navigate(Screen.FAVORITS);
                    colors[0] = getResources().getColor(R.color.officialRed);
                    navigation.setItemIconTintList(new ColorStateList(states, colors));
                    navigation.setItemTextColor(new ColorStateList(states, colors));
                    current = Screen.FAVORITS;
                    return true;
                default:
                    navigationHelper.navigate(Screen.OTHER);
                    colors[0] = getResources().getColor(R.color.officialYellow);
                    navigation.setItemIconTintList(new ColorStateList(states, colors));
                    navigation.setItemTextColor(new ColorStateList(states, colors));
                    current = Screen.OTHER;
                    return true;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_screen);

        oldNight = night;

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigationHelper = new NavigationHelper(getSupportFragmentManager());
        navigationHelper.navigate(current);

        if(current != null) {
            switch (current) {
                case HOME:
                    navigation.setSelectedItemId(R.id.home);
                    break;
                case HYMNALS:
                    navigation.setSelectedItemId(R.id.hymnals);
                    break;
                case FAVORITS:
                    navigation.setSelectedItemId(R.id.favorits);
                    break;
                case OTHER:
                case SETTINGS:
                case BOOKMARKS:
                    navigation.setSelectedItemId(R.id.other);
                    break;
            }

        }

        if(version < getResources().getInteger(R.integer.version)){
            MaterialAlertDialogBuilder dlgAlert  = new MaterialAlertDialogBuilder(this);
            float dpi = getResources().getDisplayMetrics().density;
            TextView m = new TextView(this);
            Spanned msg = Html.fromHtml(getString(R.string.about_app));
            m.setText(msg);
            m.setLinkTextColor(getResources().getColor(R.color.link));
            m.setMovementMethod(LinkMovementMethod.getInstance());
            m.setPadding((int)(25*dpi), (int)(19*dpi), (int)(25*dpi), (int)(14*dpi));
            dlgAlert.setView(m);
            dlgAlert.setTitle(R.string.news);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
            SP.edit().putInt("oldVersion", getResources().getInteger(R.integer.version)).apply();
        }
    }

    public void showSound(){

        if(mute){
            Snackbar snack = Snackbar.make(findViewById(R.id.placeSnackBar), R.string.volume_changed, Snackbar.LENGTH_LONG).setAction("Action", null);
            snack.getView().setBackgroundColor(getResources().getColor(R.color.red));
            snack.show();
        }
    }

    @Override
    public void onBackPressed(){
        if(current != null && current != Screen.HOME){
            navigationHelper.navigate(Screen.HOME);
            navigation.setSelectedItemId(R.id.home);
            if(current == Screen.SETTINGS){
                current = Screen.HOME;
                if(night){
                    setTheme(R.style.Dark);
                }
                onStart();
            }
            return;
        }


        MaterialAlertDialogBuilder dlgAlert  = new MaterialAlertDialogBuilder(this);
        dlgAlert.setMessage(R.string.close_app);
        dlgAlert.setTitle(R.string.close_title);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dlgAlert.setNegativeButton(R.string.no, null);
        dlgAlert.create().show();
    }

    @Override
    protected void onResume() {
        setSound();
        int index = 0;
        while(index < songs.favorites.size()){
            if(songs.favorites.get(index).getRemove()){
                Favorites f = songs.favorites.get(index);
                songs.unSetFavorite(f.getNumber());
            }else{
                index++;
            }
        }
        if(current == Screen.FAVORITS){
            navigationHelper.navigate(Screen.FAVORITS);
        }
        if(current == Screen.BOOKMARKS){
            navigationHelper.navigate(Screen.BOOKMARKS);
        }

        if(night != oldNight){
            recreate();
        }

        super.onResume();
    }

    @Override
    protected void onStart(){

        boolean set = SP.getBoolean("night", false), ser = SP.getBoolean("serif", false), re = SP.getBoolean("remote", false),
                no = SP.getBoolean("note", true), mu = SP.getBoolean("mute", false), sl = SP.getBoolean("sleep", false),
                lo = SP.getBoolean("lock", false), ac = SP.getBoolean("acord", false), fs = SP.getBoolean("fullscreen", false),
                fss = SP.getBoolean("fullscreenSong", true);

        if (set != night || ser != serif || no != note || mu != mute || sl != sleep || lo != lock || re != remote || ac != acord || fs != fullscreen || fss != fullscreenSong) {
            recreate();
        }

        showSound();

        super.onStart();
    }
}
