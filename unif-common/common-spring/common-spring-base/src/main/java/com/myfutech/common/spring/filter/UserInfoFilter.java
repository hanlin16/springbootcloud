package com.myfutech.common.spring.filter;

import com.alibaba.fastjson.JSON;
import com.myfutech.common.spring.util.UserInfoUtil;
import com.myfutech.common.util.constant.HttpHeader;
import com.myfutech.common.util.vo.auth.LoginUserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * 服务内初始化用户信息
 */
public class UserInfoFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(UserInfoFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LoginUserVO vo = null;
        String userInfo = request.getHeader(HttpHeader.USER_INFO_KEY);
        if (StringUtils.isNotBlank(userInfo)) {
            try {
                vo = JSON.parseObject(URLDecoder.decode(userInfo,"UTF-8"), LoginUserVO.class);
            } catch (Exception e) {
                log.warn("请求 " + request.getRequestURI() + ", 解析用户信息" + userInfo + "失败");
            }
        }
        UserInfoUtil.init(vo);
        MDC.put("employeeCode", vo == null ? "    " : vo.getEmployeeCode());
        filterChain.doFilter(request, response);
        UserInfoUtil.clean();
    }
}
