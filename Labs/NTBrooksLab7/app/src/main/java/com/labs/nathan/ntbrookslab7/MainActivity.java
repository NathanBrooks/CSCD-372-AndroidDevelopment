package com.labs.nathan.ntbrookslab7;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener {

    final int [] boardID = {
            R.id.row0col0, R.id.row0col2, R.id.row0col4,
            R.id.row2col0, R.id.row2col2, R.id.row2col4,
            R.id.row4col0, R.id.row4col2, R.id.row4col4
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView xImageView = (ImageView)findViewById(R.id.xImageView);
        ImageView oImageView = (ImageView)findViewById(R.id.oImageView);

        xImageView.setOnTouchListener(this);
        oImageView.setOnTouchListener(this);

        for(int id:boardID) {
            View tmp = findViewById(id);
            tmp.setTag(R.drawable.blank);
            tmp.setOnDragListener(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle instanceState){
        int [] board_state = new int[9];
        for(int i=0; i<9; i++) {
            board_state[i] = (int)findViewById(boardID[i]).getTag();
        }
        instanceState.putIntArray("board_state", board_state);
    }

    @Override
    public void onRestoreInstanceState(Bundle instanceState) {
        int [] board_state = instanceState.getIntArray("board_state");
        for(int i=0; i<board_state.length; i++) {
            ImageView board_square = (ImageView)findViewById(boardID[i]);
            board_square.setImageResource(board_state[i]);
            board_square.setTag(board_state[i]);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder dragBuilder = new View.DragShadowBuilder(v);
            if(v.getId() == R.id.xImageView)
                v.setTag(R.drawable.x);
            else
                v.setTag(R.drawable.o);
            v.startDrag(null, dragBuilder, v, 0);
        }
        if(event.getActionMasked() == MotionEvent.ACTION_UP) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        if(event.getAction() == DragEvent.ACTION_DROP) {
            View passedView = (View)event.getLocalState();
            if((int)v.getTag() == R.drawable.blank) {
                ((ImageView) v).setImageResource((int) passedView.getTag());
                v.setTag(passedView.getTag());
                return true;
            } else return false;
        }
        return true;
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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("About")
                    .setMessage("Nathan T. Brooks, CSCD 372, Fall 2015, Lab 7")
                    .setNeutralButton("OK", null)
                    .setCancelable(false)
                    .create()
                    .show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
