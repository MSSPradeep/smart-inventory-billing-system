package com.firstproject.smartinventory.exception.notFound;

public class SaleNotFoundException extends RuntimeException{
    public SaleNotFoundException( String message){
        super(message);
    }
}
