package com.lambda.crowdspell;

import android.content.Context;
import android.widget.Toast;

import com.lambda.crowdspell.fxns.User;
import com.lambda.crowdspell.fxns.UserDetails;

/**
 * Created by Kunal on 7/12/2015.
 */
public class ManyFunctions {

    public static void savetoDB (User d,UserDetails ud){

    }


    public static void messageShow(String Message, Context context){
        Toast.makeText(context,Message,Toast.LENGTH_LONG).show();
    }


}
