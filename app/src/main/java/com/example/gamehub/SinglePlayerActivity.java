package com.example.gamehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SinglePlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        findViewById(R.id.coinId).setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(SinglePlayerActivity.this ,GrabCoinActivity.class);
                startActivity(intent);


            }
        });
    }
}
