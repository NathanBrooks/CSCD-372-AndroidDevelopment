package com.labs.nathan.ashmanproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Nathan on 12/5/2015.
 */
public class Joystick extends View {
    /* drawing dimensions */
    private final float width = 3f;
    private final float height = 3f;
    private final float aspect_ratio = width/height;
    private int change_width = 3;
    private int change_height = 3;

    /* paint */
    private Paint paint;
    private final int cButton = Color.rgb(36,179,227);

    /* button locations */
    float [][] buttons = new float[][]{
            {1.5f, 2.3f},
            {2.3f, 1.5f},
            {.7f, 1.5f},
            {1.5f, .7f}
    };


    /* constructors */

    public Joystick(Context context) {
        super(context);
        init();
    }

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Joystick(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null); // turn off hardware acceleration
        paint = new Paint();
    }

    public int update(MotionEvent event) {
        float xTouch = ((event.getX()/change_width)) * 3;
        float yTouch = ((event.getY()/change_height)) * 3;

        for(int i=0; i<4; i++) {
            if(Math.pow((xTouch - buttons[i][0]),2) + Math.pow((yTouch - buttons[i][1]),2) < .25)
                return i;
        }
        return -1;

    }

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

        if(newHeight > allocatedHeight) {
            setMeasuredDimension(newWidth, allocatedHeight);
        } else {
            setMeasuredDimension(allocatedWidth, newHeight);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.scale(change_width / width, change_height / height);

        paint.setAlpha(0);
        canvas.drawPaint(paint);
        paint.setColor(cButton);
        paint.setAlpha(255);
        canvas.drawCircle(buttons[0][0], buttons[0][1], .5f, paint);
        canvas.drawCircle(buttons[1][0], buttons[1][1], .5f, paint);
        canvas.drawCircle(buttons[2][0], buttons[2][1], .5f, paint);
        canvas.drawCircle(buttons[3][0], buttons[3][1], .5f, paint);
    }

}
