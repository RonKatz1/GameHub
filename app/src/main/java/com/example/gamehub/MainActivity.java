package com.example.gamehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable(){
        @Override
        public void run() {
            try
            {
                synchronized (this) {
                    wait(2000);
                    Intent intent = new Intent(MainActivity.this, IndexActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        }).start();


        /**
         * findViewById(R.id.button1).setOnClickListener(new View.OnClickListener () {
         *             @Override
         *             public void onClick (View v){
         *                 Intent intent = new Intent(MainActivity.this ,IndexActivity.class);
         *                 startActivity(intent);
         *                 finish();
         *
         *             }
         *         });
         */

    }
}
