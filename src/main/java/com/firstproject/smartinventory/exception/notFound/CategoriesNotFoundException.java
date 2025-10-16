package com.firstproject.smartinventory.exception.notFound;

public class CategoriesNotFoundException extends RuntimeException{
    public CategoriesNotFoundException(String message){
        super(message);
    }
}
