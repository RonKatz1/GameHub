package com.example.gamehub.snake;


import com.example.gamehub.model.Controls;
import com.example.gamehub.model.Snake;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.example.gamehub.R;


public class GameStage extends SurfaceView implements Runnable {

    private final int MILLIS_PER_SECOND = 1000;
    private final int widthBlocks = 50;
    private final int FPS = 10;
    private volatile boolean isActive;
    private volatile boolean currentlyPlaying;
    private int x;
    private int y;
    private int snakeBSize;
    private int controllerButtoSize;
    private int lengthBlocks;
    private long nextFrameTS;
    private int maxScreenBlocks;
    private int score;

    private String curScore;
    private String lastScoreRecorded;
    private String startPrompt;
    private String conMSG;

    private int bgColor;
    private int textColor;
    private int snakeColor;
    private int foodColor;
    private int controllerColor;
    private SharedPreferences prefs;

    private Thread thread = null;
    private final SurfaceHolder surHold;
    private final Paint paint;
    private Canvas canvas;

    private Snake snake;
    private Controls controller;
    private Rect food;

    public GameStage(Context context, Point size, SharedPreferences prefrences) {
        super(context);
        prefs = prefrences;
        curScore = getContext().getString(R.string.current_score);
        lastScoreRecorded = getContext().getString(R.string.last_score);
        startPrompt = getContext().getString(R.string.prompt);
        conMSG = getContext().getString(R.string.congratulations);
        bgColor = getContext().getResources().getColor(R.color.background);
        textColor = getContext().getResources().getColor(R.color.text);
        snakeColor = getContext().getResources().getColor(R.color.snake);
        foodColor = getContext().getResources().getColor(R.color.food);
        controllerColor = getContext().getResources().getColor(R.color.controllers);
        x = size.x;
        y = size.y;
        surHold = getHolder();
        paint = new Paint();
        snakeBSize = x / widthBlocks;
        lengthBlocks = y / snakeBSize;
        maxScreenBlocks = widthBlocks * lengthBlocks;
        controllerButtoSize = snakeBSize * 3;
        int controlsY = y - (controllerButtoSize * 5) - snakeBSize;
        controller = new Controls(snakeBSize, controlsY, controllerButtoSize);
        food = new Rect();
        nextFrameTS = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (isActive) {

            if (updateRequired()) {

                if (currentlyPlaying) {
                    update();
                }
                draw();
            }
        }
    }

    public void pause() {
        isActive = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isActive = true;
        thread = new Thread(this);
        thread.start();
    }

    private void startGame() {
        snake = new Snake(
                widthBlocks / 2,
                lengthBlocks / 2,
                Snake.Direction.RIGHT,
                maxScreenBlocks);

        spawnFood();
        score = 0;
        nextFrameTS = System.currentTimeMillis();

        currentlyPlaying = true;
    }

    private void spawnFood() {
        Random random = new Random();
        int rx;
        int ry;

        List xs = Arrays.asList(snake.bodyXs);
        List ys = Arrays.asList(snake.bodyYs);

        do {
            rx = random.nextInt(widthBlocks - 1);
            ry = random.nextInt(lengthBlocks - 1);

        } while (xs.contains(rx) && ys.contains(ry));

        int x = rx * snakeBSize;
        int y = ry * snakeBSize;

        food.set(
                x,
                y,
                x + snakeBSize,
                y + snakeBSize);
    }

    public boolean updateRequired() {
        if (nextFrameTS <= System.currentTimeMillis()) {

            nextFrameTS = System.currentTimeMillis() + MILLIS_PER_SECOND / FPS;
            return true;
        }
        return false;
    }

    public void update() {

        if ((snake.getHeadX() * snakeBSize) == food.left
                && (snake.getHeadY() * snakeBSize) == food.top) {
            eatFood();
        }

        snake.moveSnake();

        if (detectDeath()) {

            currentlyPlaying = false;
        }
    }

    private void eatFood() {

        score++;

        if (score < (maxScreenBlocks - 1)) {
            spawnFood();
            snake.increaseSize();
        } else {
            currentlyPlaying = false;
        }
    }

    private boolean detectDeath() {

        boolean dead = false;

        // Hit the screen edge
        if (snake.getHeadX() == -1) dead = true;
        if (snake.getHeadX() >= widthBlocks) dead = true;
        if (snake.getHeadY() == -1) dead = true;
        if (snake.getHeadY() == lengthBlocks) dead = true;

        // Hit itself
        for (int i = snake.getSnakeLength(); i > 0; i--) {
            if ((i > 4)
                    && (snake.getHeadX() == snake.bodyXs[i])
                    && (snake.getHeadY() == snake.bodyYs[i])) {
                dead = true;
            }
        }
        if (dead) {
            if (score > prefs.getInt("snakeHS", 0)) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("snakeHS", score);
                editor.apply();
            }
        }
        return dead;
    }

    private void draw() {
        if (surHold.getSurface().isValid()) {
            canvas = surHold.lockCanvas();
            canvas.drawColor(bgColor);
            if (currentlyPlaying) {
                drawGame(canvas, paint);
            } else {
                drawStart(canvas, paint);
            }

            surHold.unlockCanvasAndPost(canvas);
        }
    }

    private void drawGame(Canvas canvas, Paint paint) {
        paint.setColor(controllerColor);
        for (Rect control : controller.getButtons()) {
            canvas.drawRect(
                    control.left,
                    control.top,
                    control.right,
                    control.bottom,
                    paint);
        }
        paint.setColor(foodColor);
        canvas.drawRect(
                food.left,
                food.top,
                food.right,
                food.bottom,
                paint);
        paint.setColor(snakeColor);
        for (int i = 0; i < snake.getSnakeLength() + 1; i++) {
            canvas.drawRect(snake.bodyXs[i] * snakeBSize,
                    (snake.bodyYs[i] * snakeBSize),
                    (snake.bodyXs[i] * snakeBSize) + snakeBSize,
                    (snake.bodyYs[i] * snakeBSize) + snakeBSize,
                    paint);
        }
        paint.setTextSize(70);
        canvas.drawText(String.format(curScore, score, prefs.getInt("snakeHS", 0)), 10, 60, paint);
    }

    private void drawStart(Canvas canvas, Paint paint) {
        paint.setColor(textColor);
        paint.setTextSize(70);
        int halfScreen = x / 2;
        int halfText;
        if (score > 0) {
            String msgScore = String.format(lastScoreRecorded, score, prefs.getInt("snakeHS", 0));
            float scoreMeasure = paint.measureText(msgScore);
            halfText = Math.round(scoreMeasure / 2);
            canvas.drawText(
                    msgScore,
                    halfScreen - halfText,
                    (y / 2) - 100, paint);
        }

        if (score >= (maxScreenBlocks - 1)) {
            float congratsMeasure = paint.measureText(conMSG);
            halfText = Math.round(congratsMeasure / 2);
            canvas.drawText(
                    conMSG,
                    halfScreen - halfText,
                    (y / 2) - 200, paint);
        }
        float startMeasure = paint.measureText(startPrompt);
        halfText = Math.round(startMeasure / 2);
        canvas.drawText(startPrompt, halfScreen - halfText, y / 2, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            if (currentlyPlaying) {
                int posX = Math.round(motionEvent.getX());
                int posY = Math.round(motionEvent.getY());
                if (controller.getButton(Controls.Button.LEFT).contains(posX, posY)) {
                    snake.setCurrentDirection(Snake.Direction.LEFT);
                } else if (controller.getButton(Controls.Button.UP).contains(posX, posY)) {
                    snake.setCurrentDirection(Snake.Direction.UP);
                } else if (controller.getButton(Controls.Button.RIGHT).contains(posX, posY)) {
                    snake.setCurrentDirection(Snake.Direction.RIGHT);
                } else if (controller.getButton(Controls.Button.DOWN).contains(posX, posY)) {
                    snake.setCurrentDirection(Snake.Direction.DOWN);
                }
            } else {
                startGame();
            }
        }
        return super.onTouchEvent(motionEvent);
    }
}
