package com.labs.nathan.ntbrookslab2;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Activity1 extends AppCompatActivity implements ExpandableListView.OnChildClickListener {

    ArrayList<String> Make;
    ArrayList<ArrayList<String>> AllModels;
    MyListAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_list_view);
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
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Toast.makeText(getApplicationContext(), AllModels.get(groupPosition).get(childPosition), Toast.LENGTH_SHORT).show();
        return true;
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
