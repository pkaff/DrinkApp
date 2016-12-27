package com.example.kines.myapplication;


import java.text.SimpleDateFormat;

/**
 * Created by Kines on 2016-12-27.
 */

public class ConstantDateFormat extends SimpleDateFormat {
    static final private String formatString = "yyyy-MM-dd HH:mm:ss";

    public ConstantDateFormat() {
        super(formatString);
    }
}
