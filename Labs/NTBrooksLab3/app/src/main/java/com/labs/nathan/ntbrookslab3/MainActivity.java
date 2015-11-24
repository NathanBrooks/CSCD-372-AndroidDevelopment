package com.labs.nathan.ntbrookslab3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    DrawerLayout mainDrawerLayout;
    ListView mainListView;
    int[] mainImageSrcIds;
    private CharSequence titleClosed;
    private CharSequence titleOpened;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mainListView = (ListView)findViewById(R.id.left_drawer);

        titleClosed = getTitle();
        titleOpened = "Select a Page";
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mainDrawerLayout,
                toolbar, R.string.drawer_open_desc, R.string.drawer_closed_desc) {

            public void onDrawerClosed(View view) {
                super.onDrawerOpened(view);
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle(getTitle());
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                assert getSupportActionBar() != null;
                getSupportActionBar().setTitle("Select a Page");
                invalidateOptionsMenu();
            }

        };

        mainDrawerLayout.setDrawerListener(drawerToggle);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle.syncState();

        // these really should only be initialized once, place in bundle to avoid recreation on rotation
        mainImageSrcIds = new int[6];
        DrawerItem[] drawerItems = new DrawerItem[6];
        drawerItems[0] = new DrawerItem("Cindy Wilson");
        mainImageSrcIds[0] = R.drawable.cindy;
        drawerItems[1] = new DrawerItem("Fred Schneider");
        mainImageSrcIds[1] = R.drawable.fred;
        drawerItems[2] = new DrawerItem("Kate Pierson");
        mainImageSrcIds[2] = R.drawable.kate;
        drawerItems[3] = new DrawerItem("Keith Strickland");
        mainImageSrcIds[3] = R.drawable.keith;
        drawerItems[4] = new DrawerItem("Matt Flynn");
        mainImageSrcIds[4] = R.drawable.matt;
        drawerItems[5] = new DrawerItem("Rickey Wilson");
        mainImageSrcIds[5] = R.drawable.rickey;

        DrawerItemAdapter myDrawerAdapter = new DrawerItemAdapter(this, R.id.left_drawer, drawerItems);
        mainListView.setAdapter(myDrawerAdapter);
        mainListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ((ImageView)findViewById(R.id.image_view)).setImageResource(mainImageSrcIds[position]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(((DrawerLayout)findViewById(R.id.drawer_layout)).isDrawerOpen(mainListView)) {
            menu.findItem(R.id.action_settings).setVisible(false);
        } else {
            menu.findItem(R.id.action_settings).setVisible(true);
        }
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
            Toast.makeText(getApplicationContext(), "Android Lab 3, Nathan T. Brooks", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
