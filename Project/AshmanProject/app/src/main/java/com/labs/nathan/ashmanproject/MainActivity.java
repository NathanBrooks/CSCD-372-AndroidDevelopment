package com.labs.nathan.ashmanproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, OnGameUpdate,
                                                                MediaPlayer.OnCompletionListener, DialogInterface.OnClickListener {

    private PlayingField game;
    private Joystick joystick;
    private TextView score;
    private TextView level;

    private AudioManager am;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        am = (AudioManager)getSystemService(AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)/2, 0);

        game = (PlayingField)findViewById(R.id.field);
        joystick = (Joystick)findViewById(R.id.joystick);
        score = (TextView)findViewById(R.id.cakes_left);
        level = (TextView)findViewById(R.id.level);

        game.setOnClickListener(this);
        game.setOnGameUpdate(this);
        joystick.setOnTouchListener(this);

    }

    @Override
    public void onGameUpdate(int level, int count,  boolean levelSwitch, boolean gameWin, boolean gameOver) {
        score.setText("Remaining: \n" + count);
        this.level.setText("Level: \n" + level);

        if(gameWin) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("WINNER!")
                    .setMessage("YOU WIN!!\n\n REPLAY?")
                    .setNeutralButton("YES", this)
                    .setNegativeButton("NO", null)
                    .setCancelable(false)
                    .create()
                    .show();
            /* game win dialog */
        }

        if(levelSwitch) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("NEXT LEVEL")
                    .setMessage("Now entering the next level!")
                    .setNeutralButton("OK", null)
                    .setCancelable(false)
                    .create()
                    .show();
            /* level switch dialog */
        }

        if(gameOver) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Game Over")
                    .setMessage("You Lose!\n\n REPLAY?")
                    .setNeutralButton("YES", this)
                    .setNegativeButton("NO", null)
                    .setCancelable(false)
                    .create()
                    .show();
            /* Game over dialog */
        }
    }

    @Override
    public void onGameSoundRequest(boolean cake, boolean level, boolean death, boolean win) {
        if(mp !=  null) {
            mp.stop();
            mp.release();
        }

        if(cake) {
            mp = MediaPlayer.create(getApplicationContext(), R.raw.eatcake);
            mp.setOnCompletionListener(this);
            mp.start();
        } else if (level) {
            mp = MediaPlayer.create(getApplicationContext(), R.raw.intermission);
            mp.setOnCompletionListener(this);
            mp.start();
        } else if (death) {
            mp = MediaPlayer.create(getApplicationContext(), R.raw.death);
            mp.setOnCompletionListener(this);
            mp.start();
        } else if (win) {
            mp = MediaPlayer.create(getApplicationContext(), R.raw.won);
            mp.setOnCompletionListener(this);
            mp.start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.stop();
        mp.release();
        this.mp = null;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()) {
            case R.id.joystick:
                game.setJoystickDirection(joystick.update(event));
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        game.resetGame();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.field:
                if(!game.isGamePaused())
                    game.pauseGame();
                else
                    game.resumeGame();
                break;
            default:
                break;
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
        game.pauseGame();
        switch(item.getItemId()) {
            case R.id.action_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("About")
                        .setMessage("Nathan T. Brooks, CSCD 372, Fall 2015, AshMan Project")
                        .setNeutralButton("OK", null)
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            case R.id.action_reset:
                game.resetGame();
                return true;
            case R.id.action_cheat:
                game.cheat();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
