package com.acmvit.acm_app.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;

import com.acmvit.acm_app.model.ListChange;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    public static <T> ListChange<T> getListDiff(List<T> newList, List<T> oldList) {
        List<T> newListCopy = new ArrayList<>(newList);
        List<T> oldListCopy = new ArrayList<>(oldList);

        newListCopy.removeAll(oldList); //Added
        oldListCopy.removeAll(newList); //Removed

        return new ListChange<>(newListCopy, oldListCopy);
    }

}
