package com.classroom.manager.user.infra.security.resolver;

import com.classroom.manager.user.infra.security.JwtProvider;
import com.classroom.manager.user.infra.security.annotation.Auth;
import com.classroom.manager.user.infra.security.dto.TokenPayLoad;
import com.classroom.manager.user.infra.security.exception.UnAuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuth = parameter.hasParameterAnnotation(Auth.class);
        boolean authType = parameter.getParameterType().equals(TokenPayLoad.class);
        return hasAuth && authType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {
        String token = extractAccessToken(webRequest);
        return jwtProvider.extractTokenPayLoad(token);
    }

    private String extractAccessToken(NativeWebRequest webRequest) {
        String bearerToken = webRequest.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new UnAuthorizationException(bearerToken);
    }
}
