package com.example.gamehub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private final int GAME_TIMER_START_VALUE = 15000;
    // UI update interval in ms
    private final int GAME_TIMER_TICK_VALUE = 100;
    // Initial delay when new game starts
    private final int NEW_GAME_DELAY = 300;

    //for hand movement
    // private GestureDetectorCompat mDetector;

    private ImageView player;
    private ImageView coin;

    private int score = 0;

    private Timer timer;
    private int timeRemaining;

    //SoundManager soundManager = new SoundManager();


    private int moveX;
    private int moveY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coingrab);

        player = findViewById(R.id.playerId);
        coin = findViewById(R.id.coinId);

        ConstraintLayout mainLayout = findViewById(R.id.coingrabLayout);
        mainLayout.post(new Runnable() {
            @Override
            public void run() {
                startNewGame();
            }
        });
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

        //soundManager.startMusic(this);
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
            // do collision action
            // soundManager.performSound();
            moveCoin();
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

        //soundManager.stopMusic();

        //performVibration();


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
}
