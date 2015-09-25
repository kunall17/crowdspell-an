package com.igl.crowdword;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.igl.crowdword.fxns.WordSet;
import com.igl.crowdword.fxns.analysis.SetScoreCarrier;

import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameNewActivity extends ActionBarActivity {

    //TODO circle background size uniform karo
    //TODO FWD icon
    //TODO update text on FWD
    //TODO press repeadetly fwd btn gives error..!


    int noAlpha = 0;
    int wrong = 0;
    int correct = 0;
    int attempts = 0;
    TextView title_tv;
    TextView progress_tv;

    TextView[] myTextViews = new TextView[20]; // create an empty array;


    ImageView processColors;

    int currentGame = 1;
    int totalGame;
    TextView circle_txt;
    GradientDrawable circle;

    WordSet wordset = null;
    String word = null;
    List<Character> word_char = new ArrayList<>();
    List<Character> char_array = new ArrayList<Character>(Arrays.asList(new Character[]{'Z', 'Y', 'X', 'W', 'V', 'U', 'T', 'S', 'R', 'Q', 'P', 'O', 'N', 'M', 'L', 'K', 'J', 'I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A', '1', '2', '3', '4', '5', '6', '7', '8', '9'}));
    List<Character> backup_char_array;
    List<Button> threeButtons = new ArrayList<Button>();
    char currentChar;
    int words_won = 0;
    int maxChances = 5;

    public void print(String text) {
        System.out.println(text);
    }


    //TODO makeshape uniform
    void makeShapeUniform() {

    }

    void gameCompleted() {
        Intent in1 = new Intent(GameNewActivity.this, SummaryActivity.class);
        Gson gson = new Gson();

        SetScoreCarrier ssc = new SetScoreCarrier();
        ssc.setWords(wordset.getWords());
        ssc.setSetId(wordset.getId());


        String ssc_json = gson.toJson(ssc);
        in1.putExtra("ssc", ssc_json);
        Log.d("json", ssc_json);

        in1.putExtra("words_won", words_won);
        startActivity(in1);

    }


    // TODO saveScores
    void saveScores(int gameIndex) {

    }


    void checkForMaxChances(String text) {

    }

    private void win() {
        Toast.makeText(this, "Fuck You won.!", Toast.LENGTH_LONG);
        words_won++;
        if (currentGame == wordset.getWords().size()) {
            gameCompleted();
        } else {

            if (wrong > maxChances) {
                //TODO snackbar Snackbar.make(getBaseContext(),text,Snackbar.LENGTH_LONG).show();
                Toast.makeText(this, "You took more than 5 chances, you wont be awarded points for this one!", Toast.LENGTH_LONG).show();
                wordset.getWords().get(currentGame).setChancesTaken(maxChances);
            } else {
                wordset.getWords().get(currentGame).setChancesTaken(attempts);
                Toast.makeText(this, "You Completed in " + wrong + " wrong attempts!", Toast.LENGTH_LONG).show();

            }
            currentGame += 1;


            new CountDownTimer(2000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    // do something after 1s
                }

                @Override
                public void onFinish() {
                    setupGame(currentGame);
                }

            }.start();

            resetAll();
            updateStatus();


        }
    }

    public void updateStatus() {
        TextView statusx = (TextView) findViewById(R.id.statusTV);
        statusx.setText("Correct: " + correct + "|Attempt:" + attempts + "Wrong: " + wrong);
    }


    void updateColor() {

        if (wrong == 1) {
            circle.setColor(Color.argb(255, 241, 196, 15));
        } else if (wrong == 2) {
            circle.setColor(Color.argb(255, 241, 155, 15));
        } else if (wrong == 3) {
            circle.setColor(Color.argb(255, 247, 128, 15));
        } else if (wrong == 4) {
            circle.setColor(Color.argb(255, 247, 57, 15));
        } else if (wrong == 5) {
            Toast.makeText(this, "You lost this Challenge!", Toast.LENGTH_LONG).show();
            circle.setColor(Color.argb(255, 247, 49, 15));
        } else if (wrong == 6) {
            circle.setColor(Color.argb(255, 148, 11, 0));
        }
    }

    public void deleteAllTextViews() {
        if (myTextViews.length > 0) {
            for (int i = 0; i < myTextViews.length; i++) {
                myTextViews[i].destroyDrawingCache();
            }
        }
    }

    void removeTextViews() {
        print("removeTextVews");
        LinearLayout formLayout = (LinearLayout) findViewById(R.id.labelsGenerator);
        formLayout.removeAllViews();
        for (int i = 0; i < myTextViews.length; i++) {
            formLayout.removeView(myTextViews[i]);
        }
    }


    Button left;
    Button right;
    Button middle;

    public void nextButton() {
        Random rnd = new Random();
        int x = rnd.nextInt(6);
        if (x == 0) setLetters(left, middle, right);
        if (x == 1) setLetters(middle, right, left);
        if (x == 2) setLetters(right, left, middle);
        if (x == 3) setLetters(right, middle, left);
        if (x == 4) setLetters(right, left, middle);
        if (x == 5) setLetters(left, right, middle);
    }

    public void setLetters(Button one, Button two, Button three) {
        Random rnd_P = new Random();
        Random rnd_C = new Random();
        Collections.shuffle(Arrays.asList(char_array));
        Collections.shuffle(Arrays.asList(word_char));

        currentChar = word_char.get(rnd_C.nextInt(word_char.size()));
        one.setText(String.valueOf(currentChar));
        Log.d("currentChar" + attempts, String.valueOf(currentChar));
        checkIfCharArrayEmpty();
        int x = 1;
        try {
            x = rnd_P.nextInt(char_array.size());

        } catch (Exception e) {
            print("x=" + x);
            System.out.println("Error-" + e.toString());
            printCharArray();
        }

        two.setText(String.valueOf(char_array.get(x)));
        Log.d("currentRandomChar" + attempts, String.valueOf(char_array.get(x)) + "1");

        char_array.remove(x);

        checkIfCharArrayEmpty();
        x = rnd_P.nextInt(char_array.size());
        three.setText(String.valueOf(char_array.get(x)));
        Log.d("currentRandomChar" + attempts, String.valueOf(char_array.get(x)) + "2");

        char_array.remove(x);
        printCharArray();

    }

    public void finalCheck() {
        for (Character c : word_char) {
            if (char_array.contains(c)) {
                System.out.println("STILL FOUND THIS CHAR-" + String.valueOf(c));
                char_array.remove(c);
            }
        }
        char_array.removeAll(word_char);
    }

    private void checkAlpha(String theLetter) {
        int count = word.length() - word.replace(theLetter, "").length();

        if (word_char.contains(theLetter.charAt(0)))
            word_char.remove(word_char.indexOf(theLetter.charAt(0)));

        if (word.contains(theLetter) == true) { //Check for number of same letters
            int s = word.indexOf(theLetter);
            myTextViews[s].setText(theLetter);
            correct += 1;
            System.out.println("This number of times of this letter is: " + count);

            try {
                word_char.remove(word_char.indexOf(theLetter.charAt(0)));

            } catch (Exception e) {
                print("removing error-" + e.toString());
                print("Letter-" + theLetter.charAt(0));
                print("Letter Index-" + word_char.indexOf(theLetter.charAt(0)));
            }

            if (count > 1) {
                System.out.println("This is more than one time");
                for (int i = 0; i < word.length(); i++) {
                    char g = word.charAt(i);
                    if (theLetter.charAt(0) == g) {
                        System.out.println("Current letter is: " + g);
                        myTextViews[i].setText(theLetter);

                    }

                }
            }

        } else {
            wrong += 1;
            updateColor();
        }

        attempts++;
        updateStatus();
    }

    public void keyBtn_Click(View v) {
        String s = "";

        Button b = (Button) findViewById(v.getId());
        s = b.getText().toString();
        System.out.println("ASDASD");


        checkAlpha(s);
        if (word_char.size() > 0) {
            print("Superbackchodi here");
            nextButton();
        } else {
            win();
            print("Yoou have fucinkg won- keybtnclick");
        }


        printCharArray();
        updateStatus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_new);
        left = (Button) findViewById(R.id.left_btn);
        right = (Button) findViewById(R.id.right_btn);
        middle = (Button) findViewById(R.id.middle_btn);
        String text = getIntent().getStringExtra("wordName");
        String json_wordset = getIntent().getStringExtra("json_wordset");
        String json_new = getIntent().getStringExtra("json_new");
        Gson gson = new Gson();
        wordset = gson.fromJson(json_new, WordSet.class);
        getWindow().setTitle(wordset.getName());

        int noOfWords = wordset.getWords().size();

        title_tv = (TextView) findViewById(R.id.title_TV);
        progress_tv = (TextView) findViewById(R.id.progress_TV);
        circle_txt = (TextView) findViewById(R.id.circle_txt);
        circle = (GradientDrawable) circle_txt.getBackground();
        totalGame = noOfWords;
        setupGame(0);

        LinearLayout circle_linear = (LinearLayout) findViewById(R.id.circle_linear);
        TextView circle_txt = (TextView) findViewById(R.id.circle_txt);
        int height = circle_txt.getLayoutParams().height;

        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(height, height);
        circle_txt.setLayoutParams(Params1);
        circle_txt.setGravity(Gravity.CENTER);
        circle_linear.setGravity(Gravity.CENTER);
    }

    public void checkIfCharArrayEmpty() {
        if (char_array.size() < 2) {
            char_array = backup_char_array;
            Collections.shuffle(char_array);
        }
    }

    public int[] randomize(int length) {

        int showLetters = 1;
        if (length > 5 && length <= 8) {
            showLetters = 2;
        } else if (length > 8 && length <= 13) {
            showLetters = 3;
        } else if (length > 13) {
            showLetters = 4;
        }
        int randomPos[] = new int[showLetters];

        for (int i = 0; i < showLetters; i++) {
            Random rnd = new Random();
            randomPos[i] = rnd.nextInt(noAlpha);
        }
        return randomPos;
    }


    // TODO Reset

    void resetAll() {

        attempts = 0;
        wrong = 0;
        correct = 0;

    }


    public void printCharArray() {
        print("Size of char " + char_array.size());
        String s = "";
        for (int j = 0; j < char_array.size(); j++) {
            s = s + char_array.get(j) + ",";
        }
        Log.d("char_array", s);
    }

    public void displayAllLetters() {
        for (Character x : word_char) {
            String theLetter = String.valueOf(x);
            int count = word.length() - word.replace(theLetter, "").length();
            int s = word.indexOf(theLetter);
            myTextViews[s].setText(theLetter);
            correct += 1;

            if (count > 1) {
                for (int i = 0; i < word.length(); i++) {
                    char g = word.charAt(i);
                    if (theLetter.charAt(0) == g) myTextViews[i].setText(theLetter);
                }
            }

        }
    }


    public void next_btn(View v) {

        if (currentGame + 1 >= totalGame) {
            gameCompleted();
        } else {
            wordset.getWords().get(currentGame).setChancesTaken(maxChances);
            currentGame++;
            displayAllLetters();
            new CountDownTimer(2000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    // do something after 1s
                }

                @Override
                public void onFinish() {
                    resetAll();
                    removeTextViews();
                    setupGame(currentGame);
                }

            }.start();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void back_btn(View v) {
        //TODO Remove this one
        //finish();

        myTextViews[myTextViews.length - 1].setText("X");
    }

    public void prev_btn(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameNewActivity.this);
        builder.setTitle("Back?");
        builder.setMessage("Do you really want to leave this Set?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish(); //TODO previous button
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void setupGame(int gameIndex) {
        List<Character> char_array = new ArrayList<Character>(Arrays.asList(new Character[]{'Z', 'Y', 'X', 'W', 'V', 'U', 'T', 'S', 'R', 'Q', 'P', 'O', 'N', 'M', 'L', 'K', 'J', 'I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A', '1', '2', '3', '4', '5', '6', '7', '8', '9'}));

        if (currentGame > 1) {
            removeTextViews();
        }
        word = wordset.getWords().get(gameIndex).getOriginalValue();
        word = word.toUpperCase();
        System.out.println("Original Value =" + word);
        noAlpha = word.length();
        LinearLayout ll = (LinearLayout) findViewById(R.id.labelsGenerator);
        myTextViews = new TextView[noAlpha]; // create an empty array;
        System.out.println("noAlpha: " + noAlpha);

        title_tv.setText(wordset.getName());
        progress_tv.setText(currentGame + "\\" + totalGame);
        word_char.removeAll(word_char);
        String phaltu = "";
        for (int i = 0; i < word.length(); i++) {
            if (phaltu.contains(String.valueOf(word.charAt(i))) == true) {

            } else {
                phaltu = phaltu + String.valueOf(word.charAt(i));
                if (word.charAt(i) != ' ') {
                    word_char.add(word.charAt(i));
                }
            }
        }


        for (int i = 0; i < noAlpha; i++) {
            // create a new textview
            TextView rowTextView = new TextView(this);

            // set some properties of rowTextView or something
            rowTextView.setText("_");

            String letter = String.valueOf(word.charAt(i));

            //     System.out.println("This letter is =" + letter + "+");
            if (letter.equals(" ") == true) {
                System.out.println("This is a space: " + i);
                rowTextView.setText(" ");
            }
            if (char_array.contains(letter.charAt(0)))
                char_array.remove(char_array.indexOf(letter.charAt(0)));
            if (word.length() > 10) {
                rowTextView.setTextSize(15);
            }

            rowTextView.setTextSize(25);
            rowTextView.setPadding(10, 0, 10, 0);
            // add the textview to the linearlayout
            ll.addView(rowTextView);

            // save a reference to the textview for later
            myTextViews[i] = rowTextView;
            //   System.out.println("Added");
        }

        int count = word.length() - word.replace(" ", "").length();
        noAlpha = (noAlpha - count);
        System.out.println("Count =" + count + "\n " + "noAlpha= " + noAlpha);
        //   processColors = (ImageView) findViewById(R.id.processColor);


        int[] showLetters = randomize(phaltu.length());

        ArrayList<Character> showLetters_list = new ArrayList<Character>();
        for (int i = 0; i < showLetters.length; i++) {
            String letter = String.valueOf(word.charAt(showLetters[i]));
            if (char_array.contains(letter.charAt(0))) {
                char_array.remove(char_array.indexOf(letter.charAt(0)));
            }
            showLetters_list.add(letter.charAt(0));
            if (word_char.contains(letter.charAt(0)))
                word_char.remove(word_char.indexOf(letter.charAt(0)));

            if (char_array.contains(letter.charAt(0)))
                char_array.remove(char_array.indexOf(letter.charAt(0)));

            int counts = word.length() - word.replace(letter, "").length();


            if (word.contains(letter) == true) { //Check for number of same letters
                int s = word.indexOf(letter);
                myTextViews[s].setText(letter);

                if (char_array.contains(letter.charAt(0))) {
                    char_array.remove(char_array.indexOf(letter.charAt(0)));
                }
                if (word_char.contains(letter.charAt(0)))
                    word_char.remove(word_char.indexOf(letter.charAt(0)));

                System.out.println("This number of times of this letter is: " + count);

                if (counts > 1) {
                    System.out.println("This is found two times");

                    for (int d = 0; d < word.length(); d++) {

                        char g = word.charAt(d);


                        if (letter.charAt(0) == g) {
                            System.out.println("Current letter is: " + g);
                            myTextViews[d].setText(letter);
                        }

                    }
                }

            }


            //myTextViews[showLetters[i]].setText(letter);
            System.out.println("DisableButton=" + letter);


        }
        char_array.removeAll(showLetters_list);
        word_char.removeAll(showLetters_list);
        finalCheck();
        backup_char_array = char_array;
        nextButton();
        Collections.shuffle(word_char);
        printCharArray();
        currentChar = word_char.get(0);

    }

    public void testing_btjn(View v) {
        printCharArray();
        print("");
        System.out.println("My Word Array=");

        print("Size of word char " + word_char.size());
        String s = "";
        for (int j = 0; j < word_char.size(); j++) {
            s = s + word_char.get(j);
        }
        Log.d("word_char", s);
        Log.d("currentWord", word);
        Log.d("currentGame", currentGame + "");
    }
}