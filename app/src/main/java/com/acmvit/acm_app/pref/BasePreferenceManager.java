package com.acmvit.acm_app.pref;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;

public class BasePreferenceManager {
    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "BasePref";
    private static final String IS_FIRST_TIME = "IsFirstTime";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public BasePreferenceManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsFirstTime(boolean isFirstTime){
        editor.putBoolean(IS_FIRST_TIME, isFirstTime);
        editor.commit();
    }

    public boolean getIsFirstTime() {
        return pref.getBoolean(IS_FIRST_TIME, true);
    }
}
