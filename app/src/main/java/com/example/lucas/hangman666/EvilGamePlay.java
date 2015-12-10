package com.example.lucas.hangman666;

import android.util.SparseArray;
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

//        if (fourWords.length != 1) {
        if (usedWords.size() != 1){

            // stores words in temporary array
//            String[] words = new String[fourWords.length];
//            System.arraycopy(fourWords, 0, words, 0, fourWords.length);
            int x = targetLength + 1;

            // create array for indices
            int[] array = new int[x];

            // create hash map and array for words
//            SparseArray<List<String>> options = new SparseArray<>();
            HashMap<Integer, List<String>> options = new HashMap<>();
            List<String> s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15,s16,s17,s18,s19,s20,
                    s21,s22,s23,s24,s25,s26, values;

            values = new ArrayList<>();
            s1 = new ArrayList<>();
            s2 = new ArrayList<>();
            s3 = new ArrayList<>();
            s4 = new ArrayList<>();
            s5 = new ArrayList<>();
            s6 = new ArrayList<>();
            s7 = new ArrayList<>();
            s8 = new ArrayList<>();
            s9 = new ArrayList<>();
            s10 = new ArrayList<>();
            s11 = new ArrayList<>();
            s12 = new ArrayList<>();
            s13 = new ArrayList<>();
            s14 = new ArrayList<>();
            s15 = new ArrayList<>();
            s16 = new ArrayList<>();
            s17 = new ArrayList<>();
            s18 = new ArrayList<>();
            s19 = new ArrayList<>();
            s20 = new ArrayList<>();

            // array for non containing words
            List<String> noContain = new ArrayList<>();


            // loops over array of words
//            for (int i = 0; i < words.length; i++) {
            for (int i = 0; i < usedWords.size(); i++){

                // index and amount of times letter present in word
                int numbInWord = 0;
                int index = 0;

                // nothing added yet
                Boolean added = Boolean.FALSE;

                // loop over current word
//                for (int j = 0; j < words[i].length(); j++) {
                for (int j = 0; j < usedWords.get(i).length(); j++) {
//                for (int j = 0; j < wordLength; j++) {

                    // if found guess, update index where at and numbs in word
//                    if (words[i].charAt(j) == c) {
                    if (usedWords.get(i).charAt(j) == c) {
//                    if (usedWords.get(i).contains(c.toString())){
                        index = j;
                        numbInWord++;

                    }

                }


                if (numbInWord == 1) {

                    // update value of words with letter at index
                    array[index + 1]++;

                    if (options.get(index + 1) != null) {
                        options.get(index + 1).add(usedWords.get(i));
                    }
                    else{
                        values.add(usedWords.get(i));
                        options.put(index + 1, values);
                        values.clear();
                    }

                    /*// reset values
                    if (values.size() > 0) {
                        values.clear();
                    }

                    // prevent null pointer error
//                    if (options.get(index + 1) != null) {
                    if ((options.get(index + 1) != null) && (options.get(index + 1).size() > 0)){

                        // add all previous values to list
                        values.addAll(options.get(index + 1));
                        // clear previous data
                        options.remove(index + 1);
                        options.get(index + 1).add(usedWords.get(i));
                    }

                    // add current word
//                    values.add(words[i]);
                    values.add(usedWords.get(i));

                    // update hash map and set added
                    options.put(index + 1, values);*/
                    added = Boolean.TRUE;
                }

                // if number not present update not containing list
                if (!added && numbInWord == 0) {

                    array[0]++;
                    // todo see if options(0, words[i]) is better

                    noContain.add(usedWords.get(i));
                }


            }

            // clear values and add not containing words to hash map
        values.clear();
        options.put(0, noContain);

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
                usedWords.addAll(options.get(maximumIndex));
//                usedWords.addAll(noContain);
                return false;
            }

            else {

                // update with guess
                updateUnderscores(c, maximumIndex - 1);
            }



            // updates words to words from largest equivalence class
            //values.addAll(options.get(maximumIndex));
            for (int i = 0; i < usedWords.size(); i++){
                if (usedWords.get(i).charAt(maximumIndex - 1) == c){
                    values.add(usedWords.get(i));
                }
            }
            usedWords.clear();
            usedWords.addAll(values);
//            usedWords.addAll(options.get(maximumIndex));
//            values.addAll(temp);
            return true;
        }

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

            // determines return value
            if (correct) {
                return true;
            }
            else{
                return false;
            }


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