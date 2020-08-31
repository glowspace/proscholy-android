package jozkar.mladez;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import jozkar.mladez.Fragments.BookmarksFragment;
import jozkar.mladez.Fragments.FavoritsFragment;
import jozkar.mladez.Fragments.HomeFragment;
import jozkar.mladez.Fragments.HymnalsFragment;
import jozkar.mladez.Fragments.OthersFragment;
import jozkar.mladez.Fragments.PreferencesFragment;

public class NavigationHelper {

    FragmentManager sFM;


    public NavigationHelper(FragmentManager f){
        this.sFM = f;
    }

    public void navigate(Screen s){


        Fragment f = getFragment(s);
        replaceFragment(f);
    }

    private Fragment getFragment(Screen s){
       Fragment f;
       if(s == null){
            f = new HomeFragment();
            return f;
        }
        switch (s){
            case HOME:
                f = new HomeFragment();
                break;
            case HYMNALS:
                f = new HymnalsFragment();
                break;
            case FAVORITS:
                f = new FavoritsFragment();
                break;
            case SETTINGS:
                MainScreen.current = Screen.SETTINGS;
                f = new PreferencesFragment();
                break;
            case OTHER:
                f = new OthersFragment();
                break;
            case BOOKMARKS:
                MainScreen.current = Screen.BOOKMARKS;
                f = new BookmarksFragment();
                break;
            default:
                    throw new IllegalArgumentException("Obrazovka neexistuje");
        }
        return f;
    }

    private void replaceFragment(Fragment f){
        sFM.beginTransaction().replace(R.id.fragment, f).commit();
    }

    public enum Screen {
        HOME,
        HYMNALS,
        FAVORITS,
        SETTINGS,
        OTHER,
        BOOKMARKS
    }
}
