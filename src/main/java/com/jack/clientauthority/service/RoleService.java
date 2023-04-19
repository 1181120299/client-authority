package com.jack.clientauthority.service;

import com.jack.clientauthority.entity.Role;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 应用角色
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:36
 */
public interface RoleService extends IService<Role> {

    /**
     * 检查角色信息是否已经存在
     * @param text	要检查的文本（角色名称、角色编码）
     * @param type	类型（name：角色名称。code：角色编码）
     * @throws com.jack.utils.web.RRException   如果角色信息已存在
     */
    void checkRole(String text, String type);
}
