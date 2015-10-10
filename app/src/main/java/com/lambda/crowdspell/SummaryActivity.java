package com.lambda.crowdspell;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lambda.crowdspell.HTTPRequest.ApiPaths;
import com.lambda.crowdspell.HTTPRequest.GameManager;
import com.igl.crowdword.R;
import com.lambda.crowdspell.core.UserFunctions;
import com.lambda.crowdspell.fxns.Word;
import com.lambda.crowdspell.fxns.WordSet;
import com.lambda.crowdspell.fxns.analysis.SetScoreCarrier;
import com.lambda.crowdspell.fxns.analysis.UserPoints;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SummaryActivity extends ActionBarActivity {

    Toolbar toolbar;
    List<WordSet> wordset_list = null;
    List<WordSet> fav_list;
    List<String> top_list;

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
        Log.d("jsonr", ssc_json);
        TextView won_text = (TextView) findViewById(R.id.won_txt);
        won_text.setText("You have won " + won + " games!");
        ListView listView = (ListView) findViewById(R.id.words_lost);


        List<Word> words;
        words = ssc.getWords();
        List<String> words_name = new ArrayList<String>();
        String s = "";
        String words_lost[] = getIntent().getStringArrayExtra("words_lost");
        for (int i = 0; i < words_lost.length; i++) {
            if (words_lost[i] != null) words_name.add(words_lost[i]);
//            s = s + word.getChancesTaken() + ",";
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, words_name);
        listView.setAdapter(adapter);

        toolbar.setTitle("Summary");

        Log.d("chancesTaken", s);

        if (new UserFunctions().checkIfGuestModeIsOn(getBaseContext())) {
            Toast.makeText(SummaryActivity.this, "You are guest your scores aren't saved!", Toast.LENGTH_LONG).show();
        } else {
            submitScore(ssc);
        }

    }

    public void submitScore(SetScoreCarrier ssc) {
        GameManager.submitScoreAsync submit = new GameManager.submitScoreAsync(SummaryActivity.this);
        int code = 0;
        try {
            code = submit.execute(ssc).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("Score", code + "");
        if (code < 300 && code > 199) {
            Toast.makeText(SummaryActivity.this, "Saved Your Score", Toast.LENGTH_LONG).show();
        } else {
            submitScore(ssc);
            Log.d("submitScore", "Some problem is coming");
        }
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
        Intent in1 = new Intent(SummaryActivity.this, DashboardActivity.class);
        startActivity(in1);
    }

    public void startDashboard(final Boolean recurse) {

        Boolean internet = false;

        try {
            UserFunctions.checkInternetConnectionAsync checkInternet = new UserFunctions.checkInternetConnectionAsync();
            internet = checkInternet.execute(getBaseContext()).get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (internet) {
            getAllLists asd = new getAllLists(SummaryActivity.this);
            asd.execute(this);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(SummaryActivity.this);
            builder.setTitle("Internet");
            builder.setMessage("Please make sure Internet is working.!");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    startDashboard(recurse);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    System.exit(0);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();


//            in1.putExtra("json_sets", "");
//            in1.putExtra("json_top", "");
//            in1.putExtra("json_fav", "");
//            startActivity(in1);

        }


    }

    public class getAllLists extends AsyncTask<Context, String, List<UserPoints>> {
        ProgressDialog progress;
        List<UserPoints> userpoints_list;
        List<WordSet> favourites_list;
        Context context;

        public getAllLists(Context context) {
            this.context = context;
            progress = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
            progress.setMessage("Getting WordSets :) ");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected List<UserPoints> doInBackground(Context... params) {
            String output = "";
            try {
                if (getResources().getString(R.string.SERVER_ADDRESS) == getResources().getString(R.string.SERVER_ADDRESS1)) { //Remove this

                    URL url = new URL("http://46.101.37.183:8080/crowdspell-web/api/v1/sets/new");

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty(ApiPaths.APP_AUTH_KEY,
                            ApiPaths.ANDROID_APP_KEY);
                    con.connect();

                    int code = con.getResponseCode();
                    Log.d("Code", code + "");
                    output = readFromConnection(con);
                } else {
                    output = getJson();
                }

                Gson gson = getJsonWriterWithCustomDate();
                WordSet[] sets = gson.fromJson(output, WordSet[].class);
                wordset_list = Arrays.asList(sets);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            publishProgress("Getting Tops :)");

            try {
                URL url = new URL("http://46.101.37.183:8080/crowdspell-web/api/v1/scores");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty(ApiPaths.APP_AUTH_KEY,
                        ApiPaths.ANDROID_APP_KEY);
                con.connect();

//                HttpURLConnection connection = getConnection(ApiPaths.SCORES);
                String content = null;
                content = readFromConnection(con);
                Gson gson = getJsonWriterWithCustomDate();
                UserPoints[] userPoints = gson.fromJson(content, UserPoints[].class);
                userpoints_list = Arrays.asList(userPoints);
                Log.d("userPoints", "code-" + con.getResponseCode());
            } catch (IOException e) {
                e.printStackTrace();
            }
            publishProgress("Getting Favourites :)");

            UserFunctions uf = new UserFunctions();
            String token = uf.getCurrentToken(getBaseContext());
            try {
                URL url = new URL("http://46.101.37.183:8080/crowdspell-web/api/v1/favourites/" + token);
                favourites_list = new ArrayList<WordSet>();
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty(ApiPaths.APP_AUTH_KEY,
                        ApiPaths.ANDROID_APP_KEY);
                con.connect();

//                HttpURLConnection connection = getConnection(ApiPaths.FAVOURITES + "/" + params[0]);
                String listString = readFromConnection(con);
                Gson gson = getJsonWriterWithCustomDate();
                WordSet[] favourites = gson.fromJson(listString, WordSet[].class);
                favourites_list = Arrays.asList(favourites);
            } catch (Exception e) {

            }
            return null;
        }

        protected void onProgressUpdate(String... progUpdate) {
            progress.setMessage(progUpdate[0]);
        }

        protected void onPostExecute(List<UserPoints> userPoints) {
            top_list = new ArrayList<String>();
            Intent in1 = new Intent(SummaryActivity.this, DashboardNewActivity.class);
            String top_list_string[] = null;

            if (userpoints_list != null) {
                Log.d("userpoints", "Got top scores");
                for (UserPoints up : userpoints_list) {
                    top_list.add(up.getUser().getUsername() + " : " + up.getPoints());
                }
                top_list_string = top_list.toArray(new String[top_list.size()]);
                in1.putExtra("json_top", top_list_string);
            } else {
                in1.putExtra("json_top", top_list_string);
            }
            Gson gson = new Gson();
            String json_sets = gson.toJson(wordset_list);
            String json_fav = gson.toJson(favourites_list);
            in1.putExtra("json_sets", json_sets);

            if (favourites_list.size() == 0) {
                in1.putExtra("json_fav", "");
            } else {
                in1.putExtra("json_fav", json_fav);
            }

            progress.dismiss();
            startActivity(in1);
        }

    }

    String getJson() {
        InputStream is = getResources().openRawResource(R.raw.getwords);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return writer.toString();

    }



    protected static String readFromConnection(HttpURLConnection connection)
            throws IOException {
        InputStream iStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                iStream));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            response.append(line);
            response.append('\n');
        }
        reader.close();
        return response.toString();
    }

    protected static Gson getJsonWriterWithCustomDate() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        return gson;
    }

}
