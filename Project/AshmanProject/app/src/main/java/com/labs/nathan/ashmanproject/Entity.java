package com.labs.nathan.ashmanproject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;

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
        paint = new Paint();
        paint.setColor(color);
    }

    public Entity(int direction, int color, PlayingField parent) {
        this.direction = direction;
        step = 0;
        this.color = color;
        paint = new Paint();
        paint.setColor(color);

        Random rand = new Random();
        this.x = rand.nextInt(14);
        this.y = rand.nextInt(14);

        while(parent.getMap(this.x, this.y) == 0) {
            this.x = rand.nextInt(14);
            this.y = rand.nextInt(14);
        }
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

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public float getCanvasX() {
        return this.x_canvas;
    }

    public float getCanvasY() {
        return this.y_canvas;
    }

    public Point getPos() {
        return new Point(this.x, this.y);
    }

    public void setPos(int x, int y) {
        step = 0;
        this.x = x;
        this.y = y;
    }

    public int getStep() {
        return step;
    }

    public void update(PlayingField parent) {
        step = 0; // default setting
        // step++; uncomment later
    }

    /* drawing methods */

    public void onDraw(Canvas canvas) {
        if(step == 0) {
            x_canvas = x;
            y_canvas = y;
            canvas.drawCircle(x_canvas + .5f, y_canvas + .5f, .4f, paint);
        }
    }
}
