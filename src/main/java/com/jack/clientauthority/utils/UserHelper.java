package com.jack.clientauthority.utils;

import com.jack.clientauthority.annotation.CustomUserType;
import com.jack.clientauthority.annotation.UserCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * 获取用户信息的帮助类
 */
@Slf4j
public class UserHelper implements ApplicationContextAware {

    private static CustomUserType customUserType;

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

    /**
     * 获取用户信息。
     * <p></p>
     *
     * 调用此方法前，请确保你自定义的用户类实现了{@link CustomUserType}接口。
     * 并且需要注册为Spring been。
     * @return  用户信息
     */
    public static CustomUserType getUserinfo() {
        if (Objects.isNull(customUserType)) {
            throw new UnsupportedOperationException("You have not provide an implementation of CustomUserType interface, and it should register as Spring been.");
        }

        return getUserinfo(customUserType.getClass());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            customUserType = applicationContext.getBean(CustomUserType.class);
        } catch (NoSuchBeanDefinitionException e) {
            log.info("Have not provide CustomUserType implementation.");
        }
    }
}
