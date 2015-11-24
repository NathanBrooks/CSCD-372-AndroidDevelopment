package com.labs.nathan.ntbrookslab5;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentUpdateListener {

    private String current_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            current_details = savedInstanceState.getString("detail_string");
        } else {
            current_details = getString(R.string.hello_blank_fragment); // initialize to the fragment
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(!getResources().getBoolean(R.bool.dual_pane)) {
            if (savedInstanceState == null) {
                Log.d("onCreate", "placing main fragment back");
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.add(R.id.mainHolder, new MainFragment());
                trans.commit();
            } else {
                Log.d("onCreate", "updating detail fragment if existing");
                DetailFragment detailFragment = (DetailFragment) getFragmentManager().findFragmentByTag("DETAIL_FRAGMENT");
                if(detailFragment != null) {
                    detailFragment.setDisplayString(current_details);
                }
            }
        } else {
            FragmentManager fMan = getFragmentManager();

            fMan.popBackStack(0,
                    fMan.POP_BACK_STACK_INCLUSIVE);

            DetailFragment detailFragment = (DetailFragment)fMan.findFragmentById(R.id.detailHolder);
            detailFragment.setDisplayString(current_details);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("detail_string", current_details);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Nathan Brooks Lab 5", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentUpdate(String input) {
        current_details = input;
        FragmentManager fManager = getFragmentManager();

        if(getResources().getBoolean(R.bool.dual_pane)) {
            DetailFragment detailFragment = (DetailFragment) fManager.findFragmentById(R.id.detailHolder);
            detailFragment.setDisplayString(current_details);
        } else {
            DetailFragment detailFragment = (DetailFragment) fManager.findFragmentByTag("DETAIL_FRAGMENT");
            if(detailFragment != null) {
                detailFragment.setDisplayString(current_details);
            } else {
                FragmentTransaction trans = fManager.beginTransaction(); // python programmers just lost their mind
                    detailFragment = new DetailFragment();
                    trans.replace(R.id.mainHolder, detailFragment, "DETAIL_FRAGMENT");
                    Log.d("onFragmentUpdate", "adding to back stack");
                    trans.addToBackStack(null);
                trans.commit();

                detailFragment.setDisplayString(current_details);
            }
        }
    }
}
