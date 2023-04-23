package com.jack.clientauthority.utils;

import com.jack.clientauthority.annotation.UserCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * 获取用户信息的帮助类
 */
@Slf4j
public class UserHelper{

    /**
     * 获取用户信息
     * <p></p>
     * @param userClass 用户类
     * @return  用户实体
     */
    public static <T> T getUserinfo(Class<T> userClass) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (Objects.isNull(authentication)) {
            log.info("Authentication is null. Can not assemble user.");
            return null;
        }

        String username = authentication.getName();
        UserCache userCache = ApplicationContextHelper.getBean(UserCache.class);
        return userCache.getUser(username, userClass);
    }
}
