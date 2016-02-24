package com.example.ekeitho.myapplication;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ekeitho on 2/11/16.
 */
public class Utility {

    public Utility() {

    }

    public String getDateString() {
        return new SimpleDateFormat("EEE, MMM d yyyy").format(Calendar.getInstance().getTime()).toString();
    }
}
