package com.labs.nathan.ashmanproject;

import android.graphics.Color;
import android.graphics.Point;

/**
 * Created by Nathan on 11/26/2015.
 */
public class AshMan extends Entity {
    private boolean isDead;
    private int death_animation_step = 0;

    public AshMan() {
        /* TODO: Make this choose actual starting location */
        super(5,8,0,Color.rgb(191,172,0));
        isDead = false;
    }

    public void KillPlayer() {
        isDead = true;
        death_animation_step = 5;
    }

    public void update(PlayingField parent) {
        Point nextPoint = new Point(getPos());

        if(!isDead) {
            if (getStep() == 0) {
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

            if (nextPoint.x >= parent.sizes.x || nextPoint.x < 0 ||
                    nextPoint.y >= parent.sizes.y || nextPoint.y < 0 ||
                    parent.getMap(nextPoint.x, nextPoint.y) == 0) {

            } else {
                setPos(nextPoint.x, nextPoint.y);
            }
            super.update(parent);
        } else {
            /* TODO: death animmation */
        }

    }
}
