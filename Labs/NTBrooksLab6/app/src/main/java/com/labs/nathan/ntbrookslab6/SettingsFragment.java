package com.labs.nathan.ntbrookslab6;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences myPreferences = getPreferenceManager().getSharedPreferences();
        onSharedPreferenceChanged(myPreferences, "name");
        onSharedPreferenceChanged(myPreferences, "years_remaining");
        onSharedPreferenceChanged(myPreferences, "home_world");
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
        Log.d("PreferenceChanged", "Preference: " + key + " changed");
        if(key.equalsIgnoreCase("name")) {
            findPreference(key).setSummary(sharedPreferences.getString(key, "(unknown)"));
        }
        if(key.equalsIgnoreCase("years_remaining")) {
            findPreference(key).setSummary(sharedPreferences.getString(key, "(unknown)"));
        }
        if(key.equalsIgnoreCase("home_world")) {
            String[] homeWorlds = getResources().getStringArray(R.array.home_worlds);
            findPreference(key).setSummary(homeWorlds[new Integer(sharedPreferences.getString(key, "0"))]);
        }
    }
}
