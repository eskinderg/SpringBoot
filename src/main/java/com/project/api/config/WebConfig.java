package com.project.api.config;

import com.project.api.interceptor.TimestampHeaderFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    @Configuration
    public class WebConfig {

        @Bean
        public FilterRegistrationBean<TimestampHeaderFilter> timestampHeaderFilter() {
            FilterRegistrationBean<TimestampHeaderFilter> registrationBean = new FilterRegistrationBean<>();
            registrationBean.setFilter(new TimestampHeaderFilter());
            registrationBean.addUrlPatterns("/*"); // Apply to all URLs
            return registrationBean;
        }
    }