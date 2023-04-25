package com.jack.clientauthority.service;

import com.jack.clientauthority.entity.RoleMenu;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 角色-菜单 对应关系
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:36
 */
public interface RoleMenuService extends IService<RoleMenu> {

    /**
     * 保存为角色分配的菜单
     * @param roleId    角色id
     * @param menuIds   菜单id，多个以英文逗号拼接
     */
    void save(String roleId, String menuIds);
}
