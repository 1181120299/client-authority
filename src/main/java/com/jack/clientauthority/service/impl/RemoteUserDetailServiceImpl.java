package com.jack.clientauthority.service.impl;

import com.jack.clientauthority.service.UserDetailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
@Service
@ConditionalOnMissingBean(UserDetailService.class)
public class RemoteUserDetailServiceImpl implements UserDetailService {

    @Value("${authserver}")
    private String authServerAddress;

    @Override
    public List<UserDetails> list(String username) {
        return null;
    }
}
