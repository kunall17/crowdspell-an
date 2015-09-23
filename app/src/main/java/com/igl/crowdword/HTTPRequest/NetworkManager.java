package com.igl.crowdword.HTTPRequest;

import android.content.Loader;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.igl.crowdword.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Entity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ResponseCache;
import java.net.URL;

/**
 * Created by Kunal on 7/16/2015.
 */
public class NetworkManager {


    private static HttpURLConnection getBaseConnection(String extension)
            throws IOException {
        // Change URL here.

        String urla = String.valueOf(R.string.SERVER_ADDRESS).toString();
        URL url = new URL(urla
                + extension);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty(ApiPaths.APP_AUTH_KEY,
                ApiPaths.ANDROID_APP_KEY);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        return connection;
    }

    protected static HttpURLConnection getPostConnection(String path)
            throws IOException {
        HttpURLConnection connection = getBaseConnection(path);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type",
                "application/json; charset=UTF-8");
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

    protected static Gson getJsonWriterWithCustomDate(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        return gson;
    }
}