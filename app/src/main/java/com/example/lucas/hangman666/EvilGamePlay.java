package com.example.lucas.hangman666;

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



    protected void evilClick(String[] words, Character c) {

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

        values = options.get(maximumIndex);
        words = values.toArray(new String[values.size()]);
    }
}