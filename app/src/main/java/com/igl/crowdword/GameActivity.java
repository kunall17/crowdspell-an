package com.igl.crowdword;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.igl.crowdword.fxns.Word;
import com.igl.crowdword.fxns.WordSet;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Random;


public class GameActivity extends ActionBarActivity {

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

    //TODO makeshape uniform
    void makeShapeUniform() {

    }

    void updateIndex() {
        progress_tv.setText(currentGame + "/" + totalGame);
    }


    // TODO saveScores
    void saveScores(int gameIndex) {

    }


    private void win() {
        Toast.makeText(this, "Fuck You won.!", Toast.LENGTH_LONG);
    }

    private void loose() {
    }

    public void updateStatus() {
        TextView statusx = (TextView) findViewById(R.id.statusTV);
        statusx.setText("Correct: " + correct + "|Attempt:" + attempts + "Wrong: " + wrong);
    }

    private void checkAlpha(String theLetter) {
        int count = word.length() - word.replace(theLetter, "").length();

        if (word.contains(theLetter) == true) { //Check for number of same letters
            int s = word.indexOf(theLetter);
            myTextViews[s].setText(theLetter);
            correct += 1;
            System.out.println("This number of times of this letter is: " + count);

            if (count > 1) {
                System.out.println("This is more than one time");
                for (int i = 0; i < word.length(); i++) {
                    char g = word.charAt(i);
                    if (theLetter.charAt(0) == g) {
                        System.out.println("Current letter is: " + g);
                        myTextViews[i].setText(theLetter);
                        correct++;
                    }

                }
            }

        } else {
            wrong += 1;
            updateColor();
        }

        attempts++;


        if (attempts == noAlpha) win();
    }


    void updateColor() {


        if (wrong == 5) {
            Toast.makeText(this, "Your Chances khatam ho gayi!", Toast.LENGTH_LONG).show();
            circle.setColor(getResources().getColor(R.color.chances_left_5));
            circle_txt.setText("5");
        } else if (wrong == 1) {
            circle.setColor(getResources().getColor(R.color.chances_left_1));
            circle_txt.setText("1");
        } else if (wrong == 2) {
            circle.setColor(getResources().getColor(R.color.chances_left_2));
            circle_txt.setText("2");
        } else if (wrong == 3) {
            circle.setColor(getResources().getColor(R.color.chances_left_3));
            circle_txt.setText("3");
        } else if (wrong == 4) {
            circle.setColor(getResources().getColor(R.color.chances_left_4));
            circle_txt.setText("4");
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
        LinearLayout formLayout = (LinearLayout) findViewById(R.id.labelsGenerator);
        formLayout.removeAllViews();
        for (int i = 0; i < myTextViews.length; i++) {
            formLayout.removeView(myTextViews[i]);
        }
    }


    void setupGame(int gameIndex) {

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

        String phaltu = "";
        for (int i = 0; i < word.length(); i++) {
            if (phaltu.contains(String.valueOf(word.charAt(i))) == true) {

            } else {
                phaltu = phaltu + String.valueOf(word.charAt(i));

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
        for (int i = 0; i < showLetters.length; i++) {
            String letter = String.valueOf(word.charAt(showLetters[i]));

            int counts = word.length() - word.replace(letter, "").length();


            if (word.contains(letter) == true) { //Check for number of same letters
                int s = word.indexOf(letter);
                myTextViews[s].setText(letter);

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
            disableButton(letter);

        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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
        int height = circle_linear.getLayoutParams().height;
        circle_txt.setHeight(height);
        circle_txt.setWidth(height);
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

    public void next_btn(View v) {
        if (currentGame == totalGame) {
            completedGame();
        } else {
            currentGame++;
            resetAll();
            setupGame(currentGame);

        }

    }

    //TODO completedGame
    void completedGame() {
    }


    // TODO Reset

    void resetAll() {

        findViewById(R.id.A).setEnabled(true);
        findViewById(R.id.B).setEnabled(true);
        findViewById(R.id.C).setEnabled(true);
        findViewById(R.id.D).setEnabled(true);
        findViewById(R.id.E).setEnabled(true);
        findViewById(R.id.F).setEnabled(true);
        findViewById(R.id.G).setEnabled(true);
        findViewById(R.id.H).setEnabled(true);
        findViewById(R.id.I).setEnabled(true);
        findViewById(R.id.J).setEnabled(true);
        findViewById(R.id.K).setEnabled(true);
        findViewById(R.id.L).setEnabled(true);
        findViewById(R.id.M).setEnabled(true);
        findViewById(R.id.N).setEnabled(true);
        findViewById(R.id.O).setEnabled(true);
        findViewById(R.id.P).setEnabled(true);
        findViewById(R.id.Q).setEnabled(true);
        findViewById(R.id.R).setEnabled(true);
        findViewById(R.id.S).setEnabled(true);
        findViewById(R.id.T).setEnabled(true);
        findViewById(R.id.U).setEnabled(true);
        findViewById(R.id.V).setEnabled(true);
        findViewById(R.id.W).setEnabled(true);
        findViewById(R.id.X).setEnabled(true);
        findViewById(R.id.Y).setEnabled(true);
        findViewById(R.id.Z).setEnabled(true);
        findViewById(R.id.btn_1).setEnabled(true);
        findViewById(R.id.btn_2).setEnabled(true);
        findViewById(R.id.btn_3).setEnabled(true);
        findViewById(R.id.btn_4).setEnabled(true);
        findViewById(R.id.btn_5).setEnabled(true);
        findViewById(R.id.btn_6).setEnabled(true);
        findViewById(R.id.btn_7).setEnabled(true);
        findViewById(R.id.btn_8).setEnabled(true);
        findViewById(R.id.btn_9).setEnabled(true);
        findViewById(R.id.btn_10).setEnabled(true);
        attempts = 0;
        wrong = 0;
        correct = 0;

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


    public void keyBtn_Click(View v) {
        String s = "";

        Button b = (Button) findViewById(v.getId());
        s = b.getText().toString();
        b.setEnabled(false);
        System.out.println("ASDASD");
        checkAlpha(s);
        updateStatus();
    }

    public void back_btn(View v) {
        //TODO Remove this one
        //finish();

        myTextViews[myTextViews.length - 1].setText("X");
    }

    public void bcwd_btn(View v) {

    }

    public void fwd_btn(View v) {

    }


    void disableButton(String letter) {
        System.out.println("disable Button =" + letter);
        letter = letter.toUpperCase();
        if (letter.equals("A") == true) {
            Button A_btn = (Button) findViewById(R.id.A);
            A_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("Z") == true) {
            Button Z_btn = (Button) findViewById(R.id.Z);
            Z_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("Y") == true) {
            Button Y_btn = (Button) findViewById(R.id.Y);
            Y_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("X") == true) {
            Button X_btn = (Button) findViewById(R.id.X);
            X_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("W") == true) {
            Button W_btn = (Button) findViewById(R.id.W);
            W_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("V") == true) {
            Button V_btn = (Button) findViewById(R.id.V);
            V_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("U") == true) {
            Button U_btn = (Button) findViewById(R.id.U);
            U_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("T") == true) {
            Button T_btn = (Button) findViewById(R.id.T);
            T_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("S") == true) {
            Button S_btn = (Button) findViewById(R.id.S);
            S_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("R") == true) {
            Button R_btn = (Button) findViewById(R.id.R);
            R_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("Q") == true) {
            Button Q_btn = (Button) findViewById(R.id.Q);
            Q_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("P") == true) {
            Button P_btn = (Button) findViewById(R.id.P);
            P_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("O") == true) {
            Button O_btn = (Button) findViewById(R.id.O);
            O_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("N") == true) {
            Button N_btn = (Button) findViewById(R.id.N);
            N_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("M") == true) {
            Button M_btn = (Button) findViewById(R.id.M);
            M_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("L") == true) {
            Button L_btn = (Button) findViewById(R.id.L);
            L_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("K") == true) {
            Button K_btn = (Button) findViewById(R.id.K);
            K_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("J") == true) {
            Button J_btn = (Button) findViewById(R.id.J);
            J_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("I") == true) {
            Button I_btn = (Button) findViewById(R.id.I);
            I_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("H") == true) {
            Button H_btn = (Button) findViewById(R.id.H);
            H_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("G") == true) {
            Button G_btn = (Button) findViewById(R.id.G);
            G_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("F") == true) {
            Button F_btn = (Button) findViewById(R.id.F);
            F_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("E") == true) {
            Button E_btn = (Button) findViewById(R.id.E);
            E_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("D") == true) {
            Button D_btn = (Button) findViewById(R.id.D);
            D_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("C") == true) {
            Button C_btn = (Button) findViewById(R.id.C);
            C_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("B") == true) {
            Button B_btn = (Button) findViewById(R.id.B);
            B_btn.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("1") == true) {
            Button btn_1 = (Button) findViewById(R.id.btn_1);
            btn_1.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("2") == true) {
            Button btn_2 = (Button) findViewById(R.id.btn_2);
            btn_2.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("3") == true) {
            Button btn_3 = (Button) findViewById(R.id.btn_3);
            btn_3.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("4") == true) {
            Button btn_4 = (Button) findViewById(R.id.btn_4);
            btn_4.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("5") == true) {
            Button btn_5 = (Button) findViewById(R.id.btn_5);
            btn_5.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("6") == true) {
            Button btn_6 = (Button) findViewById(R.id.btn_6);
            btn_6.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("7") == true) {
            Button btn_7 = (Button) findViewById(R.id.btn_7);
            btn_7.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("8") == true) {
            Button btn_8 = (Button) findViewById(R.id.btn_8);
            btn_8.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("9") == true) {
            Button btn_9 = (Button) findViewById(R.id.btn_9);
            btn_9.setEnabled(false);
            System.out.println("ASDASD");
        } else if (letter.equals("10") == true) {
            Button btn_10 = (Button) findViewById(R.id.btn_10);
            btn_10.setEnabled(false);
            System.out.println("ASDASD");
        }

    }
}
