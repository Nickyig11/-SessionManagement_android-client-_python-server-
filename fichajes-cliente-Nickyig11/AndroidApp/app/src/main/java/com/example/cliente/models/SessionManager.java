package com.example.cliente.models;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private static final String PREF_NAME = "ControlPresenciaPref";
    private static final String KEY_USERNAME = "username";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(String username){
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public String getUsername(){
        return pref.getString(KEY_USERNAME, null);
    }

    public boolean isLoggedIn(){
        return pref.getString(KEY_USERNAME, null) != null;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
    }
}