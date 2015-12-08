package com.labs.nathan.ashmanproject;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by Nathan on 11/28/2015.
 */
public class Ghost extends Entity {
    int direction = 0;
    int activeDirection = 0;
    int current_movement_animation = 0;
    int current_level = 0;

    boolean stop = false;

    public Ghost(int color, PlayingField parent) {
        /* TODO: Make this choose random starting location */
        super(0, color, parent);
    }

    public void setCurrentLevel(int level) {
        current_level = level;
    }

    private void chooseGhostDir(Point wallPoint, PlayingField parent) {
        Random rand = new Random();
        Point nextPoint = new Point(wallPoint);

        int dir  = getDirection();


        // no need for chance north, it handles itself in the math
        double chanceE = .25;
        double chanceW = .25;
        double chanceS = .25;

        while(nextPoint.x >= parent.sizes.x || nextPoint.x < 0 ||
                nextPoint.y >= parent.sizes.y || nextPoint.y < 0 ||
                parent.getMap(nextPoint.x, nextPoint.y) == 0) {

            switch(dir) { // ADJUST ODDS
                case 0:
                    chanceE += .02;
                    chanceW += .02;
                    chanceS -= .05;
                    break;
                case 1:
                    chanceE -= .05;
                    chanceW += .01;
                    chanceS += .02;
                    break;
                case 2:
                    chanceE += .01;
                    chanceW -= .05;
                    chanceS += .02;
                    break;
                case 3:
                    chanceE += .02;
                    chanceW += .02;
                    chanceS += .01;
                    break;
                default:
                    break;
            }

            double r_val = rand.nextDouble();

            if (r_val >= 0 && r_val <= chanceS )
                dir = 0;
            else if (r_val > chanceS && r_val <= (chanceS + chanceE))
                dir = 1;
            else if (r_val > (chanceS + chanceE) && r_val <= (chanceS + chanceE + chanceW))
                dir = 2;
            else
                dir = 3;

            nextPoint.x = getPos().x;
            nextPoint.y = getPos().y;

            switch(dir) {
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

        setDirection(dir);
    }

    public void stop() { stop = true; }

    public void update(PlayingField parent) {
        if(!stop) {
            if (current_movement_animation == 0) {
                direction = activeDirection;
            }
            if (getStep() % 2 == 0 || current_level == 2) {
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

                if (nextPoint.x >= parent.sizes.x || nextPoint.x < 0 ||
                        nextPoint.y >= parent.sizes.y || nextPoint.y < 0 ||
                        parent.getMap(nextPoint.x, nextPoint.y) == 0) {
                    current_movement_animation = 0;

                    chooseGhostDir(nextPoint, parent);


                    return;
                }


                if (current_movement_animation == 3) {
                    setPos(nextPoint.x, nextPoint.y);
                    setCanvasX(nextPoint.x);
                    setCanvasY(nextPoint.y);
                    Random rand = new Random();
                    if(rand.nextDouble() > .95) {
                        chooseGhostDir(nextPoint, parent);
                    }
                    current_movement_animation = 0;
                } else {
                    setCanvasX(nextPoints[0]);
                    setCanvasY(nextPoints[1]);
                    current_movement_animation++;
                }
                parent.invalidate(invalidRegion);
            }
            super.update(parent);
        }
    }

    @Override
    public void setDirection(int direction) {
        if(direction != -1)
            activeDirection = direction;
    }
}
