package com.example.lucas.hangman666;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * Created by Lucas on 27-11-2015.
 */
public class EvilGamePlay extends  Gameplay{


    private int targetLength;
    static Random r;
    static int maxLength;

    protected void createEvil() {
        r = new Random();
        maxLength = 4;
        targetLength = r.nextInt(maxLength);

        // todo delete
        targetLength = maxLength;
        wordLength = targetLength;
    }

    protected void createUnderscores(){
        String underscores = "";
        for (int i = 0; i< targetLength - 1; i++) {
            underscores = underscores + "_ ";
        }

        // set last underscore without space at end
        underscores = underscores + "_";

        // set the underscores
        Gameplay.wordContainer.setText(underscores);

    }



    protected void evilClick(Character c) {

        // double loop over words and chars in said words
        // use array, contains at[i]
        // if contains at i, use tempCounter to increase value
        // after looping through list, add tempCounter to array
        // reset tempCounter after adding
        // continue to loop for [i+1] and add tempCounter
        // continue these loops for length of word
        // add values of arrays together and subtract from total size of word array
        // append said value to end array
        // check array for highest value
        // take index of highest value
        // if index == last --> dont reveal letter and loop over words and add words without char to new list/array
        // if index != last, reveal char at index and loop over words with char at[index] add to array

        // loop over words and lists CHECK
        // if not contains,array[0]++ CHECK
        // add to noCont CHECK
        // if contain, array[i]++ CHECK
        // loop over rest of word CHECK
        // if single contain CHECK
        // add words to string array[i] CHECK
        // else delete CHECK (as far as possible)
        // compare array[i] CHECK
        // the largest array[i]--> take word list from string array[i] CHECK
        // if largest == array[0]--> no reveal
        // else --> reveal char c at underscore[i]


        String[] words = fourWords;
        int x = targetLength;
        int y = words.length;
        int largestGroup = 0;
        Integer[] array = new Integer[x];
        HashMap<Integer, List<String>> options = new HashMap<>();
        List<String> values = new ArrayList<>();
        // todo compare with other arrays
        List<String> noContain = new ArrayList<>();
        int numbInWord = 0;
        int index = 0;

        for (int i = 0; i < words.length; i++) {
            Boolean added = Boolean.FALSE;
            for (int j = 0; j < words[i].length(); j++) {

                if (words[i].charAt(j) == c) {
                    index = j;
                    numbInWord++;

                }
                if (numbInWord == 1) {
                    array[index + 1]++;
                    values = options.get(index + 1);
                    values.add(words[i]);
                    options.put(index + 1, values);
                    added = true;
                }
            }
            if (!added) {
                array[0]++;
                noContain.add(words[i]);
            }

        }

        int maximumIndex = 0;
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                maximumIndex = i;
            } else if (array[i] < array[i + 1]) {
                maximumIndex = i + 1;
            } else {
                Random R = new Random();
                if (r.nextInt(2) == 2) {
                    maximumIndex = i + 1;
                } else {
                    maximumIndex = i;
                }
            }
        }

        // todo check if reveal, if not, addLimbs(), else updateUnderscores()
        if (maximumIndex == 0){
            addLimbs();
        }
        else{
            updateUnderscores(c, maximumIndex);
        }

        values = options.get(maximumIndex);
        words = values.toArray(new String[values.size()]);
        fourWords = words;
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