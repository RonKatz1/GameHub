package com.example.gamehub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class StartFlappyBirdGame extends Activity {

    FlappyBirdGameView gameView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new FlappyBirdGameView(this);
        setContentView(gameView);
    }
}
