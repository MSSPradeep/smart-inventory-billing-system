package com.firstproject.smartinventory.exception.notFound;

public class StoreNotFoundException extends RuntimeException{

    public StoreNotFoundException(String message){
        super(message);
    }
}
