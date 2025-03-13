package com.malgn.configure.feign;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.client.RestTemplate;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;

import com.google.common.collect.Maps;

import feign.RequestInterceptor;

@Configuration
public class FeignConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .connectTimeout(Duration.ofSeconds(10))
            .readTimeout(Duration.ofSeconds(10))
            .build();
    }

    @Bean
    public RequestInterceptor customRequestInterceptor() {
        return requestTemplate -> requestTemplate.headers(getRequestHeaders());
    }

    private Map<String, Collection<String>> getRequestHeaders() {

        Map<String, Collection<String>> headers = Maps.newHashMap();

        String accessToken = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !authentication.getClass().isAssignableFrom(AnonymousAuthenticationToken.class)) {

            Jwt jwt = (Jwt)authentication.getPrincipal();

            accessToken = jwt.getTokenValue();
        }

        if (StringUtils.isNotBlank(accessToken)) {
            headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + accessToken));
        }

        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
        headers.put(HttpHeaders.ACCEPT, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));

        return headers;
    }

}
