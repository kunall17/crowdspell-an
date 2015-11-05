package com.lambda.crowdspell;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lambda.crowdspell.HTTPRequest.UserManager;
import com.igl.crowdword.R;
import com.lambda.crowdspell.core.Stopwatch;
import com.lambda.crowdspell.core.UserFunctions;
import com.lambda.crowdspell.fxns.Word;
import com.lambda.crowdspell.fxns.WordSet;
import com.lambda.crowdspell.fxns.analysis.SetScoreCarrier;
import com.lambda.crowdspell.fxns.analysis.UserFavourites;
import com.lambda.crowdspell.fxns.analysis.WordSetVote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

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
    List<String> wordsLost;

    TextView[] myTextViews = new TextView[20]; // create an empty array;


    ImageView processColors;

    int currentGame = 0;
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
    Button left;
    Button right;
    Button middle;
    String words_lost[];
    TextView stopwatch_txt;
    CountDownTimer timer;

    public void print(String text) {
        System.out.println(text);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        finish();
    }

    void gameCompleted() {
        Intent in1 = new Intent(GameNewActivity.this, SummaryActivity.class);
        Gson gson = new Gson();

        SetScoreCarrier ssc = new SetScoreCarrier();
        ssc.setWords(wordset.getWords());
        ssc.setSetId(wordset.getId());
        ssc.setUserToken(new UserFunctions().getCurrentToken(GameNewActivity.this));

        in1.putExtra("words_lost", words_lost);

        String ssc_json = gson.toJson(ssc);
        in1.putExtra("ssc", ssc_json);
        Log.d("json", ssc_json);
        timer.cancel();
        in1.putExtra("words_won", words_won);
        in1.putExtra("wordsetId", wordset.getId());
        startActivityForResult(in1, 0);

    }

    public void favourite_btn(View v) {
        UserFunctions uf = new UserFunctions();
        String token = uf.getCurrentToken(getBaseContext());

        UserManager.addUserFavouritesAsync asd = new UserManager.addUserFavouritesAsync();

        try {
            if (getResources().getString(R.string.SERVER_ADDRESS) == getResources().getString(R.string.SERVER_ADDRESS1)) { //Remove this
                UserFunctions.checkInternetConnectionAsync checkInternet = new UserFunctions.checkInternetConnectionAsync();
                if (checkInternet.execute(getBaseContext()).get() == true) {
                    Toast.makeText(getBaseContext(), "Please check your Internet Connection", Toast.LENGTH_LONG).show();
                    return;
                }

                WordSetVote setvote = new WordSetVote();
                setvote.setUser(new UserFunctions().getCurrentUser(getBaseContext()));
                setvote.setId(wordset.getId());
                int code = asd.execute(setvote).get();
                if (code < 300 && code > 199) {
                    ImageButton ib = (ImageButton) findViewById(R.id.favourite_btn);
                    ib.setBackgroundResource(R.drawable.ic_action_important);

                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void win() {
        Toast.makeText(this, "Fuck You won.!", Toast.LENGTH_LONG);
        words_won++;
        if (currentGame + 1 == wordset.getWords().size()) {
            wordset.getWords().get(currentGame).setChancesTaken(maxChances);
//            words_lost[currentGame] = word;
            currentGame++;
            displayAllLetters();
            UserFavourites asd;

            gameCompleted();
        } else {

            if (wrong > maxChances) {
                //TODO snackbar Snackbar.make(getBaseContext(),text,Snackbar.LENGTH_LONG).show();
                Toast.makeText(this, "You took more than 5 chances, you wont be awarded points for this one!", Toast.LENGTH_SHORT).show();
                wordset.getWords().get(currentGame).setChancesTaken(maxChances);
            } else {
                wordset.getWords().get(currentGame).setChancesTaken(attempts);
                Toast.makeText(this, "You Completed in " + wrong + " wrong attempts!", Toast.LENGTH_SHORT).show();

            }
            currentGame += 1;
            rotateTextView(TEXT_WIN);


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

    public void help_btn(View v) {
        Snackbar.make(v, "Click on the right option!", Snackbar.LENGTH_LONG).setAction("DISMISS", null).show();
    }

    public static final int TEXT_WIN = 0;
    public static final int TEXT_LOOSE = 1;
    public static final int TEXT_DEFAULT = 3;
    public static final int ANIMATION_DURATION = 700;

    public void rotateTextView(int position) {
        circle.setColor(getResources().getColor(R.color.color_circle));
        switch (position) {
            case TEXT_WIN:

                RotateAnimation rotate = new RotateAnimation(0f, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

// prevents View from restoring to original direction.
                rotate.setFillAfter(true);
                rotate.setDuration(ANIMATION_DURATION);
                circle_txt.startAnimation(rotate);
                circle_txt.setText(":)");

                break;
            case TEXT_LOOSE:
                RotateAnimation rotate1 = new RotateAnimation(0f, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
// prevents View from restoring to original direction.
                rotate1.setFillAfter(true);
                circle.setColor(getResources().getColor(R.color.color_circle_wrong));
                rotate1.setDuration(ANIMATION_DURATION);
                circle_txt.startAnimation(rotate1);

                circle_txt.setText(":(");
                break;
            case TEXT_DEFAULT:
                RotateAnimation rotate2 = new RotateAnimation(0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
// prevents View from restoring to original direction.
                rotate2.setFillAfter(true);
                rotate2.setDuration(100);
                circle_txt.startAnimation(rotate2);
                circle_txt.setText("5");
                break;
        }

    }

    public void next_btn(View v) {
        imageButton.setEnabled(false);
        if (currentGame + 1 >= totalGame) {
            wordset.getWords().get(currentGame).setChancesTaken(maxChances);
            words_lost[currentGame] = word;
            currentGame++;
            displayAllLetters();
            new CountDownTimer(2000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    // do something after 1s
                }

                @Override
                public void onFinish() {
                    gameCompleted();

                }

            }.start();
        } else {
            wordset.getWords().get(currentGame).setChancesTaken(maxChances);
            words_lost[currentGame] = word;
            currentGame++;
            displayAllLetters();
            rotateTextView(TEXT_LOOSE);
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
                    imageButton.setEnabled(true);

                    wrong = 0;
                    updateColor();
                }

            }.start();
        }

    }

    public void updateStatus() {
        TextView statusx = (TextView) findViewById(R.id.statusTV);
        statusx.setText("Correct: " + correct + "|Attempt:" + attempts + "Wrong: " + wrong);
    }


    void updateColor() {

        if (wrong == 1) {
            circle.setColor(Color.argb(255, 241, 196, 15));
            circle_txt.setText(4 + "");
        } else if (wrong == 2) {
            circle.setColor(Color.argb(255, 241, 155, 15));
            circle_txt.setText(3 + "");
        } else if (wrong == 3) {
            circle.setColor(Color.argb(255, 247, 128, 15));
            circle_txt.setText(2 + "");
        } else if (wrong == 4) {
            circle.setColor(Color.argb(255, 247, 57, 15));
            circle_txt.setText(1 + "");
        } else if (wrong == 5) {
            Toast.makeText(this, "You lost this Challenge!", Toast.LENGTH_SHORT).show();
            circle.setColor(Color.argb(255, 247, 49, 15));
            circle_txt.setText(0 + "");
        } else if (wrong == 6) {
            circle.setColor(Color.argb(255, 148, 11, 0));
            circle_txt.setText(6 + "");
        } else if (wrong == 0) {
            circle.setColor(getResources().getColor(R.color.chances_left_0));
            circle_txt.setText(0 + "");
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
        LinearLayout formLayout2 = (LinearLayout) findViewById(R.id.labelsGenerator2);
        formLayout.removeAllViews();
        formLayout2.removeAllViews();
        for (int i = 0; i < myTextViews.length; i++) {
            formLayout.removeView(myTextViews[i]);
            formLayout2.removeView(myTextViews[i]);
        }
    }


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
            updateStatus();
        }

        attempts++;
        updateStatus();
        updateColor();
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
        stopwatch_txt = (TextView) findViewById(R.id.stopwatch_txt);
        Stopwatch sw = new Stopwatch();
        sw.start();

        left = (Button) findViewById(R.id.left_btn);
        right = (Button) findViewById(R.id.right_btn);
        middle = (Button) findViewById(R.id.middle_btn);
//        String text = getIntent().getStringExtra("wordName");
//        String json_wordset = getIntent().getStringExtra("json_wordset");
        String json_new = getIntent().getStringExtra("json_new");
        Gson gson = new Gson();
        wordset = gson.fromJson(json_new, WordSet.class);
        getWindow().setTitle(wordset.getName());
        wordsLost = new ArrayList<String>();
        int noOfWords = wordset.getWords().size();

        title_tv = (TextView) findViewById(R.id.title_TV);
        progress_tv = (TextView) findViewById(R.id.progress_TV);
        circle_txt = (TextView) findViewById(R.id.circle_txt);
        circle = (GradientDrawable) circle_txt.getBackground();
        totalGame = noOfWords;
        setupGame(0);
        words_lost = new String[noOfWords];
        LinearLayout circle_linear = (LinearLayout) findViewById(R.id.circle_linear);
        TextView circle_txt = (TextView) findViewById(R.id.circle_txt);
        int height = circle_txt.getLayoutParams().height;

        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(height * 2 / 3, height * 2 / 3);
        circle_txt.setLayoutParams(Params1);
        circle_txt.setGravity(Gravity.CENTER);
        circle_linear.setGravity(Gravity.CENTER);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        for (Word word : wordset.getWords()) {
            System.out.append(word.getOriginalValue() + ",");
        }

        Log.d("asd", "" + circle_txt.getRotation());


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

    ImageButton imageButton;


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
        rotateTextView(TEXT_DEFAULT);
        List<Character> char_array = new ArrayList<Character>(Arrays.asList(new Character[]{'Z', 'Y', 'X', 'W', 'V', 'U', 'T', 'S', 'R', 'Q', 'P', 'O', 'N', 'M', 'L', 'K', 'J', 'I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A', '1', '2', '3', '4', '5', '6', '7', '8', '9'}));
        if (currentGame != 0) {
            removeTextViews();
            updateColor();
            updateStatus();
        }
        word = wordset.getWords().get(gameIndex).getOriginalValue();
        word = word.toUpperCase();
        System.out.println("Original Value =" + word);
        noAlpha = word.length();
        LinearLayout ll = (LinearLayout) findViewById(R.id.labelsGenerator);
        LinearLayout ll2 = (LinearLayout) findViewById(R.id.labelsGenerator2);
        myTextViews = new TextView[noAlpha]; // create an empty array;
        System.out.println("noAlpha: " + noAlpha);

        DisplayMetrics size = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(size);
        int maxWidth = size.widthPixels;

        title_tv.setText(wordset.getName());
        progress_tv.setText(currentGame + "\\" + (totalGame - 1));
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

        int sum = 0;

        Log.d("maxWidth", "" + maxWidth);
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


            rowTextView.setTextSize(25);
            Log.d("row width", "before padding-" + rowTextView.getTextSize());
            rowTextView.setPadding(10, 0, 10, 0);
            // add the textview to the linearlayout
//            if (word.length() > 10) {
//                rowTextView.setTextSize(20);
//                rowTextView.setPadding(7, 0, 7, 0);
//
//            }
//row
            Log.d("row width", " " + rowTextView.getTextSize());
            sum += rowTextView.getTextSize() + rowTextView.getPaddingLeft() + rowTextView.getPaddingRight();

            if (maxWidth < sum) {
                ll2.addView(rowTextView);
            } else {
                ll.addView(rowTextView);

            }


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
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(500000, 1000) {
            int sec = 0;

            public void onTick(long millisUntilFinished) {
                sec++;
                stopwatch_txt.setText(String.format("%02d:%02d", sec / 60, sec % 60));
            }

            public void onFinish() {
                Toast.makeText(GameNewActivity.this, "Timeout!", Toast.LENGTH_SHORT).show();
                imageButton.setEnabled(false);
                if (currentGame + 1 >= totalGame) {
                    wordset.getWords().get(currentGame).setChancesTaken(maxChances);
                    words_lost[currentGame] = word;
                    currentGame++;
                    displayAllLetters();
                    new CountDownTimer(2000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            // do something after 1s
                        }

                        @Override
                        public void onFinish() {

                        }

                    }.start();
                } else {
                    wordset.getWords().get(currentGame).setChancesTaken(maxChances);
                    words_lost[currentGame] = word;
                    currentGame++;
                    displayAllLetters();
                    rotateTextView(TEXT_LOOSE);
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
                            imageButton.setEnabled(true);

                            wrong = 0;
                            updateColor();
                        }

                    }.start();
                }

//                mTextField.setText("done!");
            }
        }.start();
    }

    public void testing_btjn(View v) {
        for (Character x : word_char) {
            checkAlpha(String.valueOf(x));
        }
//
//        printCharArray();
//        print("");
//        System.out.println("My Word Array=");
//
//        print("Size of word char " + word_char.size());
//        String s = "";
//        for (int j = 0; j < word_char.size(); j++) {
//            s = s + word_char.get(j);
//        }
//        Log.d("word_char", s);
//        Log.d("currentWord", word);
//        Log.d("currentGame", currentGame + "");
    }

    public void share_btn(View v) {
        String shareBody = getResources().getString(R.string.share_msg) +  wordset.getId();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(GameNewActivity.this);
        builder.setTitle("Exit?");
        builder.setMessage("Are you Sure you want leave the set?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
//                Intent in1 = new Intent(GameNewActivity.this,DashboardNewActivity.class);
//                startActivity(in1);
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
}