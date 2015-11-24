package com.labs.nathan.ashmanproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

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
    private final int cGhost = Color.rgb(168,91,24);
    private final int cCake = Color.rgb(163,24,168);
    private final int cWall = Color.rgb(0,0,0);



    /* TODO: new map, this is the one in the example */
    private int[] gameField = {2,2,2,0,0,0,0,2,0,0,0,0,0,0,
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

    /* constructors */

    public PlayingField(Context context) {
        super(context);
        Log.d("PlayingField", "base field constructor called");
        setLayerType(View.LAYER_TYPE_SOFTWARE, null); // turn off hardware acceleration
        paint.setStyle(Paint.Style.FILL);
    }

    public PlayingField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayingField(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /* maths */

    private int getMap(int x, int y) {
        return gameField[x + (y * field_x)];
    }

    private void setMap(int x, int y, int value) {
        gameField[x + (y * field_x)] = value;
    }


    /* game interaction commands */

    public void setJoystickDirection(int direction) {
        /* TODO: set AshMan's direction to passed in direction */
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


    }
}
