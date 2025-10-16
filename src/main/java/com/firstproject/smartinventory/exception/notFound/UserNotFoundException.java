package com.firstproject.smartinventory.exception.notFound;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException (String message){
        super(message);
    }
}
