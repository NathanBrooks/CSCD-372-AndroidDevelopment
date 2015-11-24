package com.labs.nathan.ntbrookslab6;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String SETTINGS = "SETTINGS";
    private static final String FIRST_USE= "FIRST_USE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);

        if(savedInstanceState == null) {
            FragmentTransaction trans = getFragmentManager().beginTransaction();
            trans.add(R.id.fragment_holder, new MainFragment());
            trans.commit();
        }

        boolean firstUse = prefs.getBoolean(FIRST_USE, true);
        if(firstUse) {
            prefs.edit().putBoolean(FIRST_USE, false).apply();
            loadPreferenceMenu();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            loadPreferenceMenu();
            return true;
        }
        if (id == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("About")
                    .setMessage("Nathan T. Brooks, CSCD 372, Fall 2015, Lab 6")
                    .setNeutralButton("OK", null)
                    .setCancelable(false)
                    .create()
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fMan = getFragmentManager();
        if(fMan.getBackStackEntryCount() > 0) {
            fMan.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void loadPreferenceMenu() {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        trans.addToBackStack(null);
        trans.replace(R.id.fragment_holder, new SettingsFragment());
        trans.commit();
    }
}
