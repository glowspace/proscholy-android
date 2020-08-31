package jozkar.mladez.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceFragmentCompat;

import jozkar.mladez.R;

/**
 * Created by Jozkar on 13.11.2015.
 */

public class PreferencesFragment extends PreferenceFragmentCompat {

    public PreferencesFragment() {
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference, rootKey);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }
}