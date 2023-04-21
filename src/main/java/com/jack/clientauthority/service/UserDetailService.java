package com.jack.clientauthority.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 *  用户主要信息接口
 */
public interface UserDetailService {

    /**
     * 根据用户名模糊查询用户信息
     * <p></p>
     *
     * 如果不传用户名，则查询全部
     *
     * <p></p>
     * @param username  用户名
     * @return  用户信息
     */
    List<UserDetails> list(String username);
}
