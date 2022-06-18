package com.example.thebasegame.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thebasegame.R;
import com.example.thebasegame.model.Game;
import com.example.thebasegame.model.Question;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    Button[] buttons;
    TextView enteredText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        Bundle extras = getIntent().getExtras();
        Game g = (Game)extras.get("game");
        Question q = g.getNextQuestion();

        TextView gameProgressText = findViewById(R.id.gameProgress);
        TextView timerText = findViewById(R.id.timer);
        TextView baseText = findViewById(R.id.base);
        TextView givenText = findViewById(R.id.givenNumber);
        enteredText = findViewById(R.id.entered);

        buttons = new Button[10];
        buttons[0] = findViewById(R.id.zero);buttons[0].setOnClickListener(this);
        buttons[1] = findViewById(R.id.one);buttons[1].setOnClickListener(this);
        buttons[2] = findViewById(R.id.two);buttons[2].setOnClickListener(this);
        buttons[3] = findViewById(R.id.three);buttons[3].setOnClickListener(this);
        buttons[4] = findViewById(R.id.four);buttons[4].setOnClickListener(this);
        buttons[5] = findViewById(R.id.five);buttons[5].setOnClickListener(this);
        buttons[6] = findViewById(R.id.six);buttons[6].setOnClickListener(this);
        buttons[7] = findViewById(R.id.seven);buttons[7].setOnClickListener(this);
        buttons[8] = findViewById(R.id.eight);buttons[8].setOnClickListener(this);
        buttons[9] = findViewById(R.id.nine);buttons[9].setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zero:
                enteredText.append("0");
                break;
            case R.id.one:
                enteredText.append("1");
                break;
            case R.id.two:
                enteredText.append("2");
                break;
            case R.id.three:
                enteredText.append("3");
                break;
            case R.id.four:
                enteredText.append("4");
                break;
            case R.id.five:
                enteredText.append("5");
                break;
            case R.id.six:
                enteredText.append("6");
                break;
            case R.id.seven:
                enteredText.append("7");
                break;
            case R.id.eight:
                enteredText.append("8");
                break;
            case R.id.nine:
                enteredText.append("9");
                break;
            default:
                break;

        }
    }
}