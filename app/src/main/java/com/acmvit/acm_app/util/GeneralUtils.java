package com.acmvit.acm_app.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class GeneralUtils {

    public static int generateUniqueId() {
        Calendar calendar = Calendar.getInstance();
        return (int) (calendar.getTimeInMillis() / 1000);
    }

    public static void hideKeyboard(Context context, View currentFocus) {
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE
            );
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    public static boolean unBoxNullableBoolean(@Nullable Boolean b) {
        if (b == null) return false;
        return b;
    }

    public static String getFirstNChars(String s, int n){
        if (s == null) return "";

        if (s.length() < n) {
            return s;
        }
        return s.substring(0, n-1);
    }

    public static boolean nullableEquals(Object o1, Object o2) {
        if (o1 == o2) return true;
        if (o1 == null) return false;
        return o1.equals(o2);
    }

}
