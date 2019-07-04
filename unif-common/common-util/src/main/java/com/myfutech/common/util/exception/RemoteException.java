package com.myfutech.common.util.exception;

import com.myfutech.common.util.Responses;
import com.myfutech.common.util.enums.ResponseCode;

/**
 * 调用其他服务异常
 */
public class RemoteException extends BusinessException{

    public RemoteException() {
    }

    public RemoteException(String message) {
        super(message);
    }

    public RemoteException(ResponseCode code, String message) {
        super(code, message);
    }

    public RemoteException(Responses responses) {
        super(responses);
    }

    public RemoteException(String message, Responses responses) {
        super(message, responses);
    }

    public RemoteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteException(String message, Throwable cause, Responses responses) {
        super(message, cause, responses);
    }

    public RemoteException(Throwable cause) {
        super(cause);
    }

    public RemoteException(Throwable cause, Responses responses) {
        super(cause, responses);
    }
}
