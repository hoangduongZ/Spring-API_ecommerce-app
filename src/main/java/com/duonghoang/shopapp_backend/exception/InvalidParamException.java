package com.duonghoang.shopapp_backend.exception;

public class InvalidParamException extends RuntimeException{
    public InvalidParamException(String msg){
        super(msg);
    }
}
