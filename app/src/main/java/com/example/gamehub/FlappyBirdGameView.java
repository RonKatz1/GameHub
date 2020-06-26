package com.example.gamehub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import java.util.Random;

public class FlappyBirdGameView extends View {

    //this is out custom View class
    Handler handler; //handler is required to schedule a runnable after some delay
    Runnable runnable;
    final int Update_Millisec = 30;
    Bitmap background;
    Bitmap topTube, bottomTube;
    Display display;
    Point point;
    int DWidth, DHeight; //Device width and height
    Rect rect;
    Bitmap[] birds;
    int birdsFrame = 0;
    int velocity=0, gravity=3;
    int birdX, birdY;
    boolean gameState = false;
    int gap = 400; //gap between tubes
    int minTubeOffset, maxTubeOffset;
    int numberOfTubes = 4;
    int distanceBetweenTubes;
    int[] tubeX = new int[numberOfTubes];
    int[] toptubeY = new int[numberOfTubes];
    Random random;
    int tubeVolacity = 8;
    int score = 0;

    public FlappyBirdGameView(Context context) {
        super(context);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate(); //this will call onDraw
            }
        };
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        topTube = BitmapFactory.decodeResource(getResources(), R.drawable.toptube);
        bottomTube = BitmapFactory.decodeResource(getResources(), R.drawable.bottomtube);
        display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        DWidth = point.x;
        DHeight = point.y;
        rect = new Rect(0, 0, DWidth, DHeight);
        birds = new Bitmap[2];
        birds[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bird1);
        birds[1] = BitmapFactory.decodeResource(getResources(), R.drawable.bird2);
        birdX = DWidth / 2 - birds[0].getWidth() / 2;
        birdY = DHeight / 2 - birds[0].getHeight() / 2;
        distanceBetweenTubes = DWidth * 3 / 4;
        minTubeOffset = gap / 2;
        maxTubeOffset = DHeight - minTubeOffset - gap;
        random = new Random();
        for (int i = 0; i < numberOfTubes; i++) {
            tubeX[i] = DWidth + i*distanceBetweenTubes;
            toptubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset + 1);
        }

    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //we'll draw our view inside onDraw()
        //draw the background on canvas
        canvas.drawBitmap(background,null,rect,null);

        //display bird in the center of the screen
        if(birdsFrame == 0)
            birdsFrame = 1;
        else
            birdsFrame = 0;
        if(gameState) {
            if (birdY < DHeight - birds[0].getHeight() || velocity < 0) //bird should be on screen - cant go beyond screen
            {
                velocity += gravity; //as the bird falls, it gets faster and faster as the velocity value increase by gravity
                birdY += velocity;
            }
            for (int i =0; i<numberOfTubes;i++) {
                tubeX[i] -= tubeVolacity;
                if(tubeX[i] < -topTube.getWidth())
                {
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    toptubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset + 1);

                }
                canvas.drawBitmap(topTube, tubeX[i], toptubeY[i] - topTube.getWidth(), null);
                canvas.drawBitmap(bottomTube, tubeX[i], toptubeY[i] + gap, null);
            }

            int XtubeLeft = 0;
            int XtubeRight = 40;
            for(int i=0;i<numberOfTubes;i++) {
                if (tubeX[i] >= XtubeLeft && tubeX[i] <= XtubeRight) {
                    int Ytopgap = toptubeY[i];
                    int Ybottomgap = toptubeY[i] + gap;
                    if(birdY <= Ytopgap || birdY >= Ybottomgap)
                    {
                        GameOver();
                        break;
                    }
                }
            }
        }

        //birds display in center of screen
        canvas.drawBitmap(birds[birdsFrame], birdX, birdY, null);

        handler.postDelayed(runnable, Update_Millisec);
    }

    //Touch event
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){ //if tap detected
            //move bird upward
            velocity = -30;
            gameState = true;
        }

        return true;
    }

    public void GameOver(){
        Intent intent = new Intent(getContext(), FlappyBirdGameOver.class);
        //intent.putExtra("UserScore", score);
        getContext().startActivity(intent);
    }
}
