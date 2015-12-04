package com.example.lucas.hangman666;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Lucas on 24-11-2015.
 */
public class Gameplay extends Activity{



    static Random r;
    static int wordIndex;
    static int lettersCorrect;
    protected long startTime;

    EvilGamePlay game2;
    GoodGamePlay game;

    protected long record;

    // initiate variables
    protected int wordLength;
    protected static TextView wordContainer;
    protected int limbs;
    protected String addedName, guessString;
    protected Boolean evilGame;
    // fix way to find textfile
    protected EditText guess;
    private ImageView body, head, rArm, lArm, rLeg, lLeg;
    final static String[] words = {"dog", "at", "wood", "earth", "master", "hanging", "computer", "deer", "beer" };

    final String[] fourWords = {"dogs", "cats", "boar", "hare", "deer", "duck", "limp", "bear"};
    protected String word;
    protected static List<Character> guesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set layout
        setContentView(R.layout.game_play_layout);

        //words = getResources().getStringArray(R.array.wordFile);

//        wordContainer = (TextView) findViewById(R.id.wordContainer);


        guess = (EditText) findViewById(R.id.guessText);
        body = (ImageView) findViewById(R.id.body);
        head = (ImageView) findViewById(R.id.head);
        rArm = (ImageView) findViewById(R.id.rArm);
        lArm = (ImageView) findViewById(R.id.lArm);
        rLeg = (ImageView) findViewById(R.id.rLeg);
        lLeg = (ImageView) findViewById(R.id.lLeg);

        guesses = new ArrayList<>();
        limbs = 0;


        // compare settings
        checkGamePlay();

    }

    // todo make this work
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }
    // add onclick event
    public void submitButton(View view) {


        //get data from editText and empty
        guessString = guess.getText().toString();
        guess.setText("");
        //todo implement enter with done?

        if (evilGame) {
            if (guessString.length() == 1){

                game2.evilClick(fourWords, guessString.charAt(0));
            }
            else{
                Toast.makeText(getApplicationContext(), "Invalid input, try again!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            goodClick(guessString);
        }

    }


    public void checkGamePlay(){

        // todo use readSettings
        readSettings();

//        evilGame = false;

        wordContainer = (TextView) findViewById(R.id.wordContainer);
        if (evilGame == Boolean.TRUE) {

            game2 = new EvilGamePlay();
            game2.createEvil();
            game2.createUnderscores();
        }
        else{
            game = new GoodGamePlay();

            initGame();
        }
    }

    public void initGame(){

        // determine range of word choice
        int range = words.length; //textfilesize;

        // generates pseudo-random integer for range
        r = new Random();
        wordIndex = r.nextInt(range);

        // chooses word and determines length
        word = words[wordIndex];
        wordLength = word.length();

        // assign view for underscores and create them
        createUnderscores();

        // clears possible previous guesses and # correct
        guesses.clear();
        lettersCorrect = 0;

        // define start time and assign entry method
        guess = (EditText) findViewById(R.id.guessText);
        startTime = System.currentTimeMillis();
    }

    protected void goodClick(String str){
        // guess set to incorrect
        Boolean correct = Boolean.FALSE;

        // checks length of input
        if (str.length() == wordLength){

            // if input equals word --> win
            if (word.equals(str)){
                Toast.makeText(getApplicationContext(), "You win!", Toast.LENGTH_LONG).show();

                // fill in blanks
                // todo fill in for underscores, keep blanks
                wordContainer.setText(str);

                // determines time at end of game
                long endTime = System.currentTimeMillis();

                // creates record for played game, corrected for length of guessed word
                record = ((endTime - startTime) / wordLength) * (2 - ((6 - limbs) / 6));


                // start sending data to high score
                promptName(record);

            }

            else{
                // if wrong tell and update pic
                Toast.makeText(getApplicationContext(), "Oh no!", Toast.LENGTH_SHORT).show();
                addLimbs();
            }
        }

        // if 1 character
        else if (str.length() == 1){

            // create lower letter variable
            char letter = Character.toLowerCase(str.charAt(0));

            // checks if entry is a letter
            if (Character.isLetter(letter) == Boolean.TRUE){

                if (!Gameplay.guesses.contains(letter)) {

                    // todo add size of guesses to screen
                    // adds letter to archive
                    guesses.add(letter);
                    // loops over word and update underscores
                    for (int i = 0; i < wordLength; i++){

                        // update underscore and set correctness
                        if (word.charAt(i) == letter){
                            game.updateUnderscores(letter, i);
                            correct = Boolean.TRUE;
                        }
                    }
                    // if false add limbs and yell
                    if (correct == Boolean.FALSE){

                        // draw limbs and yell
                        addLimbs();
                        Toast.makeText(getApplicationContext(), "Guess again!", Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Already used this!", Toast.LENGTH_SHORT).show();
                }


            }

        }
        else{
            // yell for better input
            Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }



    protected void youLost(){
        AlertDialog.Builder uLose = new AlertDialog.Builder(this);
        uLose.setTitle("YOU LOSE");
        uLose.setMessage("To bad, the correct answer was: " + word);
        uLose.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // start new game
                //TODO build method to compare settings and start other game
                Intent newGame = new Intent(getApplicationContext(), GoodGamePlay.class);
                startActivity(newGame);
            }
        });

        uLose.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // exit game
                // todo fix exit
                finish();
                System.exit(1);
            }
        });

        AlertDialog commitLoss = uLose.create();
        commitLoss.show();
    }

    protected void promptName(final long time){
        // pop up asking for name
        AlertDialog.Builder askName = new AlertDialog.Builder(this);
        askName.setTitle("Name");
        askName.setMessage("Congratulations! You've won!\n" +
                "Please enter your name to commit your high score.");

        // name input box
        final EditText name = new EditText(this);
        name.setHint("Name");
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        askName.setView(name);

        // create submission button
        askName.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // only continues with valid input
                addedName = name.getText().toString();
                if (name.length() != 0) {

                    // launches and adds high score
                    Intent goToHS = new Intent(getApplicationContext(), HighScore.class);
                    goToHS.putExtra("addedName", addedName);
                    goToHS.putExtra("time", time);

                    // sets name to nothing, fixes bug
                    name.setText("");

                    startActivity(goToHS);
                }

            }
        });

        AlertDialog commitName = askName.create();
        commitName.show();
    }


    protected void createUnderscores(){

        // initialise string and place underscores
        String underscores = "";
        for (int i = 0; i < wordLength - 1; i++) {
            underscores = underscores + "_ ";
        }
        // set last underscore without space at end
        underscores = underscores + "_";

        // set the underscores
        wordContainer.setText(underscores);
    }


    protected void addLimbs(){
        limbs++;
        if (limbs == 1){
            head.setVisibility(View.VISIBLE);
        }
        else if (limbs == 2){
            body.setVisibility(View.VISIBLE);
        }
        else if (limbs == 3){
            rArm.setVisibility(View.VISIBLE);
        }
        else if (limbs == 4){
            lArm.setVisibility(View.VISIBLE);
        }
        else if (limbs == 5){
            rLeg.setVisibility(View.VISIBLE);
        }
        // todo make this == length from settings
        else if (limbs == 6){
            lLeg.setVisibility(View.VISIBLE);
            Toast.makeText(this, "LOSER", Toast.LENGTH_SHORT).show();
            //TODO goto Ulose
            youLost();
        }
    }

    protected void readSettings() {
        File file = getFilesDir();
        File todoFile = new File(file, "settings.txt");
        try {
            String mode = (FileUtils.readFileToString(todoFile));
            if (mode.equals("good")){
                evilGame = Boolean.FALSE;
            }
            else if (mode.equals("evil")){
                evilGame = Boolean.TRUE;
            }
        } catch (IOException e) {
            evilGame = Boolean.TRUE;
        }
    }

    protected void writeSettings(String mode) {
        File file = getFilesDir();
        File todoFile = new File(file, "settings.txt");
        try {
            FileUtils.writeStringToFile(todoFile, mode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
