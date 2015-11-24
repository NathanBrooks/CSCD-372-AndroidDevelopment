package com.labs.nathan.ntbrookslab4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Nathan on 10/25/2015.
 */
public class SevenSegment extends View {
    int current_value;
    boolean[] segments_current;
    boolean[][] segments_holder = {{true, true, false, true, true, true, true},
                                    {false, false, false, true, false, false, true},
                                    {true, false, true, true, true, true, false},
                                    {true, false, true, true, false, true, true},
                                    {false, true, true, true, false, false, true},
                                    {true, true, true, false, false, true, true},
                                    {true, true, true, false, true, true, true},
                                    {true, false, false, true, false, false, true},
                                    {true, true, true, true, true, true, true},
                                    {true, true, true, true, false, true, true},
                                    {false, false, false, false, false, false, false}};

    private final int height = 38;
    private final int width = 22;
    private int changeHeight = 38;
    private int changeWidth = 22;
    private final float aspect_ratio = ((float)width)/((float)height);
    private final float[] vertices = {3,3,4,2,18,2,19,3,18,4,4,4};

    public void set(int i) {
        if(i >= 0 && i <= 10) {
            current_value = i;
            segments_current = segments_holder[current_value];
            invalidate();
        }
    }

    public int get() {
        return current_value;
    }

    private void initSegments() {
        current_value = 0;
        set(current_value);
    }

    public SevenSegment(Context context) {
        super(context);
        initSegments();
    }

    public SevenSegment(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSegments();
    }

    public SevenSegment(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSegments();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle saveBundle = new Bundle();
        saveBundle.putParcelable("instanceState", super.onSaveInstanceState());
        saveBundle.putInt("currentValue", current_value);
        return saveBundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable incomingState) {
        if(incomingState != null) {
            Bundle savedState = (Bundle)incomingState;
            set(savedState.getInt("currentValue"));
            super.onRestoreInstanceState(savedState.getParcelable("instanceState"));
        } else {
            super.onRestoreInstanceState(incomingState);
        }
    }

    private Path genPath(final float[] pArray) {
        Path newPath = new Path();

        newPath.moveTo(pArray[0], pArray[1]);
        for(int i=2; i<pArray.length; i = i+2) newPath.lineTo(pArray[i], pArray[i+1]);

        newPath.close();

        return newPath;
    }

    private void drawSegment(int index, Canvas canvas) {
        canvas.clipPath(genPath(vertices));
        if(segments_current[index]) {
            canvas.drawColor(Color.rgb(255,0,0));
        } else {
            canvas.drawColor(Color.rgb(51,0,0));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        changeWidth = w;
        changeHeight = h;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        setLayerType(this.LAYER_TYPE_SOFTWARE, null);
        canvas.scale(changeWidth / width, changeHeight / height);
        canvas.drawColor(Color.rgb(0, 0, 0));

        canvas.save();
        drawSegment(0, canvas);
        canvas.restore();

        canvas.save();
        canvas.rotate(90.f, vertices[0], vertices[1]);
        drawSegment(1, canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(0, 16);
        drawSegment(2, canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(16, 0);
        canvas.rotate(90.f, vertices[0], vertices[1]);
        drawSegment(3, canvas);
        canvas.restore();


        canvas.save();
        canvas.translate(0, 16);
        canvas.rotate(90.f, vertices[0], vertices[1]);
        drawSegment(4, canvas);
        canvas.restore();
        canvas.save();

        canvas.translate(0, 32);
        drawSegment(5, canvas);
        canvas.restore();
        canvas.save();

        canvas.translate(16, 16);
        canvas.rotate(90.f, vertices[0], vertices[1]);
        drawSegment(6, canvas);
        canvas.restore();

    }
}
