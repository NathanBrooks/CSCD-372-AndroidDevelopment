package com.labs.nathan.ntbrookslab8;

import com.dropbox.chooser.android.DbxChooser;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Activity1 extends AppCompatActivity implements ExpandableListView.OnChildClickListener,
                                                            View.OnClickListener {

    ArrayList<String> Make;
    ArrayList<ArrayList<String>> AllModels;
    MyListAdapter myAdapter;

    static final String APP_KEY = "esh1djgrmeefo9e";
    static final int DBX_CHOOSER_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_list_view);

        findViewById(R.id.dbox_button).setOnClickListener(this); // register the listener

        Make = new ArrayList<>();
        AllModels = new ArrayList<>();
        if(savedInstanceState == null) {
            if (!parseFile("MakeModelList.txt")) {
                Toast.makeText(getApplicationContext(), "List Failed to Parse", Toast.LENGTH_LONG).show();
            } else {
                myAdapter = new MyListAdapter(this, Make, AllModels);
                ExpandableListView myListView = (ExpandableListView) findViewById(R.id.expandableListView);
                myListView.setOnChildClickListener(this);
                myListView.setAdapter(myAdapter);
            }
        } else {
            Make = (ArrayList<String>)savedInstanceState.getSerializable("Make");
            AllModels = (ArrayList<ArrayList<String>>)savedInstanceState.getSerializable("AllModels");
            myAdapter = new MyListAdapter(this, Make, AllModels);
            ExpandableListView myListView = (ExpandableListView) findViewById(R.id.expandableListView);
            myListView.setOnChildClickListener(this);
            myListView.setAdapter(myAdapter);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("Make", Make);
        savedInstanceState.putSerializable("AllModels", AllModels);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_expandable_list_view, menu);
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
            Toast.makeText(getApplicationContext(), "Android Lab 2, Nathan T. Brooks", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        DbxChooser mChooser = new DbxChooser(APP_KEY);
        mChooser.forResultType(DbxChooser.ResultType.FILE_CONTENT).launch(Activity1.this, DBX_CHOOSER_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == DBX_CHOOSER_REQUEST) {
            if(resultCode == RESULT_OK) {
                DbxChooser.Result myResult = new DbxChooser.Result(data);
                ContentResolver contentResolver = getContentResolver();
                try {
                    /* clear out data */
                    Make.clear();
                    AllModels.clear();
                    /* parse input stream from dropbox file */
                    Log.d("ActivityResult", "about to get iStream");
                    Log.d("ActivityResult", "myResult.getLink() = " + myResult.getLink());
                    InputStream iStream = contentResolver.openInputStream(myResult.getLink());
                    Log.d("ActivityResult", "about to parse iStream");
                    parseInputStream(iStream);
                    Log.d("onActivityResult", "Parsed data");
                    myAdapter = new MyListAdapter(this, Make, AllModels);

                    /* reset the view */
                    ExpandableListView myListView = (ExpandableListView) findViewById(R.id.expandableListView);
                    myListView.setAdapter(myAdapter);
                } catch (java.io.FileNotFoundException e) {
                    Toast.makeText(this, "There was an error opening the file!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Toast.makeText(getApplicationContext(), AllModels.get(groupPosition).get(childPosition), Toast.LENGTH_SHORT).show();
        return true;
    }

    private void parseInputStream(InputStream inputStream) {
        Scanner fileScanner = new Scanner(inputStream);
        while(fileScanner.hasNextLine()) {
            String input = fileScanner.nextLine();
            String[] splitInput = input.split(",");
            Make.add(splitInput[0]);
            ArrayList<String> modelList = new ArrayList<>();
            for(int idx = 1; idx < splitInput.length; idx++) {
                modelList.add(splitInput[idx]);
            }
            AllModels.add(modelList);
        }
    }

    private boolean parseFile(String filename) {
        AssetManager AssetMan = getResources().getAssets();
        try{
            Scanner fileScanner = new Scanner(AssetMan.open(filename));
            while(fileScanner.hasNextLine()) {
                String input = fileScanner.nextLine();
                String[] splitInput = input.split(",");
                Make.add(splitInput[0]);
                ArrayList<String> modelList = new ArrayList<>();
                for(int idx = 1; idx < splitInput.length; idx++) {
                    modelList.add(splitInput[idx]);
                }
                AllModels.add(modelList);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
