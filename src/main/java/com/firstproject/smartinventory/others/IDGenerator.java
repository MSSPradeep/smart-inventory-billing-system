package com.firstproject.smartinventory.others;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.UUID;

public class IDGenerator {
    public static String idGenerator(String prifix){
        String uniqId = UUID.randomUUID().toString().substring(0,8).toUpperCase();
        int random = (int)(Math.random()*100);
        return prifix+"-"+uniqId;
    }


}
