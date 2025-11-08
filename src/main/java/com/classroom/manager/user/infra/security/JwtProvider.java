package com.classroom.manager.user.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.classroom.manager.user.domain.Authorization;
import com.classroom.manager.user.infra.security.dto.TokenPayLoad;
import com.classroom.manager.user.infra.security.exception.UnAuthorizationException;
import org.springframework.stereotype.Component;

import java.util.Date;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component
public class JwtProvider {

    private final long accessTokenExpirationDayToMilliseconds;
    private final Algorithm algorithm;

    public JwtProvider(JwtProperty jwtProperty) {
        this.accessTokenExpirationDayToMilliseconds = MILLISECONDS.convert(jwtProperty.accessTokenExpirationDay(), DAYS);
        this.algorithm = Algorithm.HMAC256(jwtProperty.secretKey());
    }

    public String createToken(TokenPayLoad tokenPayLoad) {
        return JWT.create()
                .withExpiresAt(new Date(
                        System.currentTimeMillis() + accessTokenExpirationDayToMilliseconds
                ))
                .withIssuedAt(new Date())
                .withClaim("adminId", tokenPayLoad.adminId())
                .withClaim("authorization", tokenPayLoad.authorization().name())
                .sign(algorithm);
    }

    public Object extractTokenPayLoad(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .build()
                    .verify(token);
            String adminId = decodedJWT.getClaim("adminId").asString();
            Authorization authorization = decodedJWT.getClaim("authorization").as(Authorization.class);
            return new TokenPayLoad(adminId, authorization);
        }  catch (JWTVerificationException ex) {
            throw new UnAuthorizationException(token);
        }
    }
}
