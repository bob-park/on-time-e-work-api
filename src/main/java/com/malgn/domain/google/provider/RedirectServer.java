package com.malgn.domain.google.provider;

import static org.apache.commons.lang3.math.NumberUtils.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;

import com.malgn.common.exception.ServiceRuntimeException;

@ToString
@Getter
public class RedirectServer {
    private static final String GROUP_SCHEMA = "schema";
    private static final String GROUP_DOMAIN = "domain";
    private static final String GROUP_PORT = "port";
    private static final String GROUP_REDIRECT_URI = "redirectUri";

    private static final Pattern PATTERN = Pattern.compile(
        "(?<schema>http|https):\\/\\/(?<domain>[\\w._-가-힝]*)(:(?<port>[\\d]*))?(?<redirectUri>\\/.*)?");

    private final String schema;
    private final String domain;
    private final int port;
    private final String callbackUri;

    @Builder
    public RedirectServer(String redirectUrl) {

        Matcher matcher = PATTERN.matcher(redirectUrl);

        if (!matcher.find()) {
            throw new ServiceRuntimeException("Invalid redirect url");
        }

        String schemaStr = matcher.group(GROUP_SCHEMA);
        String domainStr = matcher.group(GROUP_DOMAIN);
        String portStr = matcher.group(GROUP_PORT);
        String callbackUriStr = matcher.group(GROUP_REDIRECT_URI);

        this.schema = schemaStr;
        this.domain = domainStr;
        this.port = StringUtils.isNotBlank(portStr) ? toInt(portStr) : 80;
        this.callbackUri = StringUtils.isNotBlank(callbackUriStr) ? callbackUriStr : "/Callback";
    }
}
