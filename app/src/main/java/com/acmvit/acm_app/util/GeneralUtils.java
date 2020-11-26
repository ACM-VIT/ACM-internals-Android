package com.acmvit.acm_app.util;

import java.util.Calendar;

public class GeneralUtils {

    public static int generateUniqueId(){
        Calendar calendar = Calendar.getInstance();
        return (int) (calendar.getTimeInMillis()/1000);
    }

}
