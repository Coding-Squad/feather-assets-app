package com.reminisense.fa.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.reminisense.fa.R;
import com.reminisense.fa.models.LoginResult;

/**
 * Created by USER on 5/24/2016.
 */
public class CacheManager {

    private static final String KEY_LOGIN_RESULT = "loginResult";

    public static void storeLoginResult (Context context, LoginResult loginResult) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginResult);
        editor.putString(KEY_LOGIN_RESULT, json);

        editor.apply();
    }

    public static LoginResult retrieveLoginResult(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_key), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_LOGIN_RESULT, null);
        if ( json != null ) {
            LoginResult loginResult = gson.fromJson(json, LoginResult.class);
            return loginResult;
        } else {
            return null;
        }
    }

    public static String retrieveAuthToken(Context context) {
        LoginResult loginResult = retrieveLoginResult(context);
        return loginResult.getAuthenticationToken();
    }

    public static int retrieveCompanyId(Context context) {
        LoginResult loginResult = retrieveLoginResult(context);
        return loginResult.getCompanyId();
    }


}
