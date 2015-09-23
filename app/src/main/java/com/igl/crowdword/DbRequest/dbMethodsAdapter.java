package com.igl.crowdword.DbRequest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.igl.crowdword.ManyFunctions;
import com.igl.crowdword.fxns.User;
import com.igl.crowdword.fxns.UserDetails;

/**
 * Created by Kunal on 7/12/2015.
 */
public class dbMethodsAdapter {
    dbMethods dbmethods;


    public dbMethodsAdapter(Context context) {
        dbmethods = new dbMethods(context);
    }


    public void insertUseruser(User User, UserDetails ud) {

        ContentValues cv = new ContentValues();
        SQLiteDatabase sql = dbmethods.getWritableDatabase();

        cv.put(dbMethods.TABLE_user_username, User.getUsername());
        cv.put(dbMethods.TABLE_user_password, User.getPassword());
        cv.put(dbMethods.TABLE_user_authProvider, User.getAuthenticationProvider());
        cv.put(dbMethods.TABLE_user_salt, User.getSalt());
        cv.put(dbMethods.TABLE_user_token, User.getToken());
        cv.put(dbMethods.TABLE_user_ID, User.getId());
        cv.put(dbMethods.TABLE_user_joiningDate, User.getJoiningDate().toString());

        //Insert UserDetails
        cv.put(dbMethods.TABLE_user_email, ud.getEmail());
        cv.put(dbMethods.TABLE_user_platform, ud.getPlatform());

        sql.insert(dbMethods.TABLE_user, null, cv);

    }

    public boolean checkIfUserHasCorrectCredentials(String username, String password) {

        SQLiteDatabase sql = dbmethods.getWritableDatabase();

        String[] columns = {dbMethods.TABLE_user_username, dbMethods.TABLE_user_password};
        Cursor cursor = sql.query(dbMethods.TABLE_user, columns, dbMethods.TABLE_user_username + "=" + username, null, null, null, null);
        if (cursor.getString(cursor.getColumnIndex(dbMethods.TABLE_user_password)) == password) {
            return true;
        } else return false;

    }

    class dbMethods extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 6;
        private static final String DATABASE_NAME = "db_crowdspell";
        private static final String TABLE_user = "crowdspell_user";
        //Columns for User Table
        private static final String TABLE_user_username = "username";
        private static final String TABLE_user_password = "password";
        private static final String TABLE_user_email = "email";
        private static final String TABLE_user_authProvider = "authProvider";
        private static final String TABLE_user_salt = "salt";
        private static final String TABLE_user_token = "token";
        private static final String TABLE_user_joiningDate = "joiningDate";
        private static final String TABLE_user_ID = "id";
        private static final String TABLE_user_platform = "platform";


        Context context;


        public dbMethods(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //Create table

            String query_exe = "CREATE TABLE crowdspell_user (" + TABLE_user_ID + " INT NOT NULL ," + TABLE_user_username + " varchar(255) ," + TABLE_user_password + " varchar(255) ," + TABLE_user_email + "  varchar(255)," + TABLE_user_authProvider + " varchar(255) ," + TABLE_user_salt + " varchar(255)," + TABLE_user_token + " varchar(255)," + TABLE_user_joiningDate + " varchar(255)," + TABLE_user_platform + " varchar(255), PRIMARY KEY(id)  );";

            db.execSQL(query_exe);
            ManyFunctions.messageShow("Database Creatued", context);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE " + " IF EXISTS " + TABLE_user);
            ManyFunctions.messageShow("Update Called", context);
            onCreate(db);
        }


    }
}
