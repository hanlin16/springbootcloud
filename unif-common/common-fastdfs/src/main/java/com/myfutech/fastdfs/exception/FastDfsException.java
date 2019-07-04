package com.myfutech.fastdfs.exception;

public class FastDfsException extends RuntimeException{

    public FastDfsException() {
    }

    public FastDfsException(String message) {
        super(message);
    }

    public FastDfsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FastDfsException(Throwable cause) {
        super(cause);
    }

    public FastDfsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
