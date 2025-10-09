package com.firstproject.smartinventory.others;

public class IDGenerator {
    public static String idGenerator(String prifix){
        String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date());
        int random = (int)(Math.random()*100);
        return prifix+timestamp+random;
    }


}
