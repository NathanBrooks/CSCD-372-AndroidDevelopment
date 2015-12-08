package com.labs.nathan.ashmanproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
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
    private final int cCake = Color.rgb(102,255,102);
    private final int cWall = Color.rgb(0,0,0);

    /* entities */
    private AshMan player;
    private Ghost [] ghosts;

    /* logic handlers */
    private final int timerHitRate = 25;
    private Handler clockHandler;
    private Runnable clockTimer;

    /* game handlers */
    private boolean isPaused;
    private int gameLevel;
    private int cakesLeft;
    private boolean gameOver;
    private boolean gameKilled;


    /* callbacks */
    OnGameUpdate gameCallback;



    private final int[] initGameField = {2,2,2,0,0,0,0,2,0,0,0,0,0,0,
                                         2,0,2,0,2,2,2,2,0,2,2,2,2,2,
                                         2,0,2,0,2,0,0,2,2,2,2,0,2,2,
                                         2,0,2,0,2,2,0,0,0,0,2,0,2,2,
                                         2,0,2,2,2,0,0,2,2,2,2,0,2,2,
                                         2,0,0,0,2,2,0,2,2,2,2,0,0,2,
                                         2,2,2,0,2,2,2,2,0,2,2,2,2,2,
                                         0,0,2,0,2,2,2,0,0,0,2,0,0,2,
                                         2,2,2,0,2,1,2,2,0,2,2,0,2,2,
                                         2,0,2,0,2,0,0,2,0,2,0,2,2,2,
                                         2,0,2,0,2,0,0,2,2,2,0,2,0,0,
                                         2,0,2,2,2,2,0,2,0,0,0,2,0,2,
                                         2,0,2,2,2,2,0,2,2,2,2,2,0,2,
                                         2,0,0,2,2,2,2,2,0,0,2,2,2,2};

    private int[] gameField;

    public Point sizes = new Point(14,14);

    /* constructors */

    public PlayingField(Context context) {
        super(context);
        init();
        clockHandler.postDelayed(clockTimer, timerHitRate);
    }

    public PlayingField(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        clockHandler.postDelayed(clockTimer, timerHitRate);
    }

    public PlayingField(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        clockHandler.postDelayed(clockTimer, timerHitRate);
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null); // turn off hardware acceleration
        paint.setStyle(Paint.Style.FILL);

        gameField = initGameField.clone();

        player = new AshMan();

        ghosts = new Ghost[5];
        for(int i=0; i<ghosts.length; i++) {
            ghosts[i] = new Ghost(Color.rgb(255,0,0), this);
        }

        isPaused = false;
        gameLevel = 1;
        cakesLeft = 0;
        gameOver = false;
        gameKilled = false;

        for(int i=0; i<field_y; i++){
            for(int j=0; j<field_y; j++) {
                if(getMap(i,j) == 2) cakesLeft++;
            }
        }

        clockHandler = new Handler();

        clockTimer = new Runnable() {
            @Override
            public void run() {
                update();
                if(!gameKilled)
                    clockHandler.postDelayed(this, timerHitRate);
            }

        };

    }

    /* maths */

    public int getMap(int x, int y) {
        return gameField[x + (y * field_x)];
    }

    private void setMap(int x, int y, int value) {
        gameField[x + (y * field_x)] = value;
    }


    /* game interaction commands */

    public void setJoystickDirection(int direction) {
        player.setDirection(direction);
    }

    public void pauseGame() {
        clockHandler.removeCallbacksAndMessages(null);
        if(!gameKilled)
            isPaused = true;
        invalidate();
    }

    public void resumeGame() {
        if(!gameOver) {
            clockHandler.postDelayed(clockTimer, timerHitRate);
            isPaused = false;
            invalidate();
        }
    }

    public void killScreen() {
        clockHandler.postDelayed(clockTimer, timerHitRate);
        gameKilled = true;
        invalidate();
    }

    public void resetGame() {
        pauseGame(); // kill current timer
        init();
        pauseGame(); // need to call again to reforce all pause vars
    }

    public void startNextLevel() {
        pauseGame(); // kill current timer
        init();
        pauseGame(); // need to call again to reforce pause vars

        gameLevel = 2; // update the game level

        for(Ghost ghost : ghosts) {
            ghost.setCurrentLevel(gameLevel); // change the ghost speed
        }

        if(this.gameCallback !=  null)
            this.gameCallback.onGameUpdate(gameLevel, cakesLeft, true, false, false); // alert the parent
            this.gameCallback.onGameSoundRequest(false, true, false, false);
    }

    public void gameWin() {
        gameOver = true;
        clockHandler.removeCallbacksAndMessages(null);
        if(this.gameCallback != null)
            this.gameCallback.onGameUpdate(gameLevel, cakesLeft, false, true, false); // alert the parent
            this.gameCallback.onGameSoundRequest(false, false, false, true);
    }

    public void cheat() {
        pauseGame();
        if(gameLevel == 1)
            startNextLevel();
        else
            gameWin();
    }

    public boolean isGamePaused() {
        return new Boolean(isPaused);
    }

    /* instance commands */

    public void setOnGameUpdate(OnGameUpdate callback) {
        gameCallback = callback;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        pauseGame();

        Bundle saveBundle = new Bundle();
        /* local saved stuff */
        saveBundle.putIntArray("map_state", gameField);

        saveBundle.putBoolean("gameOver", gameOver);
        saveBundle.putInt("level", gameLevel);
        saveBundle.putInt("cakes", cakesLeft);

        saveBundle.putBundle("player", player.getInstanceBundle());
        for(int i=0; i<ghosts.length; i++) {
            saveBundle.putBundle("ghost" + i, ghosts[i].getInstanceBundle());
        }
        saveBundle.putParcelable("instance_state", super.onSaveInstanceState());
        return saveBundle;
    }

    protected void onRestoreInstanceState(Parcelable incomingState) {
        if(incomingState != null) {
            Bundle savedState = (Bundle)incomingState;
            gameField = savedState.getIntArray("map_state");
            pauseGame();

            gameOver = savedState.getBoolean("gameOver");
            gameLevel = savedState.getInt("level");
            cakesLeft = savedState.getInt("cakes");

            player.setInstanceBundle(savedState.getBundle("player"));
            for(int i=0; i<ghosts.length; i++){
                ghosts[i].setInstanceBundle(savedState.getBundle("ghost"+i));
                ghosts[i].setCurrentLevel(gameLevel);
            }
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

        if(newHeight > allocatedHeight) {
            setMeasuredDimension(newWidth, allocatedHeight);
        } else {
            setMeasuredDimension(allocatedWidth, newHeight);
        }
    }

    private void checkPlayerInteractions() {
        if(player.playerIsDead()) {
            if(!gameOver) {
                for (Ghost ghost : ghosts) {
                    ghost.stop();
                }

                if(this.gameCallback != null)
                    this.gameCallback.onGameSoundRequest(false, false, true, false);

            }

            if(player.DeathAnimationSteps() == 0) {
                if(this.gameCallback != null && !gameKilled) {
                    this.gameCallback.onGameUpdate(gameLevel, cakesLeft, false, false, true);
                }
                killScreen();
            }
            gameOver = true; // done last because we handled the rest of ending the game
        } else {
            Point player_pos = player.getPos();

            for(Ghost ghost : ghosts) {
                if(player_pos.equals(ghost.getPos()))
                    player.killPlayer();
            }

            setMap(player_pos.x, player_pos.y, 1);
        }

    }

    private void update() {
        player.update(this);
        for(int i=0; i<ghosts.length; i++) {
            ghosts[i].update(this);
        }

        checkPlayerInteractions();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /* TODO: draw playing field state base on array */

        canvas.scale(change_width / width, change_height/height);
        canvas.drawColor(cBackground); // color the background

        int tmpCakesLeft = 0;

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
                        tmpCakesLeft++;
                        break;
                    default: // empty
                        break;
                }
            }
        }

        if(tmpCakesLeft != cakesLeft && this.gameCallback != null && !player.playerIsDead()) {
            this.gameCallback.onGameSoundRequest(true, false, false, false);
        }

        cakesLeft = tmpCakesLeft;

        if(cakesLeft != 0) { // this is here because I don't want to deal with killing the thread within itself #lazy
            if (this.gameCallback != null) {
                this.gameCallback.onGameUpdate(gameLevel, cakesLeft, false, false, false);
            }
        } else {
            if (gameLevel == 1)
                startNextLevel();
            else
                gameWin();
        }

        /* draw player */
        player.onDraw(canvas);

        /* draw ghosts */
        for(int i=0; i<ghosts.length; i++) {
            ghosts[i].onDraw(canvas);
        }

        if(isPaused) { // display the paused screen
            paint.setColor(Color.rgb(0,0, 0));
            paint.setAlpha(127);
            canvas.drawPaint(paint);
            paint.setColor(Color.rgb(255,255,255));
            paint.setTextSize(3);
            paint.setAlpha(255);
            canvas.drawText("Paused", 0, field_y/2, paint);
        } else if (gameKilled) {
            paint.setColor(Color.rgb(0,0, 0));
            paint.setAlpha(127);
            canvas.drawPaint(paint);
            paint.setColor(Color.rgb(255,255,255));
            paint.setTextSize(2);
            paint.setAlpha(255);
            canvas.drawText("Game Over", 0, field_y/2, paint);

        }


    }
}
