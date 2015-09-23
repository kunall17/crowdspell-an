package com.igl.crowdword.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.igl.crowdword.R;
import com.igl.crowdword.fxns.User;
import com.igl.crowdword.fxns.UserDetails;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by MAHE on 9/6/2015.
 */
public class UserFunctions {

    public static boolean checkForServer(Context context) {
        Boolean asd = false;
        if (context.getResources().getString(R.string.SERVER_ADDRESS) == context.getResources().getString(R.string.SERVER_ADDRESS1)) { //Remove this
            asd = true;
        }
        return asd;
    }

    public Boolean checkInternetConnection(Context context){
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            return false;
        }
return false;
    }
    //TODO getCurrent User
    public User getCurrentUser(Context context) {
        User us = new User();
        SharedPreferences sp = getShared(context);
        SharedPreferences.Editor spe = sp.edit();
        Long asd = sp.getLong("id", 0);
        us.setUsername(sp.getString("username", ""));
        us.setPassword(sp.getString("password", ""));
        us.setAuthenticationProvider(sp.getString("authProvider", ""));
        us.setSalt(sp.getString("salt", ""));
        us.setToken(sp.getString("token", ""));
        us.setId(asd);
        us.setJoiningDate(java.sql.Date.valueOf(sp.getString("joiningDate", "")));

        UserDetails usd = new UserDetails();

        usd.setEmail(sp.getString("email", ""));
        usd.setPlatform(sp.getString("platform", ""));
        us.setDetails(usd);
        return us;
    }

    //TODO getCurrentToken
    public String getCurrentToken(Context context) {
        User us = new User();
        SharedPreferences sp = getShared(context);
        return sp.getString("token", "");
    }

    public Date getCurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());

        return (Date.valueOf(c.getTime().toString()));
    }

    public void saveToSharedPreferences(User user, Context context) {
        SharedPreferences sp = getShared(context);
        SharedPreferences.Editor spe = sp.edit();

        spe.putString("username", user.getUsername());
        spe.putString("password", user.getPassword());
        spe.putString("authProvider", user.getAuthenticationProvider());
        spe.putString("salt", user.getSalt());
        spe.putString("token", user.getToken());
        spe.putLong("id", user.getId());
        spe.putString("joiningDate", user.getJoiningDate().toString());

        //Insert UserDetails
        spe.putString("email", user.getDetails().getEmail());
        spe.putString("platform", user.getDetails().getPlatform());
        spe.commit();
    }

    public Boolean checkIfSharedPreferencesforUserExists(Context context) {
        Boolean asd = false;
        SharedPreferences sp = getShared(context);
        if (!sp.contains(context.getResources().getString(R.string.SP_APPID).toString())) {
            asd = true;
            //Indicate that the default shared prefs have been set
        }
        return asd;
    }

    public SharedPreferences getShared(Context context) {

        SharedPreferences sp = context.getSharedPreferences(context.getResources().getString(R.string.SP_USERCREDENTIALS), context.MODE_PRIVATE);
        return sp;
    }

    public String getUserKey(Context context) {
        SharedPreferences sp = getShared(context);
        return sp.getString("token", null);
    }

    public Boolean checkIfGuestModeIsOn(Context context) {
        Boolean asd = false;
        SharedPreferences spe = getShared(context);
        String d = spe.getString("id", "");
        if (d == "guestmode") {
            asd = true;
        }
        return asd;
    }

    public void saveAsGuest(Context context) {
        User user = new User();
        user.setId(Long.valueOf("guestmode"));
        user.setJoiningDate(getCurrentDate());
        saveToSharedPreferences(user, context);
    }
}
