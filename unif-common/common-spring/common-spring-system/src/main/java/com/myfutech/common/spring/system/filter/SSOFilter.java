package com.myfutech.common.spring.system.filter;

import com.alibaba.fastjson.JSON;
import com.myfutech.common.spring.system.store.IAuthStore;
import com.myfutech.common.spring.system.util.AuthUserUtils;
import com.myfutech.common.util.Responses;
import com.myfutech.common.util.constant.AuthInfo;
import com.myfutech.common.util.constant.HttpHeader;
import com.myfutech.common.util.vo.auth.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * 单点登陆
 */
public class SSOFilter extends OncePerRequestFilter {
	
	private static final Logger log = LoggerFactory.getLogger(SSOFilter.class);

	protected static final String LOGIN_URL = "/login";

	protected static final String LOGOUT_URL = "/logout";

	protected String defaultLoginUrl;
	protected String defaultLogoutUrl;
	protected Long loginTimeOut;
	protected IAuthStore<UserInfo> authStore;
	protected Set<String> skipUrl;

	public SSOFilter(String defaultLoginUrl, String defaultLogoutUrl, IAuthStore<UserInfo> authStore) {
		this(defaultLoginUrl, defaultLogoutUrl, authStore, null);
	}

	public SSOFilter(String defaultLoginUrl, String defaultLogoutUrl, IAuthStore<UserInfo> authStore, Set<String> skipUrl) {
		this(defaultLoginUrl, defaultLogoutUrl, authStore, skipUrl, 1800L);
	}

	public SSOFilter(String defaultLoginUrl, String defaultLogoutUrl, IAuthStore<UserInfo> authStore, Set<String> skipUrl, Long loginTimeOut) {
		Assert.hasText(defaultLoginUrl, "默认登陆页面不能为空");
		Assert.hasText(defaultLogoutUrl, "默认登出页面不能为空");
		Assert.notNull(authStore, "鉴权信息存取对象不能为空");
		this.defaultLoginUrl = defaultLoginUrl;
		this.defaultLogoutUrl = defaultLogoutUrl;
		this.authStore = authStore;
		this.skipUrl = skipUrl;
		this.loginTimeOut = loginTimeOut;
		initDefaultSkipUrl();
	}

	private void initDefaultSkipUrl(){
		if (this.skipUrl == null){
			this.skipUrl = new HashSet<>(2);
		}
		this.skipUrl.add("/");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String uri = request.getRequestURI();
		//静态资源 跳过
		if (uri.contains(".")
				//设置的跳过资源
				|| skipUrl.contains(uri)){
			filterChain.doFilter(request, response);
			return;
		}

		String requestType = request.getHeader(HttpHeader.AJAX_KEY);
		String authCode = request.getHeader(HttpHeader.UNIF_AUTH_KEY);
		if (authCode == null){
			authCode = request.getHeader(HttpHeader.UNIF_AUTH_KEY.toLowerCase());
		}
		// 如果是get请求，则从请求参数中取鉴权参数
		if (authCode == null){
			authCode = request.getParameter(HttpHeader.UNIF_AUTH_KEY);
		}

		UserInfo userInfo = null;
		boolean needToLogin = false;
		Boolean squeezeOut = false;

		if (LOGIN_URL.equals(uri) || StringUtils.isBlank(authCode)){
			needToLogin = true;
		}else {
			userInfo = authStore.getAttach(authCode, AuthInfo.AUTH_REDIS_USER_INFO, UserInfo.class);
			if (userInfo == null){
				needToLogin = true;
				squeezeOut = authStore.getAttach(authCode, AuthInfo.AUTH_REDIS_SQUEEZZ_OUT, Boolean.class);
				authStore.remove(authCode);
			}
		}

		//登入跳转
		if (needToLogin){
			squeezeOut = (squeezeOut != null && squeezeOut);
			dealLogin(request, response, uri, requestType, squeezeOut);
			return;
		}

		//登出跳转
		if (LOGOUT_URL.equals(uri)){
			authStore.remove(authCode);
			dealLogout(request, response, uri, requestType);
			return;
		}
		MDC.put("employeeCode", userInfo.getEmployeeCode());
		AuthUserUtils.init(userInfo);
		authStore.expire(authCode, this.loginTimeOut);
		filterChain.doFilter(request, response);
		AuthUserUtils.clean();
	}

	protected void dealLogout(HttpServletRequest request, HttpServletResponse response, String uri, String requestType) throws IOException {
		String logoutUrlInfo = request.getParameter("defaultLogoutUrl");
		if (StringUtils.isEmpty(logoutUrlInfo)){
			logoutUrlInfo = defaultLogoutUrl;
		}
		//判断是否为ajax请求
		if(HttpHeader.AJAX_VALUE.equals(requestType)){
			PrintWriter out = response.getWriter();
			response.setStatus(HttpStatus.OK.value());
			out.print(JSON.toJSONString(Responses.success(logoutUrlInfo)));
			out.close();
			log.info("[logout] {} ajax to {}", uri, logoutUrlInfo);
		}else {
			response.sendRedirect(logoutUrlInfo);
			log.info("[logout] {} Redirect to {}", uri, logoutUrlInfo);
		}
	}

	protected void dealLogin(HttpServletRequest request, HttpServletResponse response, String uri, String requestType, Boolean squeezeOut) throws IOException {
		//判断是否为ajax请求
		if(HttpHeader.AJAX_VALUE.equals(requestType)){
			PrintWriter out = response.getWriter();
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setStatus(HttpStatus.FORBIDDEN.value());
			out.print("{\"code\": 302,\"url\": \""+ defaultLoginUrl +"\",\"squeezeOut\": "+ squeezeOut +"}");
			out.close();
			log.info("[login] {} ajax to {} squeezeOut {} ", uri, defaultLoginUrl, squeezeOut);
		}else {
			log.info("[login] {} Redirect to {} squeezeOut {} ", uri, defaultLoginUrl, squeezeOut);
			response.sendRedirect(defaultLoginUrl+"?squeezeOut=" + squeezeOut + "&successUrl=" + request.getRequestURL());
		}
	}
}
