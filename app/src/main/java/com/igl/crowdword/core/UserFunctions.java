package com.igl.crowdword.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.igl.crowdword.R;
import com.igl.crowdword.fxns.User;
import com.igl.crowdword.fxns.UserDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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


    public Boolean checkInternetConnection(Context _context) {
        // get Connectivity Manager object to check connection
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

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
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        us.setJoiningDate(java.sql.Date.valueOf(date));

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
        String dateStr = "04/05/2010";
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateObj;
    }

    public String insertBlankValueIfNull(String text) {
        if (text == null) {
            return "";
        }
        return text;
    }

    public void saveToSharedPreferences(User user, Context context) {
        SharedPreferences sp = getShared(context);
        SharedPreferences.Editor spe = sp.edit();

        try {
            spe.putString("username", insertBlankValueIfNull(user.getUsername()));
            spe.putString("password", insertBlankValueIfNull(user.getPassword()));
            spe.putString("authProvider", insertBlankValueIfNull(user.getAuthenticationProvider()));
            spe.putString("salt", insertBlankValueIfNull(user.getSalt()));
            spe.putString("token", insertBlankValueIfNull(user.getToken()));
            spe.putLong("id", Long.valueOf(insertBlankValueIfNull(user.getId().toString())));
            spe.putString("joiningDate", insertBlankValueIfNull(user.getJoiningDate().toString()));
            //Insert UserDetails
            spe.putString("email", insertBlankValueIfNull(user.getDetails().getEmail()));
            spe.putString("platform", insertBlankValueIfNull(user.getDetails().getPlatform()));
            spe.commit();
        } catch (Exception e) {
            Log.d("exception-error", e.toString());

        } finally {

        }
    }

    public Boolean checkIfSharedPreferencesforUserExists(Context context) {
        Boolean asd = false;
        SharedPreferences sp = getShared(context);
        if (sp.contains("username")) {
            asd = true;
            //Indicate that the default shared prefs have been set
        }
        return asd;
    }

    public String getCurrentUsername(Context context) {
        Boolean asd = false;
        SharedPreferences sp = getShared(context);
        if (!sp.contains("username")) {
            return sp.getString("username", "34");  //Indicate that the default shared prefs have been set
        }
        return "something";
    }

    public void deleteSharedPreferences(Context context) {
        SharedPreferences sp = getShared(context);
        sp.edit().clear();
        sp.edit().commit();
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
        Long d = spe.getLong("id", Long.valueOf(0));
        if (d == Long.valueOf(context.getResources().getString(R.string.guestmode))) {
            asd = true;
        }
        return asd;
    }

    public void saveAsGuest(Context context) {
        User user = new User();
        user.setUsername("Guest");

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        user.setJoiningDate(java.sql.Date.valueOf(date));
        UserDetails ud = new UserDetails();
        user.setDetails(ud);
        user.setId(Long.valueOf(context.getResources().getString(R.string.guestmode)));
        user.setJoiningDate(getCurrentDate());
        saveToSharedPreferences(user, context);
    }
}
