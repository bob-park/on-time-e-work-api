package com.malgn.configure.feign.error;

import static org.apache.commons.lang3.ObjectUtils.*;

import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Response;
import feign.Response.Body;
import feign.codec.ErrorDecoder;

import com.malgn.common.exception.NotFoundException;
import com.malgn.common.exception.ServiceUnavailableException;
import com.malgn.common.model.ApiResult;

@Slf4j
@RequiredArgsConstructor
@Component
public class FeignClientErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {

        String errorMsg = null;

        try {
            Body body = response.body();

            if (isNotEmpty(body)) {
                ApiResult<?> apiResult = objectMapper.readValue(body.asInputStream(), ApiResult.class);
                if (isNotEmpty(apiResult.getError())) {
                    errorMsg = apiResult.getError().getDetail().getReason();
                }
            }

        } catch (IOException e) {
            log.warn("Response parse error - {}", e.getMessage());
        }

        switch (response.status()) {
            case 400 -> {
                return new IllegalArgumentException(errorMsg);
            }

            case 401 -> {
                return new AuthenticationServiceException(errorMsg);
            }

            case 403 -> {
                return new AccessDeniedException("No permission");
            }

            case 404 -> {
                return new NotFoundException("Not found - " + errorMsg);
            }

            case 503 -> {

                String target = response.request().requestTemplate().feignTarget().name();

                return new ServiceUnavailableException(target);
            }

            default -> {
                return new Exception(response.reason());
            }
        }

    }
}
