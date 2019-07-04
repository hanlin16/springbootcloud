package com.myfutech.common.spring.system.filter;

import com.alibaba.fastjson.JSON;
import com.myfutech.common.spring.system.store.IAuthStore;
import com.myfutech.common.spring.system.util.AuthUserUtils;
import com.myfutech.common.util.Responses;
import com.myfutech.common.util.constant.AuthInfo;
import com.myfutech.common.util.constant.HttpHeader;
import com.myfutech.common.util.enums.ResponseCode;
import com.myfutech.common.util.vo.auth.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMethod;
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
 * app登陆
 */
public class AppLoginFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(AppLoginFilter.class);

	protected static final String LOGOUT_URL = "/logout";

	protected IAuthStore<UserInfo> authStore;
	protected Long loginTimeOut;
	protected Set<String> skipUrl;

	public AppLoginFilter(IAuthStore<UserInfo> authStore) {
		this(authStore, null);
	}

	public AppLoginFilter(IAuthStore<UserInfo> authStore, Set<String> skipUrl, Long loginTimeOut) {
		Assert.notNull(authStore, "鉴权信息存取对象不能为空");
		this.authStore = authStore;
		this.skipUrl = skipUrl;
		this.loginTimeOut = loginTimeOut;
		initDefaultSkipUrl();
	}

	public AppLoginFilter(IAuthStore<UserInfo> authStore, Set<String> skipUrl) {
		this(authStore, skipUrl, 1800L);
	}

	private void initDefaultSkipUrl(){
		if (this.skipUrl == null){
			this.skipUrl = new HashSet<>(0);
		}
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

		String authCode = request.getHeader(HttpHeader.UNIF_AUTH_KEY);
		if (authCode == null){
			authCode = request.getHeader(HttpHeader.UNIF_AUTH_KEY.toLowerCase());
		}

		// 如果是get请求，则从请求参数中取鉴权参数
		if (authCode == null && RequestMethod.GET.name().equals(request.getMethod())){
			authCode = request.getParameter(HttpHeader.UNIF_AUTH_KEY);
		}

		UserInfo userInfo = null;

		boolean needToLogin = false;
		Boolean squeezeOut = false;


		if (StringUtils.isBlank(authCode)){
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
			Responses responses = null;
			if (squeezeOut != null && squeezeOut){
				responses = Responses.error(ResponseCode.SQUEEZZ_OUT_CODE);
			}else {
				responses = Responses.error(ResponseCode.AUTH_INVALID_CODE);
			}
			dealLogin(response, responses);
			log.info("[login] {} squeezeOut {} ", uri, squeezeOut);
			return;
		}

		//登出跳转
		if (LOGOUT_URL.equals(uri)){
			authStore.remove(authCode);
			dealLogout(response);
			return;
		}

		MDC.put("employeeCode", userInfo.getEmployeeCode());
		AuthUserUtils.init(userInfo);
		authStore.expire(authCode, this.loginTimeOut);
		filterChain.doFilter(request, response);
		AuthUserUtils.clean();
	}

	private void dealLogin(HttpServletResponse response, Responses responses) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setStatus(HttpStatus.OK.value());
		out.print(JSON.toJSONString(responses));
		out.close();
	}

	protected void dealLogout(HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setStatus(HttpStatus.OK.value());
		out.print(JSON.toJSONString(Responses.success()));
		out.close();
	}

}
