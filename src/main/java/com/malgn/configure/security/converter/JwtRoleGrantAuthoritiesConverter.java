package com.malgn.configure.security.converter;

import java.util.Collection;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtRoleGrantAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String SCOPE_PREFIX = "SCOPE_";
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String AUTHORITY_PREFIX = "AUTHORITY_";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        List<GrantedAuthority> authorities = Lists.newArrayList();

        List<String> scopes = jwt.getClaimAsStringList("scope");

        // SCOPE + ROLE
        for (String scope : scopes) {

            String scopeName = SCOPE_PREFIX + scope;

            if (StringUtils.startsWithIgnoreCase(scope, ROLE_PREFIX)) {
                scopeName = scope;
            }

            authorities.add(new SimpleGrantedAuthority(scopeName));
        }

        return authorities;
    }

}
