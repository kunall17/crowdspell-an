package com.igl.crowdword;

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

import java.io.IOException;
import java.util.Date;

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
        pwd_et= (EditText) findViewById(R.id.password_ET);
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

    public void createNewUser_Click(View v){
        if (new UserFunctions().checkInternetConnection(this) == false) {
            Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_LONG).show();
            return;
        }
        Date d=new Date();

        User newUser = new User();
        UserDetails newUserDetails = new UserDetails();

        newUser.setId(Long.valueOf(123));
        newUser.setUsername(un_et.getText().toString());
        newUser.setPassword(pwd_et.getText().toString());
        newUserDetails.setEmail(mail_et.getText().toString());
        newUser.setJoiningDate(d);
        newUserDetails.setPlatform(String.valueOf(R.string.PLATFORM_ANDROID));
        newUser.setDetails(newUserDetails);

//        try {
//            UserManager.createUserAsync cua = new UserManager.createUserAsync();
//            //TODO     String asd = cua.execute(newUser);
//        }
        }
    }



