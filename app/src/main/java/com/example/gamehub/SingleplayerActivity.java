package com.example.gamehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SingleplayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer);

        findViewById(R.id.coingrabId).setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(SingleplayerActivity.this ,CoingrabActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.FlappyBirdBtn1).setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(SingleplayerActivity.this , FlappyBird.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.snakeId).setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(SingleplayerActivity.this ,SnakeActivity.class);
                startActivity(intent);



            }
        });
    }
}
