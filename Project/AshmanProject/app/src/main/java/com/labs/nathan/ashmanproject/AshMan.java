package com.labs.nathan.ashmanproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Nathan on 11/26/2015.
 */
public class AshMan extends Entity {
    private boolean isDead;
    private int death_animation_step = 0;
    private int current_movement_animation = 0;
    private int activeDirection = 0;
    private int direction = 0;
    private Paint paint = new Paint();

    public AshMan() {
        super(5,8,0,Color.rgb(191,172,0));
        isDead = false;
    }

    public void killPlayer() {
        isDead = true;
        death_animation_step = 6;
    }

    public int DeathAnimationSteps() { return death_animation_step; }

    public boolean playerIsDead() {
        return new Boolean(isDead);
    }


    public void update(PlayingField parent) {
        if(!isDead) {
            if(current_movement_animation == 0) {
                direction = activeDirection;
            }
            if(getStep() % 2 == 0) {
                float [] nextPoints = new float[] { 0f , 0f } ;

                Point nextPoint = new Point(getPos());
                Rect invalidRegion = new Rect(nextPoint.x, nextPoint.y, nextPoint.x + 1, nextPoint.y + 1);
                switch(direction) {
                    case 0:
                        nextPoints[0] = getCanvasX();
                        nextPoints[1] = getCanvasY() + .25f;
                        nextPoint.y++;
                        break;
                    case 1:
                        nextPoints[0] = getCanvasX() + .25f;
                        nextPoints[1] = getCanvasY();
                        nextPoint.x++;
                        invalidRegion.set(invalidRegion.left, invalidRegion.top, nextPoint.x+1, nextPoint.y+1);
                        break;
                    case 2:
                        nextPoints[0] = getCanvasX() - .25f;
                        nextPoints[1] = getCanvasY();
                        nextPoint.x--;
                        invalidRegion.set(nextPoint.x, nextPoint.y, invalidRegion.right, invalidRegion.bottom);
                        break;
                    case 3:
                        nextPoints[0] = getCanvasX();
                        nextPoints[1] = getCanvasY() - .25f;
                        nextPoint.y--;
                        invalidRegion.set(nextPoint.x, nextPoint.y, invalidRegion.right, invalidRegion.bottom);
                        break;
                    default:
                        break;
                }

                if(nextPoint.x >= parent.sizes.x || nextPoint.x < 0 ||
                    nextPoint.y >= parent.sizes.y || nextPoint.y < 0 ||
                    parent.getMap(nextPoint.x, nextPoint.y) == 0) {
                    current_movement_animation = 0;
                    return;
                }


                if(current_movement_animation == 3) {
                    setPos(nextPoint.x, nextPoint.y);
                    setCanvasX(nextPoint.x);
                    setCanvasY(nextPoint.y);
                    current_movement_animation = 0;
                } else {
                    setCanvasX(nextPoints[0]);
                    setCanvasY(nextPoints[1]);
                    current_movement_animation++;
                }
                parent.invalidate(invalidRegion);
            }
        } else {
            if(death_animation_step != 0){
                death_animation_step--;
                parent.invalidate();
            }
        }
        super.update(parent);
    }

    @Override
    public void setDirection(int direction) {
        if(direction != -1)
            activeDirection = direction;
    }

    public void drawTriangle(Canvas canvas) {
        paint.setColor(Color.rgb(24,101,168)); // this is where I break my nice class structure, this color is a background stored in PlayingField
        paint.setStyle(Paint.Style.FILL);
        if(current_movement_animation == 1 || current_movement_animation == 3) { // partial
            Path pTriangle = new Path();
            pTriangle.moveTo(.0f,.0f);
            pTriangle.lineTo(.5f, -.3f);
            pTriangle.lineTo(.5f, .2f);
            pTriangle.close();
            canvas.drawPath(pTriangle, paint);
        } else if (current_movement_animation == 2) { // full size
            Path pTriangle = new Path();
            pTriangle.moveTo(.0f,.0f);
            pTriangle.lineTo(.5f, -.5f);
            pTriangle.lineTo(.5f,.5f);
            pTriangle.close();
            canvas.drawPath(pTriangle, paint);
        }
    }

    public void drawDeathAnimationStep(Canvas canvas) {
        paint.setColor(Color.rgb(24,101,168)); // this is where I break my nice class structure, this color is a background stored in PlayingField
        paint.setStyle(Paint.Style.FILL);
        Path pTriangle = new Path();
        pTriangle.moveTo(.0f - (.4f - (death_animation_step * .1f)),.0f);
        pTriangle.lineTo(.5f, -.5f);
        pTriangle.lineTo(.5f,.5f);
        pTriangle.close();
        canvas.drawPath(pTriangle, paint);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(!isDead) {
            canvas.save();
            canvas.translate(getCanvasX()+.5f, getCanvasY()+.5f);
            switch(direction){
                case 0:
                    canvas.rotate(90f);
                    break;
                case 2:
                    canvas.rotate(180f);
                    break;
                case 3:
                    canvas.rotate(-90f);
                    break;
            }
            drawTriangle(canvas);
            canvas.restore();
        } else {
            canvas.save();
            canvas.translate(getCanvasX()+.5f, getCanvasY()+.5f);
            drawDeathAnimationStep(canvas);
            canvas.rotate(180f);
            drawDeathAnimationStep(canvas);
            canvas.restore();
        }
    }

}
