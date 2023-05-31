package com.pgman.helper;

import java.time.LocalDate;

public class DateConvreter {

    public static LocalDate convert(String date) {
        LocalDate date1 = null;
        try {
            // DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");;
            date1 = LocalDate.parse(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date1;
    }
    
}
