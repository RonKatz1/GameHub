package com.example.gamehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class IndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);


        findViewById(R.id.singleBtn).setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(IndexActivity.this ,SingleplayerActivity.class);
                startActivity(intent);


            }
        });


    }
}
