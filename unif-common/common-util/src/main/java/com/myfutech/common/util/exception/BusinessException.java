package com.myfutech.common.util.exception;

import com.myfutech.common.util.Responses;
import com.myfutech.common.util.enums.ResponseCode;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException{

    private Responses responses;

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
        this.responses = Responses.error(message);
    }

    public BusinessException(ResponseCode code, String message) {
        super(message);
        this.responses = Responses.error(code, message);
    }

    public BusinessException(Responses responses) {
        super(responses.getMsg());
        this.responses = responses;
    }

    public BusinessException(String message, Responses responses) {
        super(message);
        this.responses = responses;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message, Throwable cause, Responses responses) {
        super(message, cause);
        this.responses = responses;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(Throwable cause, Responses responses) {
        super(cause);
        this.responses = responses;
    }

    public Responses getResponses() {
        return responses;
    }
}
