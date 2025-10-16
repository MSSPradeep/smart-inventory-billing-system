package com.firstproject.smartinventory.exception.notFound;

public class SaleItemNotFoundException extends RuntimeException{
    public SaleItemNotFoundException(String message){
        super(message);
    }
}
