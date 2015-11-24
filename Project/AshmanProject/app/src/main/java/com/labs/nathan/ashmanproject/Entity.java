package com.labs.nathan.ashmanproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

/**
 * Created by Nathan on 11/9/2015.
 */
public class Entity {
    /* true position */
    private float x_canvas; // actual drawing x position
    private float y_canvas; // actual drawing y position


    /* board position */
    private int x;          // x board/grid position
    private int y;          // y board/grid position

    /* control settings */
    private int direction;  // what direction we are currently going

    /* drawing settings */
    private int step;       // what step in the animation
    private int color;      // what color to draw me
    private Paint paint;    // paint for drawing
    private final int anim_length = 0;

    /* constructors */

    public Entity(int color) {
        this.color = color;

        /* non passed in initializers */
        x = 0;
        y = 0;
        direction = 0;
        step = 0;
        paint = new Paint(this.color);
    }

    public Entity(int x, int y, int direction, int color) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.color = color;

        /* non passed in initializers */
        step = 0;
        paint = new Paint();
        paint.setColor(this.color);

    }

    /* instance handling */

    public Bundle getInstanceBundle() {
        Bundle newBundle = new Bundle();
            newBundle.putInt("pos_x", x);
            newBundle.putInt("pos_y", y);
            newBundle.putInt("direction", direction);
            newBundle.putInt("step", step);
        return newBundle;
    }

    public void setInstanceBundle(Bundle bundle) {
        x = bundle.getInt("pos_x");
        y = bundle.getInt("pos_y");
        direction = bundle.getInt("direction");
        step = bundle.getInt("step");
    }

    /* interaction methods */

    public void setDirection(int direction) {
        this.direction = direction;
    }


    public Point prepareUpdate() {

        Point nextPoint = new Point(x,y);

        if(step == 0) {
            switch (direction) {
                case 0:
                    nextPoint.y++;
                    break;
                case 1:
                    nextPoint.x++;
                    break;
                case 2:
                    nextPoint.x--;
                    break;
                case 3:
                    nextPoint.y--;
                    break;
                default:
                    break;
            }
        }

        return nextPoint;
    }

    public void update(Point updatePoint, View parent) {
        step = 0; // only do single steps for now
        if(step == 0) {
            int old_x = this.x;
            int old_y = this.y;

            this.x = updatePoint.x;
            this.y = updatePoint.y;

            parent.invalidate(new Rect(old_x, old_y, this.x+1, this.y+1));
        } else {
            /* TODO: handle animation */
        }
    }

    /* drawing methods */

    public void onDraw(Canvas canvas) {
        if(step == 0){
            x_canvas = x;
            y_canvas = y;
            canvas.drawCircle(x_canvas + .5f, y_canvas + .5f, .2f, paint);
        }
        /* todo Draw entitiy */
    }
}
