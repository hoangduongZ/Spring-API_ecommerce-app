package com.duonghoang.shopapp_backend.exception;

public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(String msg) {
        super(msg);
    }
}
