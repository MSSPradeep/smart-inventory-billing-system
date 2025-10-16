package com.firstproject.smartinventory.exception.badRequest;

public class InvalidInputException extends RuntimeException{
    public InvalidInputException(String message){
        super(message);
    }
}
