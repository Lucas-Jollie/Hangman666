package com.example.lucas.hangman666;


import android.widget.Toast;

import java.util.Random;

/**
 * Class handling some of the good game play functions
 */

public class GoodGamePlay extends Gameplay{

    Random r;

    final void createGuess(){
        r = new Random();

        word = usedWords.get(r.nextInt(usedWords.size()));
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


