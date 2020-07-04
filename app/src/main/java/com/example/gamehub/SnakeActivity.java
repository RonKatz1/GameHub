package com.example.gamehub;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;


import androidx.appcompat.app.AppCompatActivity;

import com.example.gamehub.snake.GameStage;

public class SnakeActivity extends AppCompatActivity {

    private GameStage gameStage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        SharedPreferences prefs = getSharedPreferences(
                "com.example.gamehub", Context.MODE_PRIVATE);
        gameStage = new GameStage(this, size, prefs);
        setContentView(gameStage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameStage.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameStage.pause();
    }
}
