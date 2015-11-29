package com.labs.nathan.ashmanproject;

import android.graphics.Point;
import android.view.View;

/**
 * Created by Nathan on 11/28/2015.
 */
public class Ghost extends Entity {
    public Ghost(int color, PlayingField parent) {
        /* TODO: Make this choose random starting location */
        super(0, color, parent);
    }

    public void update(PlayingField parent) {
        Point nextPoint = new Point(getPos());

        if(getStep() == 0) {
            switch (getDirection()) {
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
        } else {
            /* TODO: handle animation */
        }

        if(nextPoint.x >= parent.sizes.x || nextPoint.x < 0 ||
                nextPoint.y >= parent.sizes.y || nextPoint.y < 0 ||
                parent.getMap(nextPoint.x, nextPoint.y) == 0) {

        } else {
            setPos(nextPoint.x, nextPoint.y);
        }
        super.update(parent);
    }
}
