package com.hacker.hackathon.config.resolver;

import com.hacker.hackathon.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;



@Component
@RequiredArgsConstructor
public class UserIdResolver implements HandlerMethodArgumentResolver {

    private final JwtService jwtService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasToken = parameter.hasParameterAnnotation(UserId.class);
        boolean isLongType = Long.class.equals(parameter.getParameterType());
        return hasToken && isLongType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = null;

        if (webRequest instanceof ServletWebRequest) {
            ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest;
            request = servletWebRequest.getRequest();
        } else if (webRequest.getNativeRequest() instanceof HttpServletRequest) {
            request = (HttpServletRequest) webRequest.getNativeRequest();
        }

        if (request == null) {
            throw new IllegalStateException("HttpServletRequest not available in the current context.");
        }

        final String token = request.getHeader("Authorization");
        // 토큰 검증
        if (!jwtService.verifyToken(token)) {
            throw new RuntimeException(String.format("USER_ID를 가져오지 못했습니다. (%s - %s)", parameter.getClass(), parameter.getMethod()));
        }

        // 유저 아이디 반환
        final String tokenContents = jwtService.getJwtContents(token);
        try {
            return Long.parseLong(tokenContents);
        } catch (NumberFormatException e) {
            throw new RuntimeException(String.format("USER_ID를 가져오지 못했습니다. (%s - %s)", parameter.getClass(), parameter.getMethod()));
        }
    }
}
