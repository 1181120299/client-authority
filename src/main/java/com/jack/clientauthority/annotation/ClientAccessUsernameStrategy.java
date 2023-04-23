package com.jack.clientauthority.annotation;

/**
 * client应用之间互相调用接口时，Authentication中的用户名策略。
 * <p></p>
 *
 * 在client之间调用接口，基于OAuth2协议，解析JWT令牌得到的用户信息（Authentication）是调用方应用(client)的信息。
 */
public interface ClientAccessUsernameStrategy {

    Strategy strategy();

    enum Strategy{
        /**
         * 将Authentication中的用户名设置为真实的已认证用户
         */
        REAL_AUTHENTICATED_USER,
        /**
         * 不做操作，保留client应用信息
         */
        NOOP;
    }
}
