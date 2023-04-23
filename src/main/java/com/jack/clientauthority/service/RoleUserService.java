package com.jack.clientauthority.service;

import com.jack.clientauthority.entity.RoleUser;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户-角色 对应关系
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:35
 */
public interface RoleUserService extends IService<RoleUser> {

    /**
     * 设置角色关联的用户
     * <p></p>
     *
     * 此方法是全量的：先删除角色关联的用户，再重新设置关联。
     *
     * @param roleId    角色id
     * @param usernameArray 用户名数组
     */
    void saveRoleUser(String roleId, String[] usernameArray);
}
