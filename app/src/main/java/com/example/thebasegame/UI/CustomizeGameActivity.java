package com.example.thebasegame.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.thebasegame.R;
import com.example.thebasegame.model.Game;

public class CustomizeGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customize_game);

        // difficulty buttons and logic
        Button easy = findViewById(R.id.easy);
        Button med = findViewById(R.id.medium);
        Button hard = findViewById(R.id.hard);
        easy.setBackgroundColor(Color.BLUE);
        int[] mode = new int[2]; // array allows inner classes to modify outer variables
        mode[0] = 3;
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setBackgroundColor(Color.BLUE);
                med.setBackgroundColor(Color.RED);
                hard.setBackgroundColor(Color.RED);
                mode[0] = 3;
            }
        });
        med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setBackgroundColor(Color.RED);
                med.setBackgroundColor(Color.BLUE);
                hard.setBackgroundColor(Color.RED);
                mode[0] = 2;
            }
        });
        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easy.setBackgroundColor(Color.RED);
                med.setBackgroundColor(Color.RED);
                hard.setBackgroundColor(Color.BLUE);
                mode[0] = 1;
            }
        });

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.bases_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mode[1] = Integer.valueOf(parent.getItemAtPosition(pos).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Button startGame = findViewById(R.id.startGame);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomizeGameActivity.this, GameActivity.class);
                Game g = new Game(mode[1], mode[0]);
                intent.putExtra("game", g);
                startActivity(intent);
            }
        });






    }
}