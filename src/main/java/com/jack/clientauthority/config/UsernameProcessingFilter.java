package com.jack.clientauthority.config;

import com.jack.clientauthority.annotation.ClientAccessUsernameStrategy;
import com.jack.clientauthority.utils.WebClientHelper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 处理client之间相互调用接口时，解析JWT令牌得到的Authentication中的Name是否需要替换为真实已认证的用户名
 */
@Slf4j
public class UsernameProcessingFilter implements Filter {

    @Autowired
    private ClientAccessUsernameStrategy clientAccessUsernameStrategy;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String username = req.getHeader(WebClientHelper.JACK_OAUTH2_USERNAME_HEADER);
        if (StringUtils.hasText(username)) {
            log.debug("Got username from header, username: {}", username);
            if (ClientAccessUsernameStrategy.Strategy.REAL_AUTHENTICATED_USER.equals(clientAccessUsernameStrategy.strategy())) {
                updateAuthenticationName(username);
            }
        }

        chain.doFilter(request, response);
    }

    // 更新Authentication中的用户名
    private void updateAuthenticationName(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return;
        }

        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            Field nameField = ReflectionUtils.findField(jwtAuthenticationToken.getClass(), "name");
            assert nameField != null;
            ReflectionUtils.makeAccessible(nameField);
            ReflectionUtils.setField(nameField, jwtAuthenticationToken, username);
        }
    }
}
