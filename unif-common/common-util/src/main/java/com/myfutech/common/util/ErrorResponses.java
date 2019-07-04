package com.myfutech.common.util;

import com.alibaba.fastjson.JSON;

public class ErrorResponses {

    public static ErrorResponses newErrorResponses(Responses responses, Throwable throwable){
        return new ErrorResponses(responses, throwable);
    }

    private Responses responses;
    private Throwable throwable;

    public ErrorResponses() {
    }

    public ErrorResponses(Responses responses, Throwable throwable) {
        this.responses = responses;
        this.throwable = throwable;
    }

    public Responses getResponses() {
        return responses;
    }

    public void setResponses(Responses responses) {
        this.responses = responses;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public static void main(String[] args) {
        String msg = JSON.toJSONString(new RuntimeException("异常", new ArithmeticException("test")));
        System.out.println("msg = " + msg);
        Throwable exception = JSON.parseObject(msg, Throwable.class);
        exception.printStackTrace();
    }
}
