package com.labs.nathan.ntbrookslab4;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button incButton;
    ArrayList<SevenSegment> segment_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        segment_list = new ArrayList<>();
        segment_list.add((SevenSegment) findViewById(R.id.top_cview));
        segment_list.add((SevenSegment) findViewById(R.id.bleft_cview));
        segment_list.add((SevenSegment) findViewById(R.id.bcenter_cview));
        segment_list.add((SevenSegment) findViewById(R.id.bright_cview));

        if(savedInstanceState == null) {
            int initInc = 0;
            for(SevenSegment I_cView : segment_list) I_cView.set(initInc++);
        }

        incButton = (Button)findViewById(R.id.main_button);
        incButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        for(SevenSegment I_cView : segment_list ) {
            int current = I_cView.get();
            current = (current + 1) % 11;
            I_cView.set(current);
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
        if (id == R.id.action_about) {
            Toast.makeText(this, "Android Lab 4, Nathan T. Brooks", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
