package com.igl.crowdword.HTTPRequest;

import android.content.Context;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.igl.crowdword.R;
import com.igl.crowdword.fxns.User;
import com.igl.crowdword.fxns.WordSet;
import com.igl.crowdword.fxns.analysis.UserFavourites;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Kunal on 7/16/2015.
 */
public class UserManager extends NetworkManager {

    private static Logger LOG = Logger.getLogger(UserManager.class.getName());

    /**
     * @param user
     * @return the authentication token of the user
     * @throws IOException
     */
    public static String login(User user) throws IOException {
        Gson gson = new Gson();
        String userString = gson.toJson(user);
        LOG.fine("User string " + userString);
        HttpURLConnection connection = getPostConnection(ApiPaths.LOGIN);
        writeToOutputStream(connection, userString);
        return readFromConnection(connection);
    }

    /**
     * @param user
     * @return the authentication token of the user
     * @throws IOException
     */
    public static String createUser(User user) throws IOException {
        Gson gson = new Gson();
        String userString = gson.toJson(user);
        HttpURLConnection connection = getPostConnection(ApiPaths.USERS);
        writeToOutputStream(connection, userString);
        return readFromConnection(connection);
    }

    public static void toggleSetVote(long wordSetId, String userToken)
            throws IOException {
    }

    public static class toggleSetVoteAsync extends AsyncTask<UserFavourites, List<WordSet>, Integer> {

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
        }

        @Override
        protected Integer doInBackground(UserFavourites... params) {
            try {
                UserFavourites favourites = params[0];
//                favourites.setSetId(wordSetId);
                //              favourites.setUserToken(userToken);
                Gson gson = new Gson();
                String json = gson.toJson(favourites);
                HttpURLConnection connection = getPostConnection(ApiPaths.FAVOURITES);
                writeToOutputStream(connection, json);
                return connection.getResponseCode();

            } catch (Exception e) {

            }
            return null;
        }

        protected void onPostExecute(Integer result) {
            System.out.println("Result is here:-" + result);
        }
    }

    public static List<WordSet> getUserFavourites(String keyword) {
        try {
            HttpURLConnection connection = getConnection(ApiPaths.FAVOURITES + "/" + keyword);
            String listString = null;
            listString = readFromConnection(connection);

            LOG.fine("list [ " + listString);
            Gson gson = getJsonWriterWithCustomDate();
            WordSet[] favourites = gson.fromJson(listString, WordSet[].class);
            return Arrays.asList(favourites);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class getUserFavouritesAsync extends AsyncTask<String, List<WordSet>, List<WordSet>> {

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
        }

        @Override
        protected List<WordSet> doInBackground(String... params) {
            try {
               URL url = new URL("http://46.101.37.183:8080/crowdspell-web/api/v1/favourites/"+params[0]);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty(ApiPaths.APP_AUTH_KEY,
                        ApiPaths.ANDROID_APP_KEY);
                con.connect();

//                HttpURLConnection connection = getConnection(ApiPaths.FAVOURITES + "/" + params[0]);
                String listString = readFromConnection(con);
                LOG.fine("list [ " + listString);
                Gson gson = getJsonWriterWithCustomDate();
                WordSet[] favourites = gson.fromJson(listString, WordSet[].class);
                return Arrays.asList(favourites);
            } catch (Exception e) {

            }
            return null;
        }


        protected void onPostExecute(List<WordSet> result) {
            System.out.println("Result is here:-" + result);
        }

    }


    public static void main(String[] args) throws IOException {
        // toggleSetVote(2l, "3fa8b2bb-9412-477a-8cc7-85b8e7f5becc");
        List<WordSet> favs = getUserFavourites("3fa8b2bb-9412-477a-8cc7-85b8e7f5becc");
        LOG.fine("Lenght [ " + favs.size() + " ]");
        for (WordSet set : favs) {
            LOG.fine(set.getId() + "  " + set.getName());
        }
    }


    public static class createUserAsync extends AsyncTask<User, String, String> {

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
        }

        @Override
        protected String doInBackground(User... params) {

            Gson gson = new Gson();
            String userString = gson.toJson(params[0]);
            HttpURLConnection connection = null;
            String asd = "";
            try {
                connection = getPostConnection(ApiPaths.USERS);
                writeToOutputStream(connection, userString);
                asd = readFromConnection(connection);
                return asd;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return asd;
        }


        protected void onPostExecute(String result) {
            System.out.println("Result is here:-" + result);
        }

    }


    public static class CheckLogin extends AsyncTask<User, String, String> {

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
        }

        @Override
        protected String doInBackground(User... params) {
            User user = params[0];
            int status = 0;
            String finalString;

            String result = null;
            Gson gson = new Gson();
            result = gson.toJson(user);
            System.out.println("resultant Json=" + result);


            BufferedReader rdr = null;


            try {
                //URL url = new URL(R.string.SERVER_ADDRESS + "" + R.string.SERVER_ADDRESS_LOGIN);
                URL url = new URL("http://46.101.37.183:8080/crowdspell-web/api/v1/login");
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


                System.out.println(result);
                if (status == R.string.EXISTING_RESOURCE) {
                    return String.valueOf(status);
                    //throw new NetworkException("Sorry! that username is already taken.");
                } else if (status == R.string.BAD_REQUEST) {
                    return String.valueOf(status);
                    //   throw new NetworkException("The username or password did not match.");
                } else if (status == R.string.OK) {
                    System.out.println("Status code=" + 200);
                    return finalString;
                }
            } catch (Exception e) {
                finalString = e.toString();
                System.out.println("Exception Aa gaya-" + e.toString());
            } finally {
                if
                        ((rdr != null))

                    try {
                        rdr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            return finalString;
        }

        protected void onPostExecute(String result) {
            System.out.println("Result is here:-" + result);
        }

    }


}
