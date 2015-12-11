package com.example.lucas.hangman666;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Evil game play
 * Game determines the largest group of words, either containing the guess at [index], or not
 * containing the guess at all.
 * Multiple matches are discarded, as it gives the player too much information about possible
 * guesses.
 */
public class EvilGamePlay extends  Gameplay{


    private int targetLength;


    protected void createUnderscores(int target){
        targetLength = target;
        String underscores = "";
        for (int i = 0; i< target - 1; i++) {
            underscores = underscores + "_ ";
        }

        // set last underscore without space at end
        underscores = underscores + "_";

        // set the underscores
        Gameplay.wordContainer.setText(underscores);

    }



    protected Boolean evilClick(Character c) {

        if (usedWords.size() != 1){

            // stores words in temporary array
            int x = targetLength + 1;

            // create array for indices
            int[] array = new int[x];

            // create list for words
            List<String> values;

            values = new ArrayList<>();

            // array for non containing words
            List<String> noContain = new ArrayList<>();


            // loops over array of words
            for (int i = 0; i < usedWords.size(); i++){

                // index and amount of times letter present in word
                int numbInWord = 0;
                int index = 0;

                // nothing added yet
                Boolean added = Boolean.FALSE;

                // loop over current word
                for (int j = 0; j < usedWords.get(i).length(); j++) {

                    // if found guess
                    if (usedWords.get(i).charAt(j) == c) {

                        // update index of match
                        index = j;

                        // update number of matches
                        numbInWord++;
                    }

                }

                // for single matches
                if (numbInWord == 1) {

                    // update value of words with letter at index
                    array[index + 1]++;

                    added = Boolean.TRUE;
                }

                // if number not present update not containing list
                if (!added && numbInWord == 0) {

                    array[0]++;

                    noContain.add(usedWords.get(i));
                }


            }

            // clear values and add not containing words to hash map
            values.clear();

            // determines index of largest equivalence class
            int maximumIndex = 0;
            for (int i = 1; i < array.length; i++) {

                // compares values and sets maximum index correspondingly
                if (array[i] > array[maximumIndex]) {
                    maximumIndex = i;
                }

            }

            // if words without guess larger
            if (maximumIndex == 0) {

                usedWords.clear();
                usedWords.addAll(noContain);
                return false;
            }

            else {

                // update with guess
                updateUnderscores(c, maximumIndex - 1);

                // updates words to words from largest equivalence class
                for (int i = 0; i < usedWords.size(); i++){
                    if (usedWords.get(i).charAt(maximumIndex - 1) == c){
                        values.add(usedWords.get(i));
                    }
                }
                usedWords.clear();
                usedWords.addAll(values);
                return true;
            }

        }

        // if one word in array, resume regular game play
        else{

            // updates evilGuess
            currentEvilGuess = usedWords.get(0);
            Boolean correct = Boolean.FALSE;

            // loops over word for matches and updates
            for (int i =0; i < currentEvilGuess.length(); i++){
                if (currentEvilGuess.charAt(i) == c){
                    updateUnderscores(c, i);
                    correct = Boolean.TRUE;
                }
            }

            // returns value
            return correct;

        }
    }

    final void updateUnderscores(char letter, int index){

        // turn underscores into string and character array
        String underscores = wordContainer.getText().toString();
        char[] charsArray = underscores.toCharArray();

        // correct index for spaces and set letter
        index = index * 2;
        charsArray[index] = letter;

        // turn into string and update
        underscores = String.valueOf(charsArray);

        wordContainer.setText(underscores);

        // update correct counter to see if won yet
        lettersCorrect++;

        if (lettersCorrect == wordLength){
            Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show();

            // determines time at end of game
            long endTime = System.currentTimeMillis();

            // creates record for played game, corrected for length of guessed word
            record = ((endTime - startTime) / wordLength) * (2 - ((6 - limbs) / 6));

            // ask name input
            promptName(record);
        }


    }
}