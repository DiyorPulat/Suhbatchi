package com.example.suhbatchi.config;

import com.thetransactioncompany.cors.CORSFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CorsConfig {

    @Bean
    public FilterRegistrationBean<CORSFilter> corsFilterRegistration() {
        FilterRegistrationBean<CORSFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CORSFilter());
        registrationBean.setUrlPatterns(Collections.singletonList("/*"));

        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("cors.allowOrigin", "http://localhost:3000");
        initParameters.put("cors.supportedMethods", "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH");
        registrationBean.setInitParameters(initParameters);
        return registrationBean;
    }
}