package com.igl.crowdword.HTTPRequest;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.igl.crowdword.core.UserFunctions;
import com.igl.crowdword.fxns.User;
import com.igl.crowdword.fxns.WordSet;
import com.igl.crowdword.fxns.analysis.SetScoreCarrier;
import com.igl.crowdword.fxns.analysis.UserPoints;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
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
                WordSet[] sets = gson.fromJson(output, WordSet[].class);
                return Arrays.asList(sets);
            } catch (Exception e) {

            }
            return null;
        }


        protected void onPostExecute(List<WordSet> result) {
            System.out.println("Result is here:-" + result);
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

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
        }

        @Override
        protected String doInBackground(WordSet... params) {

            UserFunctions us = new UserFunctions();
            WordSet set = params[0];
          //TODO  set.setUserToken(us.getCurrentToken());
            HttpURLConnection connection = null;
            int code = 0;
            try {
                connection = getPostConnection("sets");
                Gson gson = getJsonWriterWithCustomDate();
                writeToOutputStream(connection, gson.toJson(set));
                code = connection.getResponseCode();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return String.valueOf(code);
        }


        protected void onPostExecute(String result) {
            System.out.println("Result is here:-" + result);
        }

    }


    public static class submitScoreAsync extends AsyncTask<SetScoreCarrier, List<UserPoints>, Integer> {

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
        }

        @Override
        protected Integer doInBackground(SetScoreCarrier... params) {

            try {

                SetScoreCarrier carrier = params[0];
                Gson gson = getJsonWriterWithCustomDate();
                String objStr = gson.toJson(carrier);
                HttpURLConnection connection = getPostConnection(ApiPaths.SCORES);
                writeToOutputStream(connection, objStr);
                int code = connection.getResponseCode();
                return code;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }


        protected void onPostExecute(int result) {
            System.out.println("Result is here:-" + result);
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
                HttpURLConnection connection = getConnection(ApiPaths.SCORES);
                String content = null;
                content = readFromConnection(connection);
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

