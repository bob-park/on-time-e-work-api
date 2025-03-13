package com.malgn.configure.security;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.malgn.auth.client.RoleClient;
import com.malgn.auth.model.RoleResponse;
import com.malgn.configure.security.converter.JwtRoleGrantAuthoritiesConverter;
import com.malgn.configure.security.handler.RestAccessDeniedHandler;
import com.malgn.configure.security.handler.RestAuthenticationEntryPoint;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class OAuth2ResourceServerConfiguration {

    private final ObjectMapper om;

    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain resourceSecurityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
            requests ->
                requests.anyRequest().authenticated());

        http.oauth2ResourceServer(
            resourceServer ->
                resourceServer.jwt(
                    jwtConfigurer ->
                        jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        http.exceptionHandling(
            exceptionHandler ->
                exceptionHandler.authenticationEntryPoint(authenticationEntryPoint())
                    .accessDeniedHandler(accessDeniedHandler()));

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(corsConfigurationSource));

        return http.build();
    }

    /*
     * exception handling
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new RestAuthenticationEntryPoint(om);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new RestAccessDeniedHandler(om);
    }

    @Bean
    public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties) {
        return JwtDecoders.fromOidcIssuerLocation(properties.getJwt().getIssuerUri());
    }

    /**
     * authentication converter
     *
     * @return authenticationConverter
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtRoleGrantAuthoritiesConverter());

        return jwtAuthenticationConverter;
    }

    /**
     * role hierarchy
     */
    @Bean
    public static MethodSecurityExpressionHandler expressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();

        expressionHandler.setRoleHierarchy(roleHierarchy);

        return expressionHandler;
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy(RoleClient roleClient) {

        List<RoleResponse> roles = roleClient.getAll();

        Map<String, List<String>> roleHierarchyMap = parseRoleHierarchyMap(roles);
        String rolesHierarchyStr = RoleHierarchyUtils.roleHierarchyFromMap(roleHierarchyMap);

        log.debug("role hierarchy=\n{}", rolesHierarchyStr);

        return RoleHierarchyImpl.fromHierarchy(rolesHierarchyStr);
    }

    private Map<String, List<String>> parseRoleHierarchyMap(final List<RoleResponse> roles) {

        Map<String, List<String>> result = Maps.newHashMap();

        for (RoleResponse role : roles) {

            if (role.children().isEmpty()) {
                continue;
            }

            List<String> children = role.children().stream().map(item -> item.type().name()).toList();

            result.put(role.type().name(), Lists.newArrayList(children));
        }

        return result;
    }
}
