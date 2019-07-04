package com.myfutech.common.util.constant;

/**
 * 请求头 key格式
 * X-Your-Custom-Header
 */
public class HttpHeader {

    public static final String HTTP_HEADER_PREFIX = "Unif-";
    /**
     * 用户信息key
     */
    public static final String USER_INFO_KEY = HTTP_HEADER_PREFIX + "Service-User-Info";
    /**
     *  系统key
     */
    public static final String SUBSYSTEM_KEY = HTTP_HEADER_PREFIX + "Subsystem-Code";
    /**
     *  鉴权key
     */
    public static final String UNIF_AUTH_KEY = HTTP_HEADER_PREFIX + "Auth-Code";

    /**
     *  ajax key
     */
    public static final String AJAX_KEY = "X-Requested-With";
    /**
     *  ajax value
     */
    public static final String AJAX_VALUE = "XMLHttpRequest";

}
