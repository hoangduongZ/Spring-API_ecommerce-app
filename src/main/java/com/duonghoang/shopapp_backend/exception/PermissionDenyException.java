package com.duonghoang.shopapp_backend.exception;

public class PermissionDenyException extends RuntimeException{
    public PermissionDenyException(String msg){
        super(msg);
    }
}
