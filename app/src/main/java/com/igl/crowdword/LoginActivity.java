package com.igl.crowdword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.igl.crowdword.DbRequest.dbMethodsAdapter;
import com.igl.crowdword.HTTPRequest.UserManager;
import com.igl.crowdword.core.UserFunctions;
import com.igl.crowdword.fxns.UserDetails;
import com.igl.crowdword.fxns.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends ActionBarActivity {

    EditText user_ET;
    EditText pass_ET;
    dbMethodsAdapter dbFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_ET = (EditText) findViewById(R.id.userName_ET);
        pass_ET = (EditText) findViewById(R.id.password_ET);

        UserFunctions asd = new UserFunctions();
        if (asd.checkIfGuestModeIsOn(this) == true) {
            System.out.println("ASD");
            Intent in1 = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(in1);
        } else if (asd.checkIfSharedPreferencesforUserExists(this)) {
            System.out.println("ASD123");
            Intent in1 = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(in1);
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

            User user = new User();
            if (usf.checkIfSharedPreferencesforUserExists(this) == true) {
                user = usf.getCurrentUser(this);
            } else if(user_ET.getText()!= null || pass_ET.getText()!=null) {
                String username = user_ET.getText().toString();
                String password = pass_ET.getText().toString();

                try {

                    String appid = null;
                    user.setUsername(username);
                    user.setPassword(password);

                    appid = UserManager.login(user);
                    user.setToken(appid);
                    usf.saveToSharedPreferences(user, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(user_ET.getText()== null || pass_ET.getText() ==null){
                Toast.makeText(this,"Please Enter Password and UserName!",Toast.LENGTH_LONG).show();
            }
        } else {

        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }}

    public void newUser_click(View v) {
        Intent intent = new Intent(this, newUserActivity.class);
        startActivity(intent);
    }


//    UserManager.GetData();
    //    UserManager.postRequest("http://46.101.37.183:8080/crowdspell-web/api/v1/login",user);
    //    requestData("http://46.101.37.183:8080/crowdspell-web/api/v1/hello");

    public void guest_click(View v) {
        UserFunctions asd = new UserFunctions();
        asd.saveAsGuest(this);

        Intent in1 = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(in1);

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



}