package com.firstproject.smartinventory.exception.badRequest;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(String message){
        super(message);
    }
}
