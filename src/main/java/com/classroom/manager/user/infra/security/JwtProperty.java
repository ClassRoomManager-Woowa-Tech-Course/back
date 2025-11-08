package com.classroom.manager.user.infra.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
public record JwtProperty(
        String secretKey,
        Long accessTokenExpirationDay
) {
}
