package com.myfutech.common.spring.system.interceptor;

import com.alibaba.fastjson.JSON;
import com.myfutech.common.spring.system.util.AuthUserUtils;
import com.myfutech.common.util.Responses;
import com.myfutech.common.util.enums.ResponseCode;
import com.myfutech.common.util.vo.auth.Auth;
import com.myfutech.common.util.vo.auth.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }

        HandlerMethod handlerMethod = (HandlerMethod)handler;

        Object o = handlerMethod.getBean();
        if (o instanceof BasicErrorController){
            return super.preHandle(request, response, handler);
        }

        UserInfo userInfo = AuthUserUtils.getUserInfo();
        Set<String> resourceSet = userInfo.getResourceCodeSet();

        Auth t = handlerMethod.getMethodAnnotation(Auth.class);
        if (t == null || containsAny(resourceSet, t.value())){
            log.info("[{}] 用户 {} 请求接口 {}", userInfo.getId(), userInfo.getRealName(), request.getServletPath());
            return super.preHandle(request, response, handler);
        }

        log.warn("[{}] 用户 {} 请求接口 {}({}),但没有对应权限", userInfo.getId(), userInfo.getRealName(), request.getServletPath(), t.value());
        writeInfo(response, ResponseCode.ERROR_CODE, "您没有权限进行此操作，如果需要请联系管理人员");
        return false;
    }

    private boolean containsAny(Set<String> resourceSet, String[] codes){
        if (codes == null || codes.length == 0){
            return true;
        }

        for (String code : codes) {
            if (resourceSet.contains(code)){
                return true;
            }
        }

        return false;
    }

    private void writeInfo(HttpServletResponse response, ResponseCode code, String msg) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Responses result = Responses.error(code, msg);
        PrintWriter printWriter = response.getWriter();
        printWriter.write(JSON.toJSONString(result));
        printWriter.flush();
        printWriter.close();
    }
}
