package com.example.lucas.hangman666;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
public class Gameplay extends AppCompatActivity{


    static Random r;
    static int wordIndex, lettersCorrect;
    protected long startTime, record;

    EvilGamePlay game2;
    GoodGamePlay game;

    protected int livesTries, livesLeft, wordLength, limbs;

    // initiate variables
    protected static TextView wordContainer, showLives;
    protected String addedName, guessString, word;
    protected Boolean evilGame, hiScore;
    protected Boolean godMode = Boolean.FALSE;
    protected Boolean allLengths = Boolean.TRUE;

    protected Random bla;
    // fix way to find textfile
    protected EditText guess;
    protected String currentEvilGuess;
    private ImageView body, head, rArm, lArm, rLeg, lLeg;

    protected static List<Character> guesses;
    protected List<String> sizeOne, sizeTwo, sizeThree, sizeFour, sizeFive, sizeSix, sizeSeven, sizeEight, sizeNine,
            sizeTen, sizeEleven, sizeTwelve, sizeThirteen, sizeFourteen, sizeFifteen, sizeSixteen,
            sizeSeventeen, sizeEightteen, sizeNineteen, sizeTwenty;
    protected static List<String> usedWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set layout
        setContentView(R.layout.game_play_layout);

        // compare settings
        readSettings();

        // definitions
        guess = (EditText) findViewById(R.id.guessText);
        body = (ImageView) findViewById(R.id.body);
        head = (ImageView) findViewById(R.id.head);
        rArm = (ImageView) findViewById(R.id.rArm);
        lArm = (ImageView) findViewById(R.id.lArm);
        rLeg = (ImageView) findViewById(R.id.rLeg);
        lLeg = (ImageView) findViewById(R.id.lLeg);
        showLives = (TextView) findViewById(R.id.lives);



        usedWords = new ArrayList<>();

        // reads file, on first opening app should return false
        if (!retrieveSavedValues()){


            wordLength = 4;
            evilGame = Boolean.TRUE;
            livesLeft = 6;
            livesTries = livesLeft;

            // divide words into arrays
            createWordArrays();

            // compare given word size
            checkWordSize();

            // generate random guess for evil game play
            bla = new Random();
            Log.i("Gameplay", "create random" + usedWords.size());
            currentEvilGuess = usedWords.get(bla.nextInt(usedWords.size()));

            limbs = 0;
        }
        else{
            livesLeft = livesTries;
        }
        // check which mode is active
        checkGamePlay();

        guesses = new ArrayList<>();

        // cheat codes always reset
        godMode = Boolean.FALSE;
        hiScore = Boolean.FALSE;

        // displays the amount of lives a player has left
        showLives.setText(String.valueOf(livesLeft));


    }


    // add menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // launches settings activity
                Intent goSettings = new Intent(this, Settings.class);
                startActivity(goSettings);
                break;
            case R.id.restart:

                // reset underscores
                createUnderscores();

                // reset lives
                livesLeft = livesTries;

                // reset limbs
                limbs = 0;

                // reset already guessed
                guesses.clear();

                // reset word / words
                if (evilGame){
                    usedWords.clear();
                    checkWordSize();
                }
                else{
                    game.createGuess();
                }
                // reset images
                resetLimbs(limbs);

                // empty guess box
                guess.setText("");

                // save to file
                saveCurrentValues();

                // restart activity
                // todo think if restart activity or just reset everything

            default:
                break;
        }
        return true;
    }


    public void resetLimbs(int data){
        /**********************************
         * Resets the limb ImageViews upon
         * restart of the game or when
         * starting a new game, depending
         * on the state of the game or
         * what action was performed.
         **********************************/
        if (data == 0){
            head.setVisibility(View.INVISIBLE);
            body.setVisibility(View.INVISIBLE);
            rArm.setVisibility(View.INVISIBLE);
            lArm.setVisibility(View.INVISIBLE);
            rLeg.setVisibility(View.INVISIBLE);
            lLeg.setVisibility(View.INVISIBLE);
        }
        else if (data == 1){
            head.setVisibility(View.VISIBLE);
            body.setVisibility(View.INVISIBLE);
            rArm.setVisibility(View.INVISIBLE);
            lArm.setVisibility(View.INVISIBLE);
            rLeg.setVisibility(View.INVISIBLE);
            lLeg.setVisibility(View.INVISIBLE);
        }
        else if (data == 2){
            body.setVisibility(View.VISIBLE);
            rArm.setVisibility(View.INVISIBLE);
            lArm.setVisibility(View.INVISIBLE);
            rLeg.setVisibility(View.INVISIBLE);
            lLeg.setVisibility(View.INVISIBLE);
        }
        else if (data == 3){
            rArm.setVisibility(View.VISIBLE);
            lArm.setVisibility(View.INVISIBLE);
            rLeg.setVisibility(View.INVISIBLE);
            lLeg.setVisibility(View.INVISIBLE);
        }
        else if (data == 4){
            lArm.setVisibility(View.VISIBLE);
            rLeg.setVisibility(View.INVISIBLE);
            lLeg.setVisibility(View.INVISIBLE);
        }
        else if (data == 5){
            rLeg.setVisibility(View.VISIBLE);
            lLeg.setVisibility(View.INVISIBLE);
        }
        else if (livesLeft == 0){
            lLeg.setVisibility(View.VISIBLE);
        }
    }


    // create arrays of words upon start
    private void createWordArrays(){

        sizeOne = new ArrayList<>();
        sizeTwo = new ArrayList<>();
        sizeThree = new ArrayList<>();
        sizeFour = new ArrayList<>();
        sizeFive = new ArrayList<>();
        sizeSix = new ArrayList<>();
        sizeSeven = new ArrayList<>();
        sizeEight = new ArrayList<>();
        sizeNine = new ArrayList<>();
        sizeTen = new ArrayList<>();
        sizeEleven = new ArrayList<>();
        sizeTwelve = new ArrayList<>();
        sizeThirteen = new ArrayList<>();
        sizeFourteen = new ArrayList<>();
        sizeFifteen = new ArrayList<>();
        sizeSixteen = new ArrayList<>();
        sizeSeventeen = new ArrayList<>();
        sizeEightteen = new ArrayList<>();
        sizeNineteen = new ArrayList<>();
        sizeTwenty = new ArrayList<>();

        // clear words just in case
        usedWords.clear();

        String[] totalWords = getResources().getStringArray(R.array.words_large);
        if (!allLengths && evilGame) {
            fillArrays(totalWords);
        }
        else if (allLengths && !evilGame){
            allSizes(totalWords);
        }
        totalWords = getResources().getStringArray(R.array.words_large2);
        if (!allLengths && evilGame) {
            fillArrays(totalWords);
        }
        else if (allLengths && !evilGame){
            allSizes(totalWords);
        }
        totalWords = getResources().getStringArray(R.array.words_large3);
        if (!allLengths && evilGame) {
            fillArrays(totalWords);
        }
        else if (allLengths && !evilGame){
            allSizes(totalWords);
        }
        totalWords = getResources().getStringArray(R.array.words_large4);
        if (!allLengths && evilGame) {
            fillArrays(totalWords);
        }
        else if (allLengths && !evilGame){
            allSizes(totalWords);
        }
    }

    private void allSizes(String[] totalWords){
        for (int i = 0; i < totalWords.length; i++) {
            usedWords.add(totalWords[i]);
        }
    }

    private void fillArrays(String[] totalWords){

            for (int i = 0; i < totalWords.length; i++) {
                if (totalWords[i].length() == 3) {
                    sizeThree.add(totalWords[i]);
                } else if (totalWords[i].length() == 2) {
                    sizeTwo.add(totalWords[i]);
                } else if (totalWords[i].length() == 1) {
                    sizeOne.add(totalWords[i]);
                } else if (totalWords[i].length() == 4) {
                    sizeFour.add(totalWords[i]);
                } else if (totalWords[i].length() == 5) {
                    sizeFive.add(totalWords[i]);
                } else if (totalWords[i].length() == 6) {
                    sizeSix.add(totalWords[i]);
                } else if (totalWords[i].length() == 7) {
                    sizeSeven.add(totalWords[i]);
                } else if (totalWords[i].length() == 8) {
                    sizeEight.add(totalWords[i]);
                } else if (totalWords[i].length() == 9) {
                    sizeNine.add(totalWords[i]);
                } else if (totalWords[i].length() == 10) {
                    sizeTen.add(totalWords[i]);
                } else if (totalWords[i].length() == 11) {
                    sizeEleven.add(totalWords[i]);
                } else if (totalWords[i].length() == 12) {
                    sizeTwelve.add(totalWords[i]);
                } else if (totalWords[i].length() == 13) {
                    sizeThirteen.add(totalWords[i]);
                } else if (totalWords[i].length() == 14) {
                    sizeFourteen.add(totalWords[i]);
                } else if (totalWords[i].length() == 15) {
                    sizeFifteen.add(totalWords[i]);
                } else if (totalWords[i].length() == 16) {
                    sizeSixteen.add(totalWords[i]);
                } else if (totalWords[i].length() == 17) {
                    sizeSeventeen.add(totalWords[i]);
                } else if (totalWords[i].length() == 18) {
                    sizeEightteen.add(totalWords[i]);
                } else if (totalWords[i].length() == 19) {
                    sizeNineteen.add(totalWords[i]);
                } else if (totalWords[i].length() == 20) {
                    sizeTwenty.add(totalWords[i]);
                }
            }

    }

    // checks word length and updates used words accordingly
    private void checkWordSize(){

        if (wordLength == 3){
            usedWords.addAll(sizeThree);
        }
        else if (wordLength == 4){
            usedWords.addAll(sizeFour);
        }

        else if (wordLength == 5){
            usedWords.addAll(sizeFive);
        }

        else if (wordLength == 6){
            usedWords.addAll(sizeSix);
        }

        else if (wordLength == 7){
            usedWords.addAll(sizeSeven);
        }

        else if (wordLength == 8){
            usedWords.addAll(sizeEight);
        }

        else if (wordLength == 9){
            usedWords.addAll(sizeNine);
        }

        else if (wordLength == 10){
            usedWords.addAll(sizeTen);
        }
        else if (wordLength == 11){
            usedWords.addAll(sizeEleven);
        }
        else if (wordLength == 12){
            usedWords.addAll(sizeTwelve);
        }
        else if (wordLength == 13){
            usedWords.addAll(sizeThirteen);
        }
        else if (wordLength == 14){
            usedWords.addAll(sizeFourteen);
        }
        else if (wordLength == 15){
            usedWords.addAll(sizeFifteen);
        }
        else if (wordLength == 16){
            usedWords.addAll(sizeSix);
        }
        else if (wordLength == 17){
            usedWords.addAll(sizeSeventeen);
        }
        else if (wordLength == 18){
            usedWords.addAll(sizeEightteen);
        }
        else if (wordLength == 19){
            usedWords.addAll(sizeNineteen);
        }
        else if (wordLength == 20){
            usedWords.addAll(sizeTwenty);
        }
    }

    // add onclick event
    public void submitButton(View view) {
        /**********************************
         * Handles the event of the player
         * clicking submit after entering
         * a guess in the form of either
         * an entire word or just a single
         * character.
         **********************************/


        //get data from editText and empty
        guessString = guess.getText().toString();
        guess.setText("");

        if (guessString.length() >0) {
            if (evilGame) {

                char c = Character.toUpperCase(guessString.charAt(0));

                if (Character.isLetter(c)) {

                    if (!guesses.contains(c)) {
                        guesses.add(c);
                        if (!game2.evilClick(c)) {
                            addLimbs();
                        }
                        currentEvilGuess = usedWords.get(bla.nextInt(usedWords.size()));

                        // see if underscores present and prompt win message
                        String winCon = wordContainer.getText().toString();
                        String underscore = "_";
                        if (!winCon.contains(underscore)) {
                            // determines time at end of game
                            long endTime = System.currentTimeMillis();

                            // creates record for played game, corrected for length of guessed word
                            record = ((endTime - startTime) / wordLength) * (2 - ((livesLeft) / livesTries));
                            promptName(record);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Already guessed this!", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Invalid input, try again!", Toast.LENGTH_SHORT).show();
                }
            } else {
                goodClick(guessString);
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Invalid input, try again!", Toast.LENGTH_SHORT).show();
        }

        saveCurrentValues();

    }

    // saves state of game
    public void saveCurrentValues(){
        /*******************************
         * Writes data to file to
         * preserve progress in game
         *******************************/

        // initialising strings to modify or write
        String saveWordContainer = wordContainer.getText().toString();
        String saveCurrentLives = String.valueOf(livesLeft);
        String saveCurrentLimbs = String.valueOf(limbs);
        String saveCurrentWords = "";
        String saveCurrentGuess = "";
        String saveStartTime = String.valueOf(startTime);

        // makes writable string of words
        if (evilGame) {
            // creates a string of words left in the list
            int last = usedWords.size()- 1;
            for (int i = 0; i < last; i++){
                saveCurrentWords += usedWords.get(i) + ",";
            }
            saveCurrentWords += usedWords.get(last);
        }
        else{
            saveCurrentWords = word;
        }

        // concatenates variables into writable string
        String written = saveWordContainer + "," + saveCurrentLives + "," + saveCurrentLimbs + ","
                + currentEvilGuess + saveStartTime;

        // creates string of guesses already made
        for (int i = 0; i < guesses.size() - 1; i++){
            saveCurrentGuess += String.valueOf(guesses.get(i)) + ",";
        }

        saveCurrentGuess += String.valueOf(guesses.get(guesses.size() - 1));

        // saves guesses to file
        File madeGuess= getFilesDir();
        File writeGuess = new File(madeGuess, "guesses.txt");
        try{
            FileUtils.writeStringToFile(writeGuess, saveCurrentGuess);
        }catch (IOException e){
            e.printStackTrace();
        }

        // saves variables to file
        File vars = getFilesDir();
        File writeVars = new File(vars, "vars.txt");
        try{
            FileUtils.writeStringToFile(writeVars, written);
        } catch (IOException e){
            e.printStackTrace();
        }

        // saves word list to file
        File vars2 = getFilesDir();
        File writeWords =new File(vars2, "wordlist.txt");
        try{
            FileUtils.writeStringToFile(writeWords, saveCurrentWords);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    // retrieves state of game
    public Boolean retrieveSavedValues(){
        /*******************************
         * Retrieves the previously
         * saved data if available
         *******************************/

        // set vars
        Boolean success = Boolean.FALSE;
        File file = getFilesDir();
        File varFile = new File(file, "vars.txt");

        // gets variables from file
        if (varFile.length() != 0){
            try{
                // retrieve saved string and divide into separate strings
                String vars = FileUtils.readFileToString(varFile);
                String[] varParts = vars.split(",");

                // resets progress in terms of guesses
                wordContainer.setText(varParts[0]);

                // resets lives
                livesLeft = Integer.valueOf(varParts[1]);
                limbs = Integer.valueOf(varParts[2]);
                // resets images
                resetLimbs(limbs);

                // resets current guess
                currentEvilGuess = varParts[3];
                success = Boolean.TRUE;

                // adds start time
                startTime = Long.parseLong(varParts[4]);

            }catch (IOException e){
                // default settings upon failure
                evilGame = Boolean.TRUE;
                livesTries = 6;
                wordLength = 4;
            }
        }

        // set vars
        File file2 = getFilesDir();
        File wordFile = new File(file2, "wordlist.txt");

        // retrieve words from file
        if (wordFile.length() != 0){
            try{
                // loads (all) word(s) from file
                String wordsFromFile = FileUtils.readFileToString(varFile);
                // if evil split string and divide
                if (wordsFromFile.contains(",")) {
                    String[] wordParts = wordsFromFile.split(",");

                    // resets word list and adds old words
                    usedWords.clear();
                    for (int i = 0; i < wordParts.length; i++){
                        usedWords.add(wordParts[i]);
                    }
                }
                else{
                    // resets chosen word
                    word = wordsFromFile;
                }

                if(success){
                    return true;
                }


            }catch (IOException e){
                evilGame = Boolean.TRUE;
                livesTries = 6;
                wordLength = 4;
            }
        }

        // set vars
        File file3 = getFilesDir();
        File getGuesses = new File(file3, "guesses.txt");

        // retrieve guesses
        if(getGuesses.length() != 0){
            try{
                // gets all made guesses
                String totalGuess = FileUtils.readFileToString(getGuesses);
                if (totalGuess.contains(",")){
                    String[] guessArray = totalGuess.split(",");

                    // reset guesses if not empty
                    guesses.clear();
                    for (int i = 0; i < guessArray.length; i++){
                        // add made guesses
                        guesses.add(guessArray[i].charAt(0));

                    }
                }
                else{
                    guesses.add(totalGuess.charAt(0));
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return false;

    }

    // compares settings
    public void checkGamePlay(){
        /****************************
         * Decides which class to
         * access when starting a new
         * game.
         ****************************/

        // sets container for word and start time
        wordContainer = (TextView) findViewById(R.id.wordContainer);
        startTime = System.currentTimeMillis();
        if (evilGame == Boolean.TRUE) {

            game2 = new EvilGamePlay();
            game2.createUnderscores(wordLength);
        }
        else{
            game = new GoodGamePlay();

            initGame();
        }
    }

    // starts normal game play
    public void initGame(){

        // get new random word
        game.createGuess();

        // get word length
        wordLength = word.length();

        // assign view for underscores and create them
        createUnderscores();

        // clears possible previous guesses and # correct
        // guesses.clear();
        lettersCorrect = 0;

    }

    // click event good game mode
    protected void goodClick(String str){
        /****************************************
         * Handles the events when the submit
         * button is click in case of good game
         * play.
         * This was already coded this way and
         * due to lack of time has not been
         * able to be recoded into a completely
         * seperate class.
         ****************************************/
        // guess set to incorrect
        Boolean correct = Boolean.FALSE;

        // checks length of input
        if (str.length() == wordLength){

            // if input equals word --> win
            if (word.equals(str)){
                Toast.makeText(getApplicationContext(), "You win!", Toast.LENGTH_LONG).show();

                // fill in blanks
                wordContainer.setText(str);

                // determines time at end of game
                long endTime = System.currentTimeMillis();

                // creates record for played game, corrected for length of guessed word
                record = ((endTime - startTime) / wordLength) * (2 - ((livesTries - limbs) / livesTries));


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
            char letter = Character.toUpperCase(str.charAt(0));

            // checks if entry is a letter
            if (Character.isLetter(letter) == Boolean.TRUE){

                if (!Gameplay.guesses.contains(letter)) {

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

    // prompt losing message
    protected void youLost(){
        AlertDialog.Builder uLose = new AlertDialog.Builder(this);
        TextView title = new TextView(this);
        title.setText("YOU LOSE");
        title.setGravity(Gravity.CENTER);
        uLose.setCustomTitle(title);
        if (evilGame){
            word = currentEvilGuess;
        }
        uLose.setMessage("Too bad, the correct answer was: " + word);
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

    // promp winning message
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

    // set underscores
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

    // update limbs, lives and images
    protected void addLimbs(){

        if (!godMode) {
            limbs++;
            livesLeft--;
            showLives.setText(String.valueOf(livesLeft));
            if (livesLeft == 0) {
                lLeg.setVisibility(View.VISIBLE);
                Toast.makeText(this, "LOSER", Toast.LENGTH_SHORT).show();

                youLost();
            }
            else if (limbs == 1) {
                head.setVisibility(View.VISIBLE);
            } else if (limbs == 2) {
                body.setVisibility(View.VISIBLE);
            } else if (limbs == 3) {
                rArm.setVisibility(View.VISIBLE);
            } else if (limbs == 4) {
                lArm.setVisibility(View.VISIBLE);
            } else if (limbs == 5) {
                rLeg.setVisibility(View.VISIBLE);
            }
        }

    }

    // read settings from file
    protected void readSettings() {
        File file = getFilesDir();
        File todoFile = new File(file, "settings.txt");
        if(todoFile.length() != 0) {
            try {
                String info = (FileUtils.readFileToString(todoFile));
                String[] parts = info.split(",");
                String mode = parts[0];
                String length = parts[1];
                String lives = parts[2];
                if (mode.equals("good")) {
                    evilGame = Boolean.FALSE;
                } else if (mode.equals("evil")) {
                    evilGame = Boolean.TRUE;
                }
                livesTries = Integer.parseInt(lives);
                wordLength = Integer.parseInt(length);
            } catch (IOException e) {
                evilGame = Boolean.TRUE;
                livesTries = 6;
                wordLength = 4;
            }
        }
    }

    // update settings file
    protected void writeSettings(String mode, String word_length, String livesLeft) {
        String written = mode + "," + word_length + "," + livesLeft;
        File file = getFilesDir();
        File todoFile = new File(file, "settings.txt");
        try {
            FileUtils.writeStringToFile(todoFile, written);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
