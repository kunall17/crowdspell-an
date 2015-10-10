package com.lambda.crowdspell.HTTPRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.igl.crowdword.R;
import com.lambda.crowdspell.SummaryActivity;
import com.lambda.crowdspell.fxns.WordSet;
import com.lambda.crowdspell.fxns.analysis.SetScoreCarrier;
import com.lambda.crowdspell.fxns.analysis.UserPoints;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Kunal on 7/16/2015.
 */
public class GameManager extends NetworkManager {

    private static Logger LOG = Logger.getLogger(GameManager.class.getName());


    public static class getAllSetsAsync extends AsyncTask<Context, List<WordSet>, List<WordSet>> {

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
        }

        @Override
        protected List<WordSet> doInBackground(Context... params) {
            try {
                HttpURLConnection connection = getConnection(ApiPaths.SETS + "/" + ApiPaths.SORT_NEW);
                Gson gson = getJsonWriterWithCustomDate();
                String output = readFromConnection(connection);
                Log.d("WORDSETS", output);
                int code = connection.getResponseCode();
                Log.d("Code", code + "");
                WordSet[] sets = gson.fromJson(output, WordSet[].class);
                return Arrays.asList(sets);
            } catch (Exception e) {
                Log.d("getAllSetsAsyncError", e.toString());
            }
            return null;
        }


        protected void onPostExecute(List<WordSet> result) {
            System.out.println("Result is here:-" + result);
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

    public static class getWordSetAsync extends AsyncTask<Long, List<WordSet>, WordSet> {

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
        }

        @Override
        protected WordSet doInBackground(Long... params) {
            try {
                HttpURLConnection connection = getConnection(ApiPaths.SETS + "/"
                        + ApiPaths.PARAM_ID + "/" + params[0]);
                Gson gson = getJsonWriterWithCustomDate();
                String output = readFromConnection(connection);
                WordSet set = gson.fromJson(output, WordSet.class);
                return set;
            } catch (Exception e) {

            }
            return null;
        }


        protected void onPostExecute(List<WordSet> result) {
            System.out.println("Result is here:-" + result);
        }
    }

    public static class searchAsync extends AsyncTask<String, List<WordSet>, List<WordSet>> {

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
        }

        @Override
        protected List<WordSet> doInBackground(String... params) {
            String keyword = params[0];
            try {
                keyword = URLEncoder.encode(keyword, "UTF-8");
                HttpURLConnection connection = getConnection("search/" + keyword);
                Gson gson = getJsonWriterWithCustomDate();
                String output = readFromConnection(connection);
                WordSet[] sets = gson.fromJson(output, WordSet[].class);
                return Arrays.asList(sets);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(List<WordSet> result) {
            System.out.println("Result is here:-" + result);
        }

    }


    public static class createSetAsync extends AsyncTask<WordSet, String, String> {
        ProgressDialog progress;
        Context context;

        public createSetAsync(Context context) {
            this.context = context;
            progress = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
            progress.setMessage("Creating Set :)");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected String doInBackground(WordSet... params) {
            WordSet set = params[0];
            int code = 0;
            try {


                URL url = new URL("http://46.101.37.183:8080/crowdspell-web/api/v1/" + ApiPaths.SETS);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                // con.setRequestProperty("app-id", "4b08dee3-c8ec-40a0-99d1-4aee47c0772a");
                con.addRequestProperty("app-id", "4b08dee3-c8ec-40a0-99d1-4aee47c0772a");
                con.connect();

                Gson gson = getJsonWriterWithCustomDate();


                JsonElement je = gson.toJsonTree(set);
                JsonObject jo = new JsonObject();
                jo.add("person", je);
                StringBuilder sb = new StringBuilder();
                String json = gson.toJson(set);
                sb.append(json);
                sb.insert(1, " \"type\": \"wordSet\", ");
                json = sb.toString();
                System.out.println("Json-" + json);
                writeToOutputStream(con, json);
                code = con.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return String.valueOf(code);
        }


        protected void onPostExecute(String result) {
            System.out.println("Result is here:-" + result);
            progress.dismiss();
            Toast.makeText(context, "Your Set is created.!", Toast.LENGTH_LONG).show();


        }

    }


    public static class submitScoreAsync extends AsyncTask<SetScoreCarrier, List<UserPoints>, Integer> {
        ProgressDialog progress;

        public submitScoreAsync(Context context) {
            progress = new ProgressDialog(context);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setTitle("Submiting Score!");
        }

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
            progress.show();
            progress.setCancelable(false);
        }

        @Override
        protected Integer doInBackground(SetScoreCarrier... params) {
            int status = 0;
            String finalString;
            SetScoreCarrier ssc = params[0];
            String result = null;
            int code = 0;
            BufferedReader rdr = null;
            Gson gson = new Gson();
            result = gson.toJson(ssc);
            try {
                URL url = new URL("http://46.101.37.183:8080/crowdspell-web/api/v1/" + ApiPaths.SCORES);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                // con.setRequestProperty("app-id", "4b08dee3-c8ec-40a0-99d1-4aee47c0772a");
                con.addRequestProperty("app-id", "4b08dee3-c8ec-40a0-99d1-4aee47c0772a");
                con.connect();

                StringBuilder sb = new StringBuilder();


                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(result);
                wr.flush();
                wr.close();
                status = con.getResponseCode();
                System.out.println(" received " + status);

                rdr = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;

                while ((line = rdr.readLine()) != null) {
                    sb.append(line);
                }
                finalString = sb.toString();

                code = status;
                System.out.println(result);
                if (status == R.string.EXISTING_RESOURCE) {
                    System.out.println("Status code=" + 409);
                    return (status);
                    //throw new NetworkException("Sorry! that username is already taken.");
                } else if (status == R.string.BAD_REQUEST) {
                    System.out.println("Status code=" + 400);
                    return (status);
                    //   throw new NetworkException("The username or password did not match.");
                } else if (status == R.string.OK) {
                    System.out.println("Status code=" + 200);
                    return 0;
                }
            } catch (Exception e) {
                finalString = e.toString();
                System.out.println("Exception Aa gaya-" + e.toString());
            } finally {
                if((rdr != null))
                    try {
                        rdr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            return code;
        }


        protected void onPostExecute(int result) {
            System.out.println("Result is here:-" + result);
            progress.dismiss();
        }

    }


    public static List<UserPoints> getAllTopScorers() throws IOException {
        HttpURLConnection connection = getConnection(ApiPaths.SCORES);
        String content = null;
        content = readFromConnection(connection);
        Gson gson = getJsonWriterWithCustomDate();
        UserPoints[] userPoints = gson.fromJson(content, UserPoints[].class);
        return Arrays.asList(userPoints);

    }


    public static class getAllTopScoresAsync extends AsyncTask<String, List<UserPoints>, List<UserPoints>> {

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


        protected void onPostExecute(String result) {
            System.out.println("Result is here:-" + result);

        }

    }

    public static void main(String[] args) throws IOException {
        List<UserPoints> points = getAllTopScorers();
        for (UserPoints point : points) {
            LOG.fine(point.getUser().getUsername() + " - " + point.getPoints());
        }
    }
}

