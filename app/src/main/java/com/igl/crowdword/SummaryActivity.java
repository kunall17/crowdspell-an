package com.igl.crowdword;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.igl.crowdword.R;
import com.igl.crowdword.fxns.Word;
import com.igl.crowdword.fxns.WordSet;
import com.igl.crowdword.fxns.analysis.SetScoreCarrier;

import java.util.ArrayList;
import java.util.List;

public class SummaryActivity extends ActionBarActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        toolbar = (Toolbar) findViewById(R.id.toolbar_summary);
        setSupportActionBar(toolbar);
        SetScoreCarrier ssc;
        Gson gson = new Gson();
        String ssc_json = getIntent().getStringExtra("ssc");
        ssc = gson.fromJson(ssc_json, SetScoreCarrier.class);
        int won = getIntent().getIntExtra("words_won", 0);
        Log.d("jsonr",ssc_json);
        TextView won_text = (TextView) findViewById(R.id.won_txt);
        won_text.setText("You have won " + won + " games!");
        ListView listView = (ListView) findViewById(R.id.words_lost);


        List<Word> words;
        words = ssc.getWords();
        List<String> words_name = new ArrayList<String>();
        String s= "";
        for (Word word : words) {
            words_name.add(word.getOriginalValue());
            s=s+word.getChancesTaken()+",";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, words_name);
        listView.setAdapter(adapter);
        for(Word word : words){

        }
        Log.d("chancesTaken",s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary, menu);
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

    public void backtoDash_btn(View v) {
        finish();
    }
}