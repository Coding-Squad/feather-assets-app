package com.reminisense.fa.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by USER on 5/24/2016.
 */
public class TokenSaver {
    private final static String SHARED_PREF_NAME = "com.reminisense.Feather_Assets";
    private final static String TOKEN_KEY = "com.reminisense.TOKEN_KEY";

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN_KEY, "");
    }

    public static void setToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }
}
