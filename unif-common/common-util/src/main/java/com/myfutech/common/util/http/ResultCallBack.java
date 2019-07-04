package com.myfutech.common.util.http;

import com.myfutech.common.util.exception.HttpClientException;

public interface ResultCallBack {

    /**
     * 请求失败，默认抛出{@link HttpClientException}
     *
     * @param url   请求url
     * @param e     异常
     */
    default void onRequestFailure(String url, Exception e){
        throw new HttpClientException("执行请求[" + url + "]失败", e);
    }

    /**
     * 响应失败，默认抛出{@link HttpClientException}
     *
     * @param url           请求url
     * @param code          响应码
     * @param responseBody  响应体
     */
    default void onResponseFailure(String url, int code, String responseBody){
        throw new HttpClientException("执行请求[" + url + "]失败");
    }

    /**
     *  响应成功
     *
     * @param url               请求url
     * @param responseBody      响应体
     */
    void onSuccess(String url, String responseBody);
}
