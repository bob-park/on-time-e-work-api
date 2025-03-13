package com.malgn.configure.jpa;

import java.util.Optional;

import jakarta.persistence.EntityManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.querydsl.jpa.impl.JPAQueryFactory;

@EnableJpaAuditing
@Configuration
public class JpaConfiguration {

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null) {
                return Optional.of("system");
            }

            if (auth.getClass().isAssignableFrom(AnonymousAuthenticationToken.class)) {
                throw new AuthenticationServiceException("Authentication is required...");
            }

            return Optional.of(auth.getName());
        };
    }

}
