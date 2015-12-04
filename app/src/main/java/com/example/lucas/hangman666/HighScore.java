package com.example.lucas.hangman666;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Lucas on 29-11-2015.
 */
public class HighScore extends Gameplay {

    private long time;
    private String name;
    private ListView scoreList;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score);

        // added is false
        Boolean added = Boolean.FALSE;

        // get info from prev screen
        Intent getScore = getIntent();
        time = getScore.getExtras().getLong("time");
        name = getScore.getExtras().getString("addedName");

        // cast to string and concatenate
        String timeString = Long.toString(time);

        String record = name + ": " + timeString;

        // find and load list of scores
        scoreList = (ListView) findViewById(R.id.scoreList);
        items = new ArrayList<>();

        // find items from saved file to add
        readItems();

        // set list adapter
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        scoreList.setAdapter(itemsAdapter);

        // get size of score list
        int listLength = scoreList.getAdapter().getCount();

        if (listLength != 0) {
            int i = 0;
            // compares current time with other times
            while ((i < listLength) & (added == Boolean.FALSE)) {

                String current = scoreList.getItemAtPosition(i).toString();
                String[] raw = current.split(": ");

                // if current time is lower than current, add at current place
                if ((time < Integer.parseInt(raw[1])) & (record.length() != 0)) {
                    items.add(i, record);
                    record = "";
                    added = Boolean.TRUE;
                }
                // if current time higher than last on list, add to bottom
                else if (( i == (listLength - 1) & time > Integer.parseInt(raw[1]))
                        & (record.length() != 0)){
                    items.add(record);
                    record = "";
                    added = Boolean.TRUE;
                }
                i++;
            }
        }
        // normal adding if first item in list
        else{

            itemsAdapter.add(record);
        }

        // write to list
        writeItems();


    }

    // detect back pressed
    @Override
    public void onBackPressed() {

        // reboot with new game
//        super.checkGameplay();
    }

    // method for reading scores
    private void readItems() {
        // open file
        File file = getFilesDir();
        File todoFile = new File(file, "scores.txt");
        // try to find items to add
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    // method for saving scores
    private void writeItems() {

        // open file
        File file = getFilesDir();
        File todoFile = new File(file, "scores.txt");

        // try to add items to file
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void deleteScores(View view) {

        // pop up for deletion
        AlertDialog.Builder deleteScores = new AlertDialog.Builder(this);

        // set title + message
        deleteScores.setTitle("Delete high score");
        deleteScores.setMessage("Are you sure you want to delete all high scores?");

        // if yes
        deleteScores.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // deletes all high scores and updates adapter
                itemsAdapter.clear();
                itemsAdapter.notifyDataSetChanged();
                writeItems();
            }
        });

        // if no
        deleteScores.setNegativeButton("Keep scores", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });

        // open pop up
        AlertDialog promptDel = deleteScores.create();
        promptDel.show();
    }
}
