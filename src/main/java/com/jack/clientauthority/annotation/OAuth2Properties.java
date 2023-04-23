package com.jack.clientauthority.annotation;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jack.oauth2")
public class OAuth2Properties {

    /**
     * 授权服务的地址，例如：http://ip:port/contextPath
     * <p></p>
     *
     * 注意：后面不不不要加斜杠/
     */
    private String authServerUri;

    /**
     * 资源服务的地址，例如：http://ip:port/contextPath
     * <p></p>
     *
     * 注意：后面不不不要加斜杠/
     */
    private String resourceServerUri;

    /**
     * 在授权服务器上注册的OAuth client的id（应用名称）
     */
    private String clientId;

    /**
     * 在授权服务器上注册的OAuth client的秘钥
     */
    private String clientSecret;

    /**
     * 当前应用部署的服务器ip，需要和授权服务器上注册的OAuth client回调地址中的ip保持一致
     */
    private String clientIp;
}
