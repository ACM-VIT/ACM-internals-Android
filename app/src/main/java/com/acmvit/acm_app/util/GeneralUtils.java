package com.acmvit.acm_app.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Calendar;

public class GeneralUtils {

    public static int generateUniqueId() {
        Calendar calendar = Calendar.getInstance();
        return (int) (calendar.getTimeInMillis() / 1000);
    }

    public static void hideKeyboard(Context context, View currentFocus){
        if (currentFocus != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(
                    Context.INPUT_METHOD_SERVICE
            );
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }
}
