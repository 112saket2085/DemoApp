package com.example.demoapplication.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverterManager {

    private static DateConverterManager instance;
    private final String INPUT_DATE_FORMAT = "dd MM yyyy";
    private final String OUTPUT_DATE_FORMAT = "dd MMM yyyy";
    private final SimpleDateFormat inputDateFormat = new SimpleDateFormat(INPUT_DATE_FORMAT, Locale.ENGLISH);
    private final SimpleDateFormat outputDateFormat = new SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale.ENGLISH);

    public static DateConverterManager getInstance() {
        if(instance==null) {
            instance = new DateConverterManager();
        }
        return instance;
    }

    public String getInputDateString(Date date) {
        try {
            return inputDateFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public String getOutputDateString(String inputDate) {
        try {
            Date date = inputDateFormat.parse(inputDate);
            return outputDateFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public String getTaskDateString(String inputDate) {
        try {
            Date date = inputDateFormat.parse(inputDate);
            SimpleDateFormat taskDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            return taskDateFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }





}
