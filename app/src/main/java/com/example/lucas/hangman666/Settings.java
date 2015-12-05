package com.example.lucas.hangman666;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Settings code, it includes game mode, word length, lives and possibility to add cheat codes
 */
public class Settings extends Gameplay{

    // initialise switch
    private Switch mode;
    private EditText cheats;
    private String godMode = "g0dm0d3";
    private String superScore = "1337h15c0r3";
    private SeekBar wordLen;
    private SeekBar lives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        // load switch
        mode = (Switch) findViewById(R.id.toggleEvil);
        cheats = (EditText) findViewById(R.id.editText);
        wordLen = (SeekBar) findViewById(R.id.seekBar);
        lives = (SeekBar) findViewById(R.id.limbBar);

        wordLen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                wordLength = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // do nothing
            }
        });

        lives.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                livesTries = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // do nothing
            }
        });


        cheats.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String entry = cheats.getText().toString();
                    cheats.setText("");
                    if (entry.equals(godMode)){

                        // todo add codeMode
                    }
                    if (entry.equals(superScore)){
                        // todo add codeMode
                    }
                    handled = true;
                }
                return handled;
            }
        });

        readSettings();
    }


    // check if switch on
    public void toggleMode(View view) {
        // if on make evil
        if (mode.isChecked()){
            evilGame = Boolean.TRUE;
            // write settings to file for future
            writeSettings("evil", Integer.toString(wordLength), Integer.toString(livesTries));
        }
        // else good
        else{
            evilGame = Boolean.FALSE;
            // write settings to file
            writeSettings("good", Integer.toString(wordLength), Integer.toString(livesTries));
        }
    }



}
