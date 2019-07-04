package com.myfutech.demo.user.service.provider.config;

import com.myfutech.common.spring.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean logFilter() {
		final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		final LogFilter logFilter = new LogFilter();
		filterRegistrationBean.setFilter(logFilter);
		return filterRegistrationBean;
	}
}
