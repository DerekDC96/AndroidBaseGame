package com.example.thebasegame.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.thebasegame.R;
import com.example.thebasegame.model.Diff;
import com.example.thebasegame.model.Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class ViewRecordActivity extends AppCompatActivity {

    Spinner spinner;
    Diff diff;
    int base;

    ArrayList<Pair<Integer, LocalDateTime>> unsorted;
    ArrayList<Pair<Integer, LocalDateTime>> sorted;

    TableLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_record);

        File dir = getFilesDir();
        File[] files = dir.listFiles();

        Button easyButton = findViewById(R.id.viewEasy);
        Button medButton = findViewById(R.id.viewMed);
        Button hardButton = findViewById(R.id.viewHard);

        // by default spinner is populated with fixed values from bases_array
        spinner = findViewById(R.id.baseRecords);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.bases_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // by default difficulty is easy
        this.diff = Diff.EASY;

        Bundle extras = getIntent().getExtras();
        // not null when view records is accessed from ScoreScreenActivity, in which extras are passed
        if (extras != null) {
            Game g = (Game) extras.get("game");
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
            this.diff = g.getDiff();
            this.base = g.getBase();
            updateContentDiff();
            updateContentBase();
        }

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyButton.setBackgroundColor(Color.RED);
                medButton.setBackgroundColor(Color.BLUE);
                hardButton.setBackgroundColor(Color.BLUE);
                diff = Diff.EASY;
                updateContentDiff();
            }
        });

        medButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyButton.setBackgroundColor(Color.BLUE);
                medButton.setBackgroundColor(Color.RED);
                hardButton.setBackgroundColor(Color.BLUE);
                diff = Diff.MED;
                updateContentDiff();
            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyButton.setBackgroundColor(Color.BLUE);
                medButton.setBackgroundColor(Color.BLUE);
                hardButton.setBackgroundColor(Color.RED);
                diff = Diff.HARD;
                updateContentDiff();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                base = Integer.parseInt(spinner.getSelectedItem().toString());
                updateContentBase();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // table setup
        tl = (TableLayout) findViewById(R.id.main_table);
        TableRow tr_head = new TableRow(this);

    }

    // EFFECTS: updates spinner to match bases with records in folder of this.diff
    private void updateContentDiff() {
        try {
            // pathname based on diff specified
            File dir = new File(getFilesDir() + File.separator + diff.toString());
            File[] files = dir.listFiles();

            String[] arr = new String[files.length];

            // parse files in subdir for base names to put into array
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
            Log.e("ViewRecordActivity", "updateContent1", e);
        }
    }


    // EFFECTS: updates table when spinner item is clicked, sorting the table by
    //          score and date, and updates file
    // file consists of two sections
        // first section:
            // sorted with score(Integer), datetime(LocalDateTime), rank(Integer)
        // second section:
            // unsorted with score(Integer), datetime(LocalDateTime)
    private void updateContentBase() {
        readFileIntoArrays();
        // merge the two sorted lists
        ArrayList<Pair<Integer, LocalDateTime>> merged = mergeSortedLists();

        // write merged list to file
        updateFile(merged);

        // update table with merged list
        updateTable(merged);
    }

    // EFFECTS:
    // given l1 (sorted), and l2 (unsorted)
    // sorts from scores(high to low), then on dateTime(early to late)
    // returns a sorted list
    private ArrayList<Pair<Integer, LocalDateTime>>
    mergeSortedLists() {

        // sort the unsorted
        unsorted.sort(new Comparator<Pair<Integer, LocalDateTime>>() {
            @Override
            public int compare(Pair<Integer, LocalDateTime> o1, Pair<Integer, LocalDateTime> o2) {
                // if equal
                if (Integer.compare(o1.first, o2.first) == 0) {
                    // compare on datetime
                    return o1.second.compareTo(o2.second);
                } else {
                    return Integer.compare(o2.first, o1.first);
                }
            }
        });

        ArrayList<Pair<Integer, LocalDateTime>> mergedList = new ArrayList<>();

        int m = sorted.size(); int n = unsorted.size();
        int i = 0; int j = 0;

        while (i < m && j < n) {
            if (sorted.get(i).first > unsorted.get(j).first) {
                mergedList.add(sorted.get(i));
                i++;
            } else if (sorted.get(i).first < unsorted.get(j).first) {
                mergedList.add(unsorted.get(j));
                j++;
            } else {
                if (sorted.get(i).second.isAfter(unsorted.get(j).second)) {
                    mergedList.add(unsorted.get(j));
                    j++;
                } else {
                    mergedList.add(sorted.get(i));
                    i++;
                }
            }
        }
        if (i == m) {
            while (j < n) {
                mergedList.add(unsorted.get(j));
                j++;
            }
        } else {
            while (i < m) {
                mergedList.add(sorted.get(i));
                i++;
            }
        }
        return mergedList;
    }

    // EFFECTS:
    // reads file specified by pathname, which is based on this.diff and this.base
    // and updates sorted and unsorted with the contents of the file
    private void readFileIntoArrays() {
        unsorted = new ArrayList<>();
        sorted = new ArrayList<>();
        try {
            // read file
            BufferedReader reader =
                    new BufferedReader(new FileReader(getFilesDir() + File.separator + diff + File.separator + base + ".txt"));

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
        } catch (FileNotFoundException e) {
            Log.e("ViewRecordActivity", "updateContent2", e);
        } catch (IOException e) {
            Log.e("ViewRecordActivity", "updateContent2", e);
        }
    }

    // EFFECTS: writes to the file with the sorted list
    private void updateFile(ArrayList<Pair<Integer, LocalDateTime>> ranking) {
        File dir = new File(getFilesDir() + File.separator + diff);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName =  base + ".txt";
        FileWriter fileWriter = null;
        try {
            File f = new File(dir,fileName);
            if (!f.exists()) {
                f.createNewFile();
            }
            fileWriter = new FileWriter(getFilesDir() + File.separator + diff + File.separator + fileName,false);

            // formatting to match reader parser
            int rank = 1;
            for (Pair<Integer, LocalDateTime> entry : ranking) {
                String output = entry.first + "," + entry.second + "," + rank + "\n";
                fileWriter.write(output);
                rank++;
            }
        } catch (IOException e) {
            Log.e("GameActivity", "updateContent2", e);
        } finally {
            if (fileWriter != null) try { fileWriter.close(); } catch (IOException ignore) {}
        }
    }

    // EFFECTS: given a ranking list, displays the up to 10 entries in the table GUI
        // the list should be sorted
    private void updateTable(ArrayList<Pair<Integer, LocalDateTime>> ranking) {
        tl.removeAllViews();
        int rows = Math.min(10, ranking.size());
        TextView[] scoreArray = new TextView[rows];
        TextView[] dateArray = new TextView[rows];
        TableRow[] trHead = new TableRow[rows];

        for (int i = 0; i < rows; i++) {
            // create row
            trHead[i] = new TableRow(this);
            trHead[i].setId(i+1);
            trHead[i].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            // modify textViews
            scoreArray[i] = new TextView(this);
            dateArray[i] = new TextView(this);
            scoreArray[i].setId(i+10);
            dateArray[i].setId(i+100);
            scoreArray[i].setText(String.valueOf(ranking.get(i).first));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            dateArray[i].setText(ranking.get(i).second.format(formatter));

            trHead[i].addView(scoreArray[i]);
            trHead[i].addView(dateArray[i]);

            tl.addView(trHead[i], new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }



}
