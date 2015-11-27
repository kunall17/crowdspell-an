package com.lambda.crowdspell;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lambda.crowdspell.DbRequest.dbMethodsAdapter;
import com.lambda.crowdspell.HTTPRequest.ApiPaths;
import com.lambda.crowdspell.HTTPRequest.UserManager;
import com.igl.crowdword.R;
import com.lambda.crowdspell.core.UserFunctions;
import com.lambda.crowdspell.fxns.UserDetails;
import com.lambda.crowdspell.fxns.User;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class newUserActivity extends ActionBarActivity {
    //TODO Make UI BETTER
    EditText un_et;
    EditText pwd_et;
    EditText mail_et;
    dbMethodsAdapter dbFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        un_et = (EditText) findViewById(R.id.userName_ET);
        pwd_et = (EditText) findViewById(R.id.password_ET);
        mail_et = (EditText) findViewById(R.id.email_ET);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_user, menu);
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

    public void createNewUser_Click(View v) {
        UserFunctions.checkInternetConnectionAsync checkInternet = new UserFunctions.checkInternetConnectionAsync();
        try {
            if (checkInternet.execute(getBaseContext()).get() == false) {
                Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Date d = new Date();

        User newUser = new User();
        UserDetails newUserDetails = new UserDetails();

        newUser.setId(Long.valueOf(123));
        newUser.setUsername(un_et.getText().toString());
        newUser.setPassword(pwd_et.getText().toString());
        newUserDetails.setEmail(mail_et.getText().toString());
        newUser.setJoiningDate(d);
        newUserDetails.setPlatform(String.valueOf(R.string.PLATFORM_ANDROID));
        newUser.setDetails(newUserDetails);
        String token = "";

        createUserAsync cua = new createUserAsync();
        cua.execute(newUser);
    }


    public class createUserAsync extends AsyncTask<User, String, String> {
        User usernew;
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
//            updateDisplay("Starting task");
            progress = new ProgressDialog(newUserActivity.this);
            progress.setMessage("Creating User");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
            progress.setCancelable(false);

        }

        @Override
        protected String doInBackground(User... params) {

            URL url = null;
            try {
                url = new URL("http://46.101.37.183:8080/crowdspell-web/api/v1/" + ApiPaths.USERS);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                // con.setRequestProperty("app-id", "4b08dee3-c8ec-40a0-99d1-4aee47c0772a");
                con.addRequestProperty("app-id", "4b08dee3-c8ec-40a0-99d1-4aee47c0772a");
                con.connect();
                Gson gson = new Gson();
                String userString = gson.toJson(params[0]);
                String asd = "";
                usernew = params[0];
                writeToOutputStream(con, userString);
                asd = readFromConnection(con);
                return asd;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(String result) {
            if (result != null) {
                Toast.makeText(newUserActivity.this, "User Created", Toast.LENGTH_LONG).show();
                new UserFunctions().saveToSharedPreferences(usernew, newUserActivity.this);
                Intent in1 = new Intent(newUserActivity.this, LoginActivity.class);
                startActivity(in1);
            } else {
                Toast.makeText(newUserActivity.this, "Error Occured", Toast.LENGTH_LONG).show();
            }

        }

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


    public void cancel_user_btn(View v) {
        finish();
    }
}



