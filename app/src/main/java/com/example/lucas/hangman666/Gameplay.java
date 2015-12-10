package com.example.lucas.hangman666;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.text.InputType;
import android.view.Gravity;
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
    protected int livesTries, livesLeft;

    // initiate variables
    public int wordLength;
    protected static TextView wordContainer, showLives;
    protected int limbs;
    protected String addedName, guessString;
    protected Boolean evilGame;
    protected Boolean godMode = Boolean.FALSE;
    protected Boolean hiScore;
    protected Random bla;
    // fix way to find textfile
    protected EditText guess;
    protected String currentEvilGuess;
    private ImageView body, head, rArm, lArm, rLeg, lLeg;
    protected static String[] words = {"dog", "at", "wood", "earth", "master", "hanging", "computer", "deer", "beer" };

    protected String[] fourWords = {"bear", "boar", "brat", "hole", "mole", "duck", "vole"};
//    protected String[] fourWords = new String[7];
    protected String word;
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

        //words = getResources().getStringArray(R.array.wordFile);

//        wordContainer = (TextView) findViewById(R.id.wordContainer);

        // compare settings todo
        checkGamePlay();

        guess = (EditText) findViewById(R.id.guessText);
        body = (ImageView) findViewById(R.id.body);
        head = (ImageView) findViewById(R.id.head);
        rArm = (ImageView) findViewById(R.id.rArm);
        lArm = (ImageView) findViewById(R.id.lArm);
        rLeg = (ImageView) findViewById(R.id.rLeg);
        lLeg = (ImageView) findViewById(R.id.lLeg);
        showLives = (TextView) findViewById(R.id.lives);

//        fourWords = getResources().getStringArray(R.array.words_small);

        // todo build in a method to check if file already exists, if not create
        createWordArrays();
//        test();

        usedWords = new ArrayList<>();
        usedWords.add("bear");
        usedWords.add("boar");
        usedWords.add("brat");
        usedWords.add("hole");
        usedWords.add("mole");
        usedWords.add("duck");
        usedWords.add("vole");

        // todo update with right words
        bla = new Random();

        currentEvilGuess = fourWords[bla.nextInt(fourWords.length)];

        guesses = new ArrayList<>();
        limbs = 0;
        godMode = Boolean.FALSE;
        hiScore = Boolean.FALSE;

        livesLeft = livesTries;

//        showLives.setText(String.valueOf(livesLeft));

        // compare settings
//        checkGamePlay();
//        checkWordSize();

    }

    private void test(){

        usedWords = new ArrayList<>();
        usedWords.add("bear");
        usedWords.add("boar");
        usedWords.add("brat");
        usedWords.add("hole");
        usedWords.add("mole");
        usedWords.add("duck");
        usedWords.add("vole");
    }

    // todo make this work
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
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
        usedWords = new ArrayList<>();


        String[] totalWords = getResources().getStringArray(R.array.words_large);
        fillArrays(totalWords);
        totalWords = getResources().getStringArray(R.array.words_large2);
        fillArrays(totalWords);
        totalWords = getResources().getStringArray(R.array.words_large3);
        fillArrays(totalWords);
        totalWords = getResources().getStringArray(R.array.words_large4);
        fillArrays(totalWords);

        String data = "";
        data += "1: " + String.valueOf(sizeOne.size());
        data += " 2: " + String.valueOf(sizeTwo.size());
        data += " 3: " + String.valueOf(sizeThree.size());
        data += " 4: " + String.valueOf(sizeFour.size());
        data += " 5: " + String.valueOf(sizeFive.size());
        data += " 6: " + String.valueOf(sizeSix.size());
        data += " 7 " + String.valueOf(sizeSeven.size());
        data += " 8 " + String.valueOf(sizeEight.size());
        data += " 9 " + String.valueOf(sizeNine.size());
        data += " 10 " + String.valueOf(sizeTen.size());
        data += " 11 " + String.valueOf(sizeEleven.size());
        data += " 12 " + String.valueOf(sizeTwelve.size());
        data += " 14 " + String.valueOf(sizeThirteen.size());
        data += " 15 " + String.valueOf(sizeFourteen.size());
        data += " 16 " + String.valueOf(sizeFifteen.size());
        data += " 17 " + String.valueOf(sizeSixteen.size());
        data += " 18 " + String.valueOf(sizeSeventeen.size());
        data += " 19 " + String.valueOf(sizeNineteen.size());
        data += " 20 " + String.valueOf(sizeTwenty.size());
        showLives.setText(data);




    }

    private void fillArrays(String[] totalWords){

        for (int i = 0; i < totalWords.length; i++) {
            if (totalWords[i].length() == 3) {
                sizeThree.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 2){
                sizeTwo.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 1){
                sizeOne.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 4){
                sizeFour.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 5){
                sizeFive.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 6){
                sizeSix.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 7){
                sizeSeven.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 8){
                sizeEight.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 9){
                sizeNine.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 10){
                sizeTen.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 11){
                sizeEleven.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 12){
                sizeTwelve.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 13){
                sizeThirteen.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 14){
                sizeFourteen.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 15){
                sizeFifteen.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 16){
                sizeSixteen.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 17){
                sizeSeventeen.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 18){
                sizeEightteen.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 19){
                sizeNineteen.add(totalWords[i]);
            }
            else if (totalWords[i].length() == 20){
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


        //get data from editText and empty
        guessString = guess.getText().toString();
        guess.setText("");
        //todo implement enter with done?

        if (evilGame) {

            // todo make toUpper
            char c = Character.toLowerCase(guessString.charAt(0));

            if ((guessString.length() == 1) && Character.isLetter(c)){

                if (!guesses.contains(c)){
                    guesses.add(c);
                    if (!game2.evilClick(c)){
                        addLimbs();
                    }
                    String tempTest = "";
                    for (int i = 0; i < usedWords.size(); i++){
                        tempTest  += usedWords.get(i) + " ";
                    }
                    tempTest += String.valueOf(usedWords.size());
                    showLives.setText(tempTest);
//                    currentEvilGuess = usedWords.get(bla.nextInt(usedWords.size()));

                    // see if underscores present and prompt win message
                    String winCon = wordContainer.getText().toString();
                    String underscore = "_";
                    if (!winCon.contains(underscore)){
                        // determines time at end of game
                        long endTime = System.currentTimeMillis();

                        // creates record for played game, corrected for length of guessed word
                        record = ((endTime - startTime) / wordLength) * (2 - ((livesLeft) / livesTries));
                        promptName(record);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Already guessed this!", Toast.LENGTH_SHORT).show();
                }


            }
            else{
                Toast.makeText(getApplicationContext(), "Invalid input, try again!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            goodClick(guessString);
        }

    }

    // compares settings
    public void checkGamePlay(){


        readSettings();

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
    }

    // click event good game mode
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
            // todo make toUpper
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
        if(todoFile != null) {
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
