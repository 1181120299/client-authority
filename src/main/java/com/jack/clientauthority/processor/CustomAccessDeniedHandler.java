package com.jack.clientauthority.processor;

import com.alibaba.fastjson.JSON;
import com.jack.utils.web.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.info("Access deny: {}", accessDeniedException.getMessage());
        response.setContentType("application/json;charset=utf-8");
        String message = accessDeniedException.getMessage();
        R error = R.error(StringUtils.hasText(message) ? message : "您没有访问权限");
        response.getWriter().write(JSON.toJSONString(error));
    }
}
