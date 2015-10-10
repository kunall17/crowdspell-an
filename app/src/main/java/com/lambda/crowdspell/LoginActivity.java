package com.lambda.crowdspell;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lambda.crowdspell.HTTPRequest.ApiPaths;
import com.lambda.crowdspell.HTTPRequest.GameManager;
import com.lambda.crowdspell.HTTPRequest.UserManager;
import com.igl.crowdword.R;
import com.lambda.crowdspell.core.UserFunctions;
import com.lambda.crowdspell.fxns.User;
import com.lambda.crowdspell.fxns.UserDetails;
import com.lambda.crowdspell.fxns.WordSet;
import com.lambda.crowdspell.fxns.analysis.UserPoints;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class LoginActivity extends ActionBarActivity {


    //TODO 404 exception coming on login do error handling.!
    EditText user_ET;
    EditText pass_ET;
    List<WordSet> wordset_list = null;
    List<WordSet> fav_list;
    List<String> top_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_ET = (EditText) findViewById(R.id.loginIdText);
        pass_ET = (EditText) findViewById(R.id.pwdText);


        UserFunctions asd = new UserFunctions();
        if (asd.checkIfGuestModeIsOn(this)) {
            System.out.println("ASD");
            startDashboard(true);
        } else if (asd.checkIfSharedPreferencesforUserExists(this)) {
            System.out.println("ASD123");
            asd.deleteSharedPreferences(this);
            Log.d("xxx", asd.getCurrentUsername(this));
            startDashboard(true);


        }
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
            getAllLists asd = new getAllLists(LoginActivity.this);
            asd.execute(this);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void loginBtn_click(View v) {

        UserFunctions usf = new UserFunctions();
        if (getResources().getString(R.string.SERVER_ADDRESS) == getResources().getString(R.string.SERVER_ADDRESS1)) { //Remove this
            UserFunctions.checkInternetConnectionAsync checkInternet = new UserFunctions.checkInternetConnectionAsync();
            try {
                if (checkInternet.execute(getBaseContext()).get() == true) {
                    Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            User user = new User();
            UserDetails ud = new UserDetails();
            user.setDetails(ud);
            if (String.valueOf(user_ET.getText()) != null || String.valueOf(pass_ET.getText()) != null) {
                String username = String.valueOf(user_ET.getText());
                String password = pass_ET.getText().toString();

                try {

                    String token = null;
                    user.setUsername(username);
                    user.setPassword(password);
                    UserManager.CheckLogin checklogin = new UserManager.CheckLogin();
                    token = checklogin.execute(user).get();
                    Log.d("token", token);
                    if (token.length() == 36) {
                        user.setToken(token);
                        startDashboard(false);
                        new UserFunctions().saveToSharedPreferences(user, this);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else if (user_ET.getText() == null || pass_ET.getText() == null) {
                Toast.makeText(this, "Please Enter Password and UserName!", Toast.LENGTH_LONG).show();
            }
        } else {

            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }
    }

    public void newUser_click(View v) {


        Intent intent = new Intent(this, newUserActivity.class);
        startActivity(intent);
    }


//    UserManager.GetData();
    //    UserManager.postRequest("http://46.101.37.183:8080/crowdspell-web/api/v1/login",user);
    //    requestData("http://46.101.37.183:8080/crowdspell-web/api/v1/hello");

    public void guest_click(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Guest Mode?");
        builder.setMessage(getString(R.string.guestmode_warning));
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                UserFunctions asd = new UserFunctions();
                asd.saveAsGuest(getBaseContext());
                startDashboard(true);
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

    public static String getData(String uri) {
        BufferedReader bufferedReader = null;
        String finalText = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sp = new StringBuilder();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sp.append(line);

            }
            finalText = sp.toString();

        } catch (Exception e) {

        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return finalText;

    }

    public void updateDisplay(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void initializeSetsList() {
        Log.d("Initialing list called", "called");
        String json1 = "";
        wordset_list = new ArrayList<WordSet>();


        GameManager.getAllSetsAsync2 asd = new GameManager.getAllSetsAsync2();

        if (getResources().getString(R.string.SERVER_ADDRESS) == getResources().getString(R.string.SERVER_ADDRESS1)) { //Remove this
            try {
                wordset_list = asd.execute(this).get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            //nameArray = getNameArray(wordset_list).toArray(new String[wordset_list.size()]);
        } else {
            json1 = getJson();
            JsonElement json = new JsonParser().parse(json1);
            JsonArray array = json.getAsJsonArray();
            Iterator iterator = array.iterator();
            int i = 0;

            while (iterator.hasNext()) {
                JsonElement json2 = (JsonElement) iterator.next();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
                WordSet wordset = gson.fromJson(json2, WordSet.class);
                i++;
                wordset_list.add(wordset);
            }
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
            Intent in1 = new Intent(LoginActivity.this, DashboardNewActivity.class);
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

    public static class getAllSetsAsync2 extends AsyncTask<Context, List<WordSet>, List<WordSet>> {

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
        }

        @Override
        protected List<WordSet> doInBackground(Context... params) {
            URL url;
            try {
                context = params[0];

                url = new URL("http://46.101.37.183:8080/crowdspell-web/api/v1/sets/new");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty(ApiPaths.APP_AUTH_KEY,
                        ApiPaths.ANDROID_APP_KEY);
                con.connect();

                int code = con.getResponseCode();
                Log.d("Code", code + "");
//           HttpURLConnection connection = getConnection(ApiPaths.SETS + "/" + ApiPaths.SORT_NEW);
                String output = readFromConnection(con);
                Gson gson = getJsonWriterWithCustomDate();
                WordSet[] sets = gson.fromJson(output, WordSet[].class);
                return Arrays.asList(sets);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        Context context;

        protected void onPostExecute(List<WordSet> result) {
//            System.out.println("Result is here:-" + result);
        }

    }

    public class getAllTopScoresAsync extends AsyncTask<String, List<UserPoints>, List<UserPoints>> {

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
        }

        @Override
        protected List<UserPoints> doInBackground(String... params) {

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
                return Arrays.asList(userPoints);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(List<UserPoints> userPoints) {
            top_list = new ArrayList<String>();
            for (UserPoints up : userPoints) {
                top_list.add(up.getUser().getUsername() + " : " + up.getPoints());
            }
        }

    }

    public class getUserFavouritesAsync extends AsyncTask<String, List<WordSet>, List<WordSet>> {

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
        }

        @Override
        protected List<WordSet> doInBackground(String... params) {

            return null;
        }


        protected void onPostExecute(List<WordSet> result) {
            System.out.println("Result is here:-" + result);
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


    private static HttpURLConnection getBaseConnection(String extension)
            throws IOException {
        // Change URL here.

        String urla = String.valueOf(R.string.SERVER_ADDRESS).toString();
        URL url = new URL(urla + extension);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty(ApiPaths.APP_AUTH_KEY, ApiPaths.ANDROID_APP_KEY);
//        connection.setDoInput(true);
//        connection.setDoOutput(true);

        return connection;
    }

    protected static HttpURLConnection getPostConnection(String path)
            throws IOException {
        HttpURLConnection connection = getBaseConnection(path);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("connectiontent-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        // connection.setRequestProperty("app-id", "4b08dee3-c8ec-40a0-99d1-4aee47c0772a");
        return connection;
    }

    protected static HttpURLConnection getConnection(String path)
            throws IOException {
        HttpURLConnection connection = getBaseConnection(path);
        connection.setRequestMethod("GET");
        return connection;
    }

    protected static void writeToOutputStream(HttpURLConnection connection,
                                              String content) throws IOException {
        DataOutputStream stream = new DataOutputStream(
                connection.getOutputStream());
        stream.writeUTF(content);
        stream.flush();
        stream.close();
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