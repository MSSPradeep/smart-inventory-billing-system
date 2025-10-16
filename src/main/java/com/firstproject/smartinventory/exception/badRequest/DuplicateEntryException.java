package com.firstproject.smartinventory.exception.badRequest;

public class DuplicateEntryException extends RuntimeException{
    public DuplicateEntryException(String message){
        super(message);
    }
}
