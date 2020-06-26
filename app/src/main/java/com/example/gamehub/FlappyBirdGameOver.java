package com.example.gamehub;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FlappyBirdGameOver extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flappy_bird_game_over);

/*        Intent intent = getIntent();
        int userScore = intent.getIntExtra("UserScore", 0);
        TextView textView = (TextView)this.findViewById(R.id.UserScoreTextID);
        textView.setText(getResources().getString(R.string.your_score_is) + " " + userScore);*/
    }

    public void StartOver(View view)
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