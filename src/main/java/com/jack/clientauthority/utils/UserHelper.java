package com.jack.clientauthority.utils;

import com.alibaba.fastjson.JSON;
import com.jack.utils.excel.EntityLambdaUtils;
import com.jack.utils.web.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * 获取用户信息的帮助类
 */
@Slf4j
public class UserHelper {

    /**
     * 提取用户信息，组装成指定的用户实体类
     * @param userClass 用户类
     * @return  用户实体
     */
    public static <T> T assembleUser(Class<T> userClass) {
        return assembleUser(userClass, null);
    }

    /**
     * 提取用户信息，组装成指定的用户实体类。
     * <p></p>
     *
     * 此方法用于应用（oauth client）之间相互调用接口时，提取用户信息。
     * 因为应用之间的相互调用，基于OAuth2协议，JWT令牌解析得到的是调用方应用（client）的信息。
     * <p></p>
     *
     * 此方法只能做到获取已认证授权的用户名，无法获取用户的其他信息。
     *
     * <p></p>
     * @param userClass 用户类
     * @param column    用户名的字段（例如：User::getUsername）
     * @return  用户实体
     */
    public static <T, R> T assembleUser(Class<T> userClass, EntityLambdaUtils.Column<T, R> column) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (Objects.isNull(authentication)) {
            log.info("Authentication is null. Can not assemble user.");
            return null;
        }

        if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
            return JSON.parseObject(JSON.toJSONString(oidcUser.getClaims()), userClass);
        } else if (authentication.getPrincipal() instanceof Jwt && column != null) {
            try {
                Constructor<T> tConstructor = ReflectionUtils.accessibleConstructor(userClass);
                T newUser = tConstructor.newInstance();
                String usernameFieldName = EntityLambdaUtils.getFieldByFunction(column);
                Field field = ReflectionUtils.findField(userClass, usernameFieldName);
                Assert.notNull(field, "The class " + userClass.getName() + " has not field name: " + usernameFieldName);
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, newUser, authentication.getName());
                return newUser;
            } catch (NoSuchMethodException e) {
                String message = "The class %s should provide a no arguments constructor.";
                throw new RuntimeException(String.format(message, userClass.getName()), e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                log.error("Sorry! I try to assemble user, but fail. simple message: {}", e.getMessage());
            }
        } else {
            log.info("Sorry! I can`t assemble user from authentication: {}", authentication);
        }

        return null;
    }

}
