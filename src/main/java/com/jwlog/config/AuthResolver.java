package com.jwlog.config;

import com.jwlog.config.data.UserSession;
import com.jwlog.domain.Session;
import com.jwlog.exception.UnauthorizedException;
import com.jwlog.repository.SessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserSession.class);
    }

    /* 인증 - 헤더 이용 */
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        String accessToken = webRequest.getHeader("Authorization");
//        if (!StringUtils.hasText(accessToken)) {
//            throw new UnauthorizedException();
//        }
//
//        Session session = sessionRepository.findByAccessToken(accessToken)
//                .orElseThrow(UnauthorizedException::new);
//
//        return new UserSession(session.getUser().getId());
//    }


    /* 인증 - 쿠키 이용 */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (servletRequest == null) {
            log.error("servletRequest is null");
            throw new UnauthorizedException();
        }

        Cookie[] cookies = servletRequest.getCookies();
        if (cookies.length == 0) {
            log.error("쿠키가 없습니다.");
        }

        String SESSION = cookies[0].getName();
        String accessToken = cookies[0].getValue();

        log.info("session={}", SESSION);
        log.info("accessToken={}", accessToken);

        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(UnauthorizedException::new);

        return new UserSession(session.getUser().getId());
    }
}
