package com.jack.clientauthority.service.impl;

import com.jack.clientauthority.annotation.OAuth2Properties;
import com.jack.clientauthority.service.UserDetailService;
import com.jack.clientauthority.utils.ClientRegistrationConstant;
import com.jack.clientauthority.utils.WebClientHelper;
import com.jack.utils.web.R;
import com.jack.utils.web.RRException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息接口默认实现类。
 * <p></p>
 *
 * 从配置的授权服务器（authserver）获取用户信息。
 * <p></p>
 *
 * 如果用户信息来源于别的地方，client可以通过提供{@link com.jack.clientauthority.service.UserDetailService}的实现类来自定义。
 */
@Slf4j
public class RemoteUserDetailServiceImpl implements UserDetailService {

    @Autowired
    private OAuth2Properties oAuth2Properties;

    private final String findByUsernameUri = "/user/findByUsername?username=";

    private final String usernameListUri = "/user/usernameList";

    @Override
    public <T> T findByUsername(String username, Class<T> clazz) {
        String url = oAuth2Properties.getResourceServerUri() + findByUsernameUri + username;
        R resp = WebClientHelper.get(url, R.class);
        if (R.getCodeOk() == resp.getCode()) {
            return resp.getData(clazz);
        }

        log.error("Request user info fail. resp: {}", resp);
        throw new RRException("用户信息获取异常");
    }

    @Override
    public List<String> usernameList() {
        String url = oAuth2Properties.getResourceServerUri() + usernameListUri;
        R resp = WebClientHelper.get(url, R.class);
        if (R.getCodeOk() == resp.getCode()) {
            return resp.getDataList(String.class);
        }

        log.error("Request username list fail. resp: {}", resp);
        throw new RRException("用户信息获取异常");
    }
}
