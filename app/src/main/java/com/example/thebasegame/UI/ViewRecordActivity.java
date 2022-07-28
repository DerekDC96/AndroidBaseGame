package com.example.thebasegame.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.thebasegame.R;
import com.example.thebasegame.model.Diff;
import com.example.thebasegame.model.Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import kotlin.Triple;

public class ViewRecordActivity extends AppCompatActivity {

    Spinner spinner;
    //Table table;
    Diff diff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_record);
        File dir = getFilesDir();
        File[] files = dir.listFiles();


        Button easyButton = findViewById(R.id.viewEasy);
        Button medButton = findViewById(R.id.viewMed);
        Button hardButton = findViewById(R.id.viewHard);
        spinner = findViewById(R.id.baseRecords);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.bases_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        try {
            // below two lines will throw exception if ViewRecordActivity is accessed from mainActivity
            Bundle extras = getIntent().getExtras();
            Game g = (Game)extras.get("game");

            // color correct difficulty button from recent game
            switch (g.getDiff()) {
                case MED:
                    medButton.setBackgroundColor(Color.RED);
                    break;
                case HARD:
                    hardButton.setBackgroundColor(Color.RED);
                    break;
                default:
                    easyButton.setBackgroundColor(Color.RED);
                    break;
            }
            updateContent(g.getDiff());
            updateContent(g.getBase());

        } catch (Exception e) {

        }

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyButton.setBackgroundColor(Color.BLUE);
                medButton.setBackgroundColor(Color.RED);
                hardButton.setBackgroundColor(Color.RED);
                updateContent(Diff.EASY);
            }
        });

        medButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyButton.setBackgroundColor(Color.BLUE);
                medButton.setBackgroundColor(Color.RED);
                hardButton.setBackgroundColor(Color.RED);
                updateContent(Diff.MED);
            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyButton.setBackgroundColor(Color.BLUE);
                medButton.setBackgroundColor(Color.RED);
                hardButton.setBackgroundColor(Color.RED);
                updateContent(Diff.HARD);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int base = Integer.parseInt(spinner.getSelectedItem().toString());
                updateContent(base);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    // EFFECTS: updates spinner when difficulty button is clicked
    private void updateContent(Diff d) {
        this.diff = d;
        try {
            // create a folder for the difficulty
            File subDir = new File(diff.toString());
            File[] files = subDir.listFiles();
            String[] arr = new String[files.length];
            // parse files in subdir for names to put into array
            for (int i = 0; i < files.length; i++) {
                arr[i] = files[i].getName().replace(".txt", "");
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    ViewRecordActivity.this,
                    android.R.layout.simple_spinner_item,
                    arr);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("Exception", "updateContent1", e);
        }
    }


    // EFFECTS: updates table when spinner item is clicked, sorting the table by
    //          score and date, and updates file
    private void updateContent(int base) {
        try {
            // open file
            String pathName = getFilesDir() + File.separator + base + ".txt";
            FileInputStream fis = openFileInput(pathName);

            // two different arraysLists, one with previously labeled (sorted) entries = triple
            // other with non-labeled entries = pair
            // each row is a tuple (pair or trip)


            ArrayList<Pair<Integer, LocalDateTime>> unsorted = new ArrayList<>();
            ArrayList<Pair<Integer, LocalDateTime>> sorted = new ArrayList<>();

            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);

            BufferedReader reader = new BufferedReader(inputStreamReader);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            String line = reader.readLine();
            while (line != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    sorted.add(new Pair<>(Integer.valueOf(parts[0]),
                            LocalDateTime.parse(parts[1])));
                } else if (parts.length == 2) {
                    unsorted.add(new Pair<>(Integer.valueOf(parts[0]),
                            LocalDateTime.parse(parts[1])));
                }
                line = reader.readLine();
            }


            // sort the unsorted
            unsorted.sort(new Comparator<Pair<Integer, LocalDateTime>>() {
                @Override
                public int compare(Pair<Integer, LocalDateTime> o1, Pair<Integer, LocalDateTime> o2) {
                    // if equal
                    if (Integer.compare(o1.first, o2.first) == 0) {
                        // compare on datetime
                        return o1.second.compareTo(o2.second);
                    } else {
                        return Integer.compare(o1.first, o2.first);
                    }
                }
            });

            // merge the two sorted lists
            mergeSortedLists(sorted, unsorted);


        } catch (FileNotFoundException e) {
            Log.e("Exception", "updateContent2", e);
        } catch (IOException e) {
            Log.e("Exception", "updateContent2", e);
        }
    }

    private List<Pair<Integer, LocalDateTime>>
    mergeSortedLists(List<Pair<Integer, LocalDateTime>> l1, List<Pair<Integer, LocalDateTime>> l2) {
        ArrayList<Pair<Integer, LocalDateTime>> mergedList = new ArrayList<>();

        int m = l1.size(); int n = l2.size();
        int i = 0; int j = 0;
        while (i < m && j < n) {
            //if (l1 > l2);
        }
        return null;
    }

}
