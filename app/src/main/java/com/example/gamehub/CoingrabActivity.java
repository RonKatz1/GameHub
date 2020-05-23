package com.example.gamehub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CoingrabActivity extends AppCompatActivity {


    private static final int MOVE_DELTA = 20;
    private static final int PLAYER_START_X = 250;
    private static final int PLAYER_START_Y = 100;


    // Duration of a game in ms
    private final int GAME_TIMER_START_VALUE = 10000;
    // UI update interval in ms
    private final int GAME_TIMER_TICK_VALUE = 100;
    // Initial delay when new game starts
    private final int NEW_GAME_DELAY = 300;

    //for hand movement
     private GestureDetectorCompat mDetector;

    private ImageView player;
    private ImageView coin;

    private int score = 0;

    private Timer timer;
    private int timeRemaining;

    //creating class object for sound options
    SoundManager soundManager = new SoundManager();


    private int moveX;
    private int moveY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coingrab);

        //"grabing" player and coin images
        player = findViewById(R.id.playerId);
        coin = findViewById(R.id.coinId);

        //using created  class MySimpleGestureListener
        mDetector = new GestureDetectorCompat(this, new MySimpleGestureListener(this));

        // Use AudioManager to get max volume
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        soundManager.initSoundPool(this, maxVolume);


        ConstraintLayout mainLayout = findViewById(R.id.coingrabLayout);
        mainLayout.post(new Runnable() {
            @Override
            public void run() {
                startNewGame();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_grabcoin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;

            case R.id.quit:
                Intent intent = new Intent(CoingrabActivity.this ,SingleplayerActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.menuShowHighScore:
                //showHighScore();
                return true;

            case R.id.avengers:
                //avengers();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }





    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            timeRemaining-= GAME_TIMER_TICK_VALUE;
            TextView timeView = findViewById(R.id.txtTime);
            String message = "time:" + timeRemaining / 1000f;
            timeView.setText(message);

            doMove();

            // if time to finish the game
            if (timeRemaining <= 0) {
                gameEnded();
            }

            return true;
        }});


    private void doMove() {
        // get window size
        ConstraintLayout mainLayout = findViewById(R.id.coingrabLayout);
        float futureX = player.getX() + moveX;
        float futureY = player.getY() + moveY;

        if (((futureX > 0 && (futureX + player.getWidth()) < mainLayout.getWidth())) &&
                ((futureY > 0 && (futureY + player.getHeight()) < mainLayout.getHeight()))) {
            player.setX(futureX);
            player.setY(futureY);
        }

        checkCollision();
    }


    public void doLeft(View view) {
        setPlayerMovement(MOVE_DELTA * -1, 0);
    }

    private void setPlayerMovement(int x, int y) {
        moveX = x;
        moveY = y;
    }

    public void doRight(View view) {
        setPlayerMovement(MOVE_DELTA, 0);
    }

    public void doUp(View view) {
        setPlayerMovement(0, MOVE_DELTA * -1);
    }

    public void doDown(View view) {
        setPlayerMovement(0, MOVE_DELTA);
    }


    //function moves coin to random spot on screen
    private void moveCoin() {
        Random randomGenerator = new Random();

        // get window size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int maxHeight = displayMetrics.heightPixels;
        int maxWidth = displayMetrics.widthPixels;

        Button btnUp = findViewById(R.id.btnUp);
        int x = randomGenerator.nextInt(maxWidth - coin.getWidth());
        int y = randomGenerator.nextInt(maxHeight - (int)btnUp.getY());

        coin.setX(x);
        coin.setY(y);


        ObjectAnimator mover = ObjectAnimator.ofFloat(coin , "translationY", 400f, 0f);
        mover.setDuration(3500);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(coin ,"alpha", 0f, 1f);
        fadeIn.setDuration(3500);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeIn).with(mover);
        animatorSet.start();


    }


    private void startNewGame() {

        moveX = 0;
        moveY = 0;

        player.setX(PLAYER_START_X);
        player.setY(PLAYER_START_Y);

        // restart game timer
        if (timer != null) {
            timer.cancel();
        }

        timeRemaining = GAME_TIMER_START_VALUE;
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                // signal UI thread to do timer actions
                mHandler.obtainMessage(1).sendToTarget();
            }
        }, NEW_GAME_DELAY, GAME_TIMER_TICK_VALUE);

        // disable buttons logic
        changeAllButtonsEnablement(true);


        score = -1;
        updateScore();
        moveCoin();

        //start background music whenh game starts
        soundManager.startMusic(this);
    }
    //function checks if player reached the current coin on screen
    private void checkCollision() {
        int[] location = new int[2];

        player.getLocationInWindow(location);
        Rect rectPlayer = new Rect(location[0], location[1],location[0] + player.getWidth(), location[1] + player.getHeight());

        coin.getLocationInWindow(location);
        Rect rectCoin = new Rect(location[0], location[1],location[0] + coin.getWidth(), location[1] + coin.getHeight());

        // collision is detected
        if (Rect.intersects(rectPlayer, rectCoin)) {
            // load coin sound
            soundManager.performSound();
            //move coin to a new locaton
            moveCoin();
            //updates the current score
            updateScore();
        }
    }

    // function upadtes the score
    private void updateScore() {
        score++;
        TextView textScore = findViewById(R.id.txtScore);
        textScore.setText("the score is: " + score);
    }

    private void gameEnded() {
        timer.cancel();

        // disable buttons logic
        changeAllButtonsEnablement(false);

        //stop background music when game ends
        soundManager.stopMusic();

        //vibration for game ending
        performVibration();


    }
    private void changeAllButtonsEnablement(boolean isEnabled) {
        changeButtonEnablement(R.id.btnUp, isEnabled);
        changeButtonEnablement(R.id.btnDown, isEnabled);
        changeButtonEnablement(R.id.btnLeft, isEnabled);
        changeButtonEnablement(R.id.btnRight, isEnabled);
    }

    private void changeButtonEnablement(int buttonID, boolean isEnabled) {
        Button button = findViewById(buttonID);
        button.setEnabled(isEnabled);
    }
    private void showHighScore() {
        // Toast.makeText(this, getString(R.string.message_no_highscore_yet),
        //        Toast.LENGTH_SHORT).show();
    }
    private void performVibration() {
        // Get Vibrator from the current Context
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        // Vibrate for 500 milliseconds
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(500);
        }
    }
}
