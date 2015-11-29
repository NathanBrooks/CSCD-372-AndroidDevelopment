package com.labs.nathan.ashmanproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Nathan on 11/9/2015.
 */
public class PlayingField extends View {
    /* drawing dimensions */
    private final int field_x = 14;
    private final int field_y = 14;
    private final float width = 14f;
    private final float height = 14f;
    private final float aspect_ratio = width/height;
    private int change_width = 14;
    private int change_height = 14;
    /* drawing colors */
    private final Paint paint = new Paint();
    private final int cBackground = Color.rgb(24,101,168);
    private final int cAshMan = Color.rgb(29,168,24);
    private final int cCake = Color.rgb(102,255,102);
    private final int cWall = Color.rgb(0,0,0);

    /* entities */
    private AshMan player;
    private Ghost [] ghosts;

    /* logic handlers */
    private Handler clockHandler;
    private Runnable clockTimer;


    private final int[] initGameField = {2,2,2,0,0,0,0,2,0,0,0,0,0,0,
                                         2,0,2,0,2,2,2,2,0,2,2,2,2,2,
                                         2,0,2,0,2,0,0,2,2,2,2,0,2,2,
                                         2,0,2,0,2,2,0,0,0,0,2,0,2,2,
                                         2,0,2,2,2,0,0,2,2,2,2,0,2,2,
                                         2,0,0,0,2,2,0,2,2,2,2,0,0,2,
                                         2,2,2,0,2,2,2,2,0,2,2,2,2,2,
                                         0,0,2,0,2,2,2,0,0,0,2,0,0,2,
                                         2,2,2,0,2,2,2,2,0,2,2,0,2,2,
                                         2,0,2,0,2,0,0,2,0,2,0,2,2,2,
                                         2,0,2,0,2,0,0,2,2,2,0,2,0,0,
                                         2,0,2,2,2,2,0,2,0,0,0,2,0,2,
                                         2,0,2,2,2,2,0,2,2,2,2,2,0,2,
                                         2,0,0,2,2,2,2,2,0,0,2,2,2,2};

    private int[] gameField;

    public Point sizes = new Point(14,14);

    /* constructors */

    public PlayingField(Context context) {
        super(context);
        init(context);
    }

    public PlayingField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayingField(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null); // turn off hardware acceleration
        paint.setStyle(Paint.Style.FILL);

        gameField = initGameField;

        player = new AshMan();

        Random rand = new Random(System.currentTimeMillis());
        ghosts = new Ghost[5];
        for(int i=0; i<ghosts.length; i++) {
            Log.d("PlayingField init", "new ghost");
            //ghosts[i] = new Ghost(Color.rgb(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)), this);
            ghosts[i] = new Ghost(Color.rgb(255,0,0), this);
        }


        clockHandler = new Handler();

        clockTimer = new Runnable() {
            @Override
            public void run() {
                update();
                clockHandler.postDelayed(this, 500);
            }
        };

        clockHandler.postDelayed(clockTimer, 500);

    }

    /* maths */

    public int getMap(int x, int y) {
        return gameField[x + (y * field_x)];
    }

    private void setMap(int x, int y, int value) {
        gameField[x + (y * field_x)] = value;
    }


    /* game interaction commands */

    public void setJoystickDirection(int direction) {
        player.setDirection(direction);
    }

    public void pauseGame() {
        /* TODO: stop game timers */
    }

    public void resumeGame() {
        /* TODO: resume game timers */
    }

    public boolean isGameOver() {
        /* TODO: detect when game has ended */
        return false;
    }

    /* instance commands */

    @Override
    protected Parcelable onSaveInstanceState() {
        /* TODO: save current state of the game */
        /* NOTE: Each entity should create its own bundle to be stored*/
        Bundle saveBundle = new Bundle();
        saveBundle.putIntArray("map_state", gameField);
        saveBundle.putParcelable("instance_state", super.onSaveInstanceState());
        return saveBundle;
    }

    protected void onRestoreInstanceState(Parcelable incomingState) {
        if(incomingState != null) {
            /* TODO: return state of the game */
            Bundle savedState = (Bundle)incomingState;
            gameField = savedState.getIntArray("map_state");
            super.onRestoreInstanceState(savedState.getParcelable("instance_state"));
        } else {
            super.onRestoreInstanceState(incomingState);
        }
    }

    /* drawing commands */

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        change_height = h;
        change_width = w;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int allocatedWidth = MeasureSpec.getSize(widthMeasureSpec);
        int allocatedHeight = MeasureSpec.getSize(heightMeasureSpec);

        int newWidth = (int)(allocatedHeight * aspect_ratio);
        int newHeight = (int)(allocatedWidth / aspect_ratio);

        Log.d("onMeasure allocated", "width: " + allocatedWidth + " height: " + allocatedHeight + " aspect_ratio: " + aspect_ratio);

        if(newHeight > allocatedHeight) {
            Log.d("onMeasure", "newWidth: " + newWidth + " allocatedHeight: " + allocatedHeight);
            setMeasuredDimension(newWidth, allocatedHeight);
        } else {
            Log.d("onMeasure", "allocatedWidth: " + allocatedWidth + " newHeight: " + newHeight);
            setMeasuredDimension(allocatedWidth, newHeight);
        }
    }

    private void checkPlayerInteractions() {
        Point player_pos = player.getPos();
        setMap(player_pos.x, player_pos.y, 1);
    }

    private boolean checkLevelComplete() {
        for(int i=0; i<sizes.y; i++) {
            for(int j=0; j<sizes.x; j++) {
                if(getMap(j,i) == 2) return false; // level is not over
            }
        }
        return true; // level is over
    }

    private void update() {
        player.update(this);
        for(int i=0; i<ghosts.length; i++) {
            ghosts[i].update(this);
        }
        checkPlayerInteractions();
        if(checkLevelComplete()) {
            Toast.makeText(getContext(), "LEVEL COMPLETE", Toast.LENGTH_LONG).show();
        }
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /* TODO: draw playing field state base on array */

        canvas.scale(change_width / width, change_height/height);
        canvas.drawColor(cBackground); // color the background

        /* draw map */
        for(int i=0; i<field_x; i++) {
            for(int j=0; j<field_y; j++) {
                switch(getMap(i,j)) {
                    case 0: // wall
                        paint.setColor(cWall);
                        canvas.drawRect(i,j,i+1,j+1, paint);
                        break;
                    case 2: // cake
                        paint.setColor(cCake);
                        canvas.drawCircle(i+.5f, j+.5f, .2f, paint);
                        break;
                    default: // empty
                        break;
                }
            }
        }

        /* draw player */
        player.onDraw(canvas);
        for(int i=0; i<ghosts.length; i++) {
            ghosts[i].onDraw(canvas);
        }


    }
}
