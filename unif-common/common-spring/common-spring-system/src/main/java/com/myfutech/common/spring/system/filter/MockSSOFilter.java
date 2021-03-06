package com.myfutech.common.spring.system.filter;

import com.myfutech.common.spring.system.store.IAuthStore;
import com.myfutech.common.spring.system.util.AuthUserUtils;
import com.myfutech.common.util.constant.AuthInfo;
import com.myfutech.common.util.constant.HttpHeader;
import com.myfutech.common.util.vo.auth.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * 单点登陆
 */
public class MockSSOFilter extends SSOFilter {

	private static final Logger log = LoggerFactory.getLogger(MockSSOFilter.class);

	private UserInfo userInfo;

	public MockSSOFilter(String defaultLoginUrl, String defaultLogoutUrl, IAuthStore<UserInfo> authStore) {
		this(defaultLoginUrl, defaultLogoutUrl, authStore, null);
	}

	public MockSSOFilter(String defaultLoginUrl, String defaultLogoutUrl, IAuthStore<UserInfo> authStore, Set<String> skipUrl) {
		super(defaultLoginUrl,defaultLogoutUrl,authStore,skipUrl);
	}

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
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
		if (authCode == null && RequestMethod.GET.name().equals(request.getMethod())){
			authCode = request.getParameter(HttpHeader.UNIF_AUTH_KEY);
		}
		UserInfo userInfo = null;
		if (StringUtils.isNotBlank(authCode)){
			userInfo = authStore.getAttach(authCode, AuthInfo.AUTH_REDIS_USER_INFO, UserInfo.class);
		}
		if (userInfo == null) {
			if (this.userInfo != null) {
				userInfo = this.userInfo;
			} else {
				userInfo = UserInfo.builder().userCode("test")
						.employeeCode("test").id(1L).position("测试职位").realName("默认测试").build();
				log.info("没有设置用户信息，使用默认用户信息：{}", userInfo);
			}
		}

		//登入跳转 不做校验
		if (LOGIN_URL.equals(uri)){
			dealLogin(request, response, uri, requestType, false);
			return;
		}
		//登出跳转
		if (LOGOUT_URL.equals(uri)){
			dealLogout(request,response,uri,requestType);
			return;
		}
		MDC.put("employeeCode", userInfo.getEmployeeCode());
		AuthUserUtils.init(userInfo);
		filterChain.doFilter(request, response);
		AuthUserUtils.clean();
	}
}
