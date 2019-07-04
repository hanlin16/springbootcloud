package com.myfutech.common.spring.feign;

import com.alibaba.fastjson.JSON;
import com.myfutech.common.util.ErrorResponses;
import com.myfutech.common.util.exception.RemoteException;
import feign.Request;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Iterator;

@Slf4j
public class FeignExceptionErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        Request request = response.request();
        ErrorResponses responses = null;
        String responseString = null;
        try {
            if (response.body() != null) {
                responseString = IOUtils.toString(response.body().asInputStream());
                if (StringUtils.isNotBlank(responseString)) {
                    responses = JSON.parseObject(responseString, ErrorResponses.class);
                }
            }
        } catch (Exception e) {
            log.error("解析失败请求 "+request.method() + " " + request.url()+" body 异常", e);
        }
        log.error("请求失败，报文如下：\n {}\n\n{}\n", request.toString(), responseToString(response, responseString));
        String msg = request.method() + " " + request.url() + " 请求失败";


        if (responses != null) {
            return new RemoteException(msg, responses.getResponses());
        }
        return new RemoteException(msg);
    }

    private String responseToString(Response response, String responseString){
        StringBuilder builder = (new StringBuilder("HTTP/1.1 ")).append(response.status());
        if (response.reason() != null) {
            builder.append(' ').append(response.reason());
        }

        builder.append('\n');
        Iterator var2 = response.headers().keySet().iterator();
        while(var2.hasNext()) {
            String field = (String)var2.next();
            Iterator var4 = Util.valuesOrEmpty(response.headers(), field).iterator();

            while(var4.hasNext()) {
                String value = (String)var4.next();
                builder.append(field).append(": ").append(value).append('\n');
            }
        }
        builder.append('\n').append(responseString);
        if (response.body() != null) {

        }
        return builder.toString();
    }

}
