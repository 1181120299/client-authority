package com.jack.clientauthority.annotation;

import com.jack.utils.web.RRException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Slf4j
@Data
@ConfigurationProperties(prefix = "jack.oauth2")
public class OAuth2Properties implements InitializingBean {

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

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(authServerUri, "AuthServerUri must not be empty.");
        Assert.hasText(clientId, "ClientId must not be empty.");
        Assert.hasText(clientSecret, "ClientSecret must not be empty.");

        if (authServerUri.endsWith("/")) {
            throw new IllegalStateException("AuthServerUri must not end with /");
        }

        if (authServerUri.contains("127.0.0.1")) {
            throw new IllegalStateException("AuthServerUri must not contains 127.0.0.1");
        }

        if (authServerUri.toLowerCase().contains("localhost")) {
            throw new RRException("AuthServerUri must not contains localhost");
        }

        if (StringUtils.hasText(resourceServerUri)) {
            if (resourceServerUri.endsWith("/")) {
                throw new IllegalStateException("ResourceServerUri must not end with /");
            }

            if (resourceServerUri.contains("127.0.0.1")) {
                throw new IllegalStateException("ResourceServerUri must not contains 127.0.0.1");
            }

            if (resourceServerUri.toLowerCase().contains("localhost")) {
                throw new RRException("ResourceServerUri must not contains localhost");
            }

            String regex = ".*\\d+\\.\\d+\\.\\d+\\.\\d+(:\\d+)?/.+";
            if (!resourceServerUri.matches(regex)) {
                throw new IllegalStateException("Invalid resourceServerUri. The correct format demo：http://192.168.1.101:8080/resource");
            }
        }
    }
}
