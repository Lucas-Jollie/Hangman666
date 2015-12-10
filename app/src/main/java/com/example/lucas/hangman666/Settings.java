package com.example.lucas.hangman666;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Settings code, it includes game mode, word length, lives and cheat codes
 */
public class Settings extends Gameplay{

    // initialise switch
    private Switch mode, lengthSwitch;
    private EditText cheats;
    private String godMod3 = "g0dm0d3";
    private String superScore = "1337h15c0r3";
    private SeekBar wordLen;
    private SeekBar lives;
    private TextView amLives, showLen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        // load views
        mode = (Switch) findViewById(R.id.toggleEvil);
        lengthSwitch = (Switch) findViewById(R.id.lengthSwitch);
        cheats = (EditText) findViewById(R.id.editText);
        wordLen = (SeekBar) findViewById(R.id.seekBar);
        lives = (SeekBar) findViewById(R.id.limbBar);
        amLives = (TextView) findViewById(R.id.limbText);
        showLen = (TextView) findViewById(R.id.textView3);

        // loads settings
        readSettings();

        // display current stats
        amLives.setText(amLives.getText().toString() + " " + String.valueOf(livesTries));
        showLen.setText(showLen.getText().toString() + " " + String.valueOf(wordLength));

        // update switch according to settings
        if (evilGame){
            mode.setChecked(true);
        }
        else{
            mode.setChecked(false);
        }

        // set seek bar to stored length and lives
        wordLen.setProgress(wordLength);
        lives.setProgress(livesTries);

        // detects change of word length
        wordLen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // force minimum size 1
                if (progress < 1){
                    progress = 1;
                }
                // updates word length
                wordLength = progress;

                // Display lives
                String data = showLen.getText().toString();
                String prog = String.valueOf(progress);
                String total = data + " " + prog;
                showLen.setText("");
                showLen.setText(total);


                updateSettings(wordLength, livesTries);

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

        // detects change in lives
        lives.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // force single live
                if (progress < 1){
                    progress = 1;
                }
                // update lives
                livesTries = progress;

                // display new lives
                String data = amLives.getText().toString();
                String prog = String.valueOf(progress);
                String total = data + " " + prog;
                amLives.setText("");
                amLives.setText(total);

                updateSettings(wordLength, livesTries);
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

        // cheat mode, detects entry with enter press
        cheats.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String entry = cheats.getText().toString();
                    cheats.setText("");

                    // enables god mode
                    if (entry.equals(godMod3)) {

                        godMode = true;
                    }

                    // enables a special high score
                    if (entry.equals(superScore)) {

                        hiScore = true;
                    }
                    handled = true;
                }
                return handled;
            }
        });

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

    protected void updateSettings(int len, int live){

        if (evilGame){
            // write settings to file for future
            writeSettings("evil", Integer.toString(len), Integer.toString(live));
        }

        // else good
        else{
            writeSettings("good", Integer.toString(len), Integer.toString(live));
        }
    }


    public void setAllLengths(View view) {
        if (lengthSwitch.isChecked()) {
            allLengths = Boolean.TRUE;
        }
        else{
            allLengths = Boolean.FALSE;
        }
    }
}
