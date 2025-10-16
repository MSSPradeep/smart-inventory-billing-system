package com.firstproject.smartinventory.exception.auth;

public class ForbiddenActionException extends RuntimeException{
    public ForbiddenActionException(String message){
        super(message);
    }
}
