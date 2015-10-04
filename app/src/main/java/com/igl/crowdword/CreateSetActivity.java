package com.igl.crowdword;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.igl.crowdword.HTTPRequest.GameManager;
import com.igl.crowdword.core.UserFunctions;
import com.igl.crowdword.fxns.Tag;
import com.igl.crowdword.fxns.User;
import com.igl.crowdword.fxns.Word;
import com.igl.crowdword.fxns.WordSet;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class CreateSetActivity extends ActionBarActivity {


    Spinner tags_spn;
    ListView words_listview;
    EditText words_txt;
    EditText tags_txt;
    EditText name_txt;
    android.support.design.widget.TextInputLayout name_txt_layout;
    ArrayList<String> words_list;
    ArrayList<String> tags_list;
    android.support.v7.widget.Toolbar toolbar;
    ArrayAdapter<String> words_adapter;

    public void plus_word_btn(View v) {

        words_list.add(words_txt.getText().toString());
        System.out.println("ADded");
    }

    public void populateTagsList() {
        String text = tags_txt.getText().toString();
        String asd[] = text.split(",");
        tags_list.clear();
        for (String s : asd) {
            tags_list.add(s);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_set);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_createset);
        toolbar.setTitle("Create A Set");
        setSupportActionBar(toolbar);
        words_list = new ArrayList<String>();
        tags_list = new ArrayList<String>();

        words_listview = (ListView) findViewById(R.id.words_listview);
        words_txt = (EditText) findViewById(R.id.words_txt);
        tags_txt = (EditText) findViewById(R.id.tags_txt);
        name_txt = (EditText) findViewById(R.id.name_txt);
        name_txt_layout = (android.support.design.widget.TextInputLayout) findViewById(R.id.name_txt_layout);


        words_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, words_list);

        words_listview.setAdapter(words_adapter);
        words_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                words_adapter.remove(words_adapter.getItem(position));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_set, menu);
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

    public void cancel_set_btn(View v) {
        finish();
    }

    public void create_btn(View v) {
        try {

            UserFunctions.checkInternetConnectionAsync checkInternet = new UserFunctions.checkInternetConnectionAsync();
            Boolean internet = checkInternet.execute(getBaseContext()).get();
            if (words_adapter.getCount() < 5) {
                Toast.makeText(this, "Add more than 5 words", Toast.LENGTH_LONG).show();
            } else if (tags_list.size() < 1) {
                Toast.makeText(this, "Add some tags", Toast.LENGTH_LONG).show();
            } else if (name_txt.getText() == null) {
                name_txt_layout.setError("You need to give it Some Name!");
            } else {
                if (internet == true) {

                    WordSet wordset = new WordSet();

                    User user;
                    UserFunctions us = new UserFunctions();
                    user = us.getCurrentUser(this);
                    wordset.setUser(user);

                    java.util.Date utilDate = us.getCurrentDate();// new java.util.Date();
                    Date sqlDate = new Date(utilDate.getTime());


                    System.out.println("utilDate:" + utilDate);
                    System.out.println("sqlDate:" + sqlDate);

                    wordset.setCreatedDate(sqlDate);
                    wordset.setUserToken(us.getCurrentToken(this));

                    EditText name = (EditText) findViewById(R.id.name_txt);

                    wordset.setName(name.getText().toString());

                    List<Word> words = new ArrayList<Word>();

                    for (int i = 0; i < words_list.size(); i++) {
                        Word word = new Word();
                        word.setOriginalValue(words_list.get(i).toString());
                        words.add(word);
                    }
                    wordset.setWords(words);
                    wordset.setUserToken(us.getCurrentToken(this));

                    ArrayList<Tag> tag_list = new ArrayList<Tag>();
                    for (String s : tags_list) {
                        Tag tag = new Tag();
                        tag.setName(s);
                        tag_list.add(tag);
                    }
                    wordset.setTags(tag_list);

                    GameManager.createSetAsync create = new GameManager.createSetAsync();
                    create.execute(wordset);

                } else if (internet == false) {
                    Toast.makeText(CreateSetActivity.this, "Please make sure your Internet is Working!", Toast.LENGTH_LONG).show();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void cancel_btn(View v) {
        finish();
    }
}
