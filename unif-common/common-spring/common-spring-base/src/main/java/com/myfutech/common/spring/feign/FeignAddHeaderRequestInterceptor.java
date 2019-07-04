package com.myfutech.common.spring.feign;

import com.myfutech.common.util.constant.HttpHeader;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Slf4j
public class FeignAddHeaderRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    if (name.startsWith(HttpHeader.HTTP_HEADER_PREFIX) || name.startsWith(HttpHeader.HTTP_HEADER_PREFIX.toLowerCase())) {
                        String values = request.getHeader(name);
                        template.header(name, values);
                    }
                }
            }
        }
    }
}
