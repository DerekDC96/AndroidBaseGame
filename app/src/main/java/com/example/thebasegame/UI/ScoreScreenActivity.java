package com.example.thebasegame.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewKt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thebasegame.R;
import com.example.thebasegame.model.Game;

public class ScoreScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_screen);

        Bundle extras = getIntent().getExtras();
        Game g = (Game)extras.get("game");

        TextView scoreText = findViewById(R.id.score_text);
        scoreText.setText("TODO score screen text");

        Button viewRecords = findViewById(R.id.view_records);
        viewRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreScreenActivity.this, ViewRecordActivity.class);
                intent.putExtra("game", g);
                startActivity(intent);
            }
        });

        Button playAgain = findViewById(R.id.play_again);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreScreenActivity.this, GameActivity.class);
                Game newGame = new Game(g.getBase(), g.getDiff());
                intent.putExtra("game", newGame);
                startActivity(intent);
            }
        });

        Button returnHome = findViewById(R.id.return_home);
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScoreScreenActivity.this, MainActivity.class));
            }
        });



    }
}