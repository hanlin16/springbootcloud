package com.myfutech.common.util.http;

import com.alibaba.fastjson.JSON;
import com.myfutech.common.util.exception.HttpClientException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpClientUtils {
    /**
     * 超时参数
     */
    private static final int CONNECT_TIME_OUT = 120;
    private static final int READ_TIME_OUT = 60;
    private static final int WRITE_TIME_OUT = 60;
    private static OkHttpClient HTTP_CLIENT;

    static {
        HTTP_CLIENT = new OkHttpClient.Builder()
                //为构建者填充超时时间
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)///连接超时
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                //允许重定向
                .followRedirects(true)
                .connectionPool(new ConnectionPool(300, 5, TimeUnit.MINUTES))
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS,
                        ConnectionSpec.CLEARTEXT))
                .build();
    }

    /**
     * json参数的同步get请求 返回对象
     *
     * @param url    请求URL
     * @param header 请求头
     * @param clazz  返回对象类型
     * @return 返回对象
     */
    public static <E> E doGetReturnJson(String url, Map<String, String> header, Class<E> clazz) {
        String result = doGet(url, header);
        if (StringUtils.isBlank(result)) {
            return null;
        }
        return JSON.parseObject(result, clazz);
    }

    /**
     * json参数的同步Post请求 返回对象
     *
     * @param url         请求URL
     * @param header      请求头
     * @param requestBody 请求报文
     * @param mediaType   请求类型
     * @param clazz       返回对象类型
     * @return 返回对象
     */
    public static <E> E doPostReturnJson(String url, Map<String, String> header, String requestBody,
                                         MediaTypes mediaType, Class<E> clazz) {
        String result = doPost(url, header, requestBody, mediaType);
        if (StringUtils.isBlank(result)) {
            return null;
        }
        return JSON.parseObject(result, clazz);
    }

    /**
     * json参数的同步Post请求
     *
     * @param url     请求url
     * @param jsonStr 请求json
     * @return return message
     */
    public static String doPostWithJson(String url, String jsonStr) {
        return doPost(url, null, jsonStr, MediaTypes.APPLICATION_JSON_UTF8);
    }

    /**
     * xml参数的同步Post请求
     *
     * @param url    请求url
     * @param xmlStr 请求xml
     * @return return message
     */
    public static String doPostWithXml(String url, String xmlStr) {
        return doPost(url, null, xmlStr, MediaTypes.APPLICATION_XML_UTF8);
    }

    /**
     * 同步Get请求
     *
     * @param url    请求url
     * @param header 请求头参数
     * @return 返回报文
     */
    public static String doGet(String url, Map<String, String> header) {
        Request.Builder builder = new Request.Builder().get().url(url);
        if (MapUtils.isNotEmpty(header)) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return execute(url, builder.build());
    }

    /**
     * 同步Get请求
     *
     * @param url    请求url
     * @return 返回报文
     */
    public static byte[] doGetReturnBytes(String url) {
        Request.Builder builder = new Request.Builder().get().url(url);
        return executeBytes(url, builder.build());
    }

    /**
     * 同步Post请求
     *
     * @param url         请求url
     * @param header      请求头参数
     * @param requestBody 请求体参数
     * @param mediaType   请求类型
     * @return 返回报文
     */
    public static String doPost(String url, Map<String, String> header, String requestBody, MediaTypes mediaType) {
        Request.Builder builder = new Request.Builder()
                .post(RequestBody.create(mediaType.getMediaType(), requestBody))
                .url(url);
        if (MapUtils.isNotEmpty(header)) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return execute(url, builder.build());
    }

    /**
     * 异步Get请求
     *
     * @param url      请求url
     * @param header   请求头参数
     * @param callBack 异步通知接口，调用方实现
     */
    public static void doAsyncGet(String url, Map<String, String> header, ResultCallBack callBack) {
        Request.Builder builder = new Request.Builder().get().url(url);
        if (MapUtils.isNotEmpty(header)) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        asyncExecute(callBack, builder.build());
    }

    /**
     * 异步Post请求
     *
     * @param url         请求url
     * @param header      请求头参数
     * @param requestBody 请求体参数
     * @param mediaType   请求类型
     * @param callBack    异步通知接口，调用方实现
     */
    public static void doAsyncPost(String url, Map<String, String> header, String requestBody, MediaTypes mediaType,
                                   ResultCallBack callBack) {
        Request.Builder builder = new Request.Builder()
                .post(RequestBody.create(mediaType.getMediaType(), requestBody))
                .url(url);
        if (MapUtils.isNotEmpty(header)) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        asyncExecute(callBack, builder.build());
    }

    public static String buildUrl(String url, Map<String, String> querys) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(url);
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }

        return sbUrl.toString();
    }

    /**
     * 同步执行http请求
     *
     * @param url     请求url
     * @param request 请求request对象
     * @return String 返回报文
     */
    private static String execute(String url, Request request) {
        log.info("执行请求[{}]",url);
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {

            ResponseBody body = response.body();
            String responseBody = (body == null ? null : body.string());

            if (!response.isSuccessful()) {
                log.error("执行请求[{}]失败, 响应码：{}, 响应体：{}",url, response.code(), responseBody);
                throw new HttpClientException("执行请求[" + url + "]失败");
            }
            return responseBody;
        } catch (IOException e) {
            throw new HttpClientException("执行请求[" + url + "]失败", e);
        }
    }

    /**
     * 同步执行http请求
     *
     * @param url     请求url
     * @param request 请求request对象
     * @return byte[] 返回报文
     */
    private static byte[] executeBytes(String url, Request request) {
        log.info("执行请求[{}]",url);
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {

            ResponseBody body = response.body();
            byte[] bytes = (body == null ? null : body.bytes());

            if (!response.isSuccessful()) {
                log.error("执行请求[{}]失败, 响应码：{}",url, response.code());
                throw new HttpClientException("执行请求[" + url + "]失败");
            }
            return bytes;
        } catch (IOException e) {
            throw new HttpClientException("执行请求[" + url + "]失败", e);
        }
    }

    /**
     * 异步执行http请求
     *
     * @param callBack 异步通知接口，调用方实现
     * @param request  请求request对象
     */
    private static void asyncExecute(ResultCallBack callBack, final Request request) {
        String url = request.url().toString();
        log.info("执行请求[{}]",url);
        HTTP_CLIENT.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onRequestFailure(call.request().url().toString(), e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                String responseBody = (body == null ? null : body.string());

                if (!response.isSuccessful()) {
                    log.error("执行请求[{}]失败, 响应码：{}, 响应体：{}",url, response.code(), responseBody);
                    callBack.onResponseFailure(url,response.code(), responseBody);
                    return;
                }
                log.info("执行请求[{}]成功, 响应码：{}",url, response.code());
                callBack.onSuccess(call.request().url().toString(), responseBody);
            }
        });
    }

}
