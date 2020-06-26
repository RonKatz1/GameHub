package com.example.gamehub;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.time.Instant;

public class FlappyBird extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flappy_bird);
    }

    public void startGame(View view)
    {
        Intent intent = new Intent(this, StartFlappyBirdGame.class);
        startActivity(intent);
        finish();
    }

    public void ReturnToMenu(View view)
    {
        Intent intent = new Intent(this, SingleplayerActivity.class);
        startActivity(intent);
        finish();
    }
}
