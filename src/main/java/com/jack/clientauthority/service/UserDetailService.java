package com.jack.clientauthority.service;

import com.jack.utils.web.R;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 *  用户信息接口
 */
public interface UserDetailService {

    /**
     * 根据用户名查询用户信息。精确查询。
     * @param username  用户名
     * @return  其中data为用户信息
     * @throws com.jack.utils.web.RRException   如果查询用户发生异常
     */
    @Nonnull
    <T> T findByUsername(String username, Class<T> clazz);

    /**
     * 获取所有的用户名
     * @return  其中data为用户名的集合
     * @throws com.jack.utils.web.RRException   如果查询用户发生异常
     */
    @Nonnull
    List<String> usernameList();
}
