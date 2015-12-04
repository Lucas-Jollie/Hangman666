package com.example.lucas.hangman666;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Lucas on 30-11-2015.
 */
public class Settings extends Gameplay{

    // initialise switch
    private Switch mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        // load switch
        mode = (Switch) findViewById(R.id.toggleEvil);

        readSettings();
    }


    // check if switch on
    public void toggleMode(View view) {
        // if on make evil
        if (mode.isChecked()){
            evilGame = Boolean.TRUE;
            // write settings to file for future
            writeSettings("evil");
        }
        // else good
        else{
            evilGame = Boolean.FALSE;
            // write settings to file
            writeSettings("good");
        }
    }


}
