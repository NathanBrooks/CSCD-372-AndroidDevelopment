package com.labs.nathan.ashmanproject;

/**
 * Created by Nathan on 12/5/2015.
 */
public interface OnGameUpdate {
    // my god these are bad, I should have used magic numbers haha
    // really these should be handled in one with something like a game update object, but oh well
    void onGameUpdate(int level, int count, boolean levelSwitch, boolean gameWin, boolean gameOver);
    void onGameSoundRequest(boolean cake, boolean level, boolean death, boolean win);
}
