package com.lambda.crowdspell.HTTPRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.igl.crowdword.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kunal on 7/16/2015.
 */
public class NetworkManager {


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