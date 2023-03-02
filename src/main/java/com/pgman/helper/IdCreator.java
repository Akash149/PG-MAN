package com.pgman.helper;

public class IdCreator {
    public static String createId(String role) {
        if(role == "GUEST") {    
            String id = "GUE" + (int) (Math.random()*9000)+1000;
            return id;
        } else if(role == "OWNER") {
            String id = "OWN" + (int) (Math.random()*9000)+1000;
            return id;
        } else {
            return null;
        }
    }
}
