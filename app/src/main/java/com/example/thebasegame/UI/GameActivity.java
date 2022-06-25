package com.example.thebasegame.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thebasegame.R;
import com.example.thebasegame.model.Game;
import com.example.thebasegame.model.Question;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    Button[] buttons;
    TextView enteredText;
    Game g;
    Question q;

    TextView baseText;
    TextView questionText;
    TextView gameProgressText;
    TextView timerText;

    CountDownTimer cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        Bundle extras = getIntent().getExtras();
        g = (Game)extras.get("game");

        timerText = findViewById(R.id.timer);

        baseText = findViewById(R.id.base);
        baseText.setText("Base " + String.valueOf(g.getBase()));
        questionText = findViewById(R.id.givenNumber);
        enteredText = findViewById(R.id.entered);
        gameProgressText= findViewById(R.id.game_progress);

        buttons = new Button[11];
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
        buttons[10] = findViewById(R.id.del);buttons[10].setOnClickListener(this);

        updateContent();
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
            case R.id.del:
                String text = enteredText.getText().toString();
                if (text.length() > 0) {
                    text = text.substring(0, text.length() - 1);
                }
                enteredText.setText(text);
            default:
                break;

        }
    }

    public void updateContent() {
        if (g.getCurrQuestionIndex() == g.getNUMQUESTIONS()) {

            // write to file
            String fileName = "record_" + g.getBase()  + "_" + g.getDiff() + ".txt";
            String output = g.getScore() + "," + LocalDateTime.now() + "\n";
            try {
                FileOutputStream fos = this.openFileOutput(fileName, Context.MODE_APPEND);
                fos.write(output.getBytes());
            } catch (Exception e) {
                Log.e("file", "exception", e);
            }

            Intent intent = new Intent(GameActivity.this, ScoreScreenActivity.class);
            intent.putExtra("game", g);
            startActivity(intent);
        } else {
            gameProgressText.setText("Game progress: " + g.getCurrQuestionIndex() + "/" + g.getNUMQUESTIONS() + "\n" +
                    "Current score: " + g.getScore() + "/" + g.getMaxScore());
            enteredText.setText("");
            q = g.getCurrQuestion();
            questionText.setText(q.toString());
            cd = new CountDownTimer(1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    timerText.setText(""+millisUntilFinished/1000);
                }

                @Override
                public void onFinish() {
                    String entered = enteredText.getText().toString();
                    if (!entered.equals("")) {
                        g.processAnswer(entered);
                    }
                    g.nextQuestion();
                    updateContent();
                }
            }.start();

        }

    }
}