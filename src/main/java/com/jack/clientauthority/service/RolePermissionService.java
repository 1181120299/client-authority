package com.jack.clientauthority.service;

import com.jack.clientauthority.entity.RolePermission;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色-权限 对应关系
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:36
 */
public interface RolePermissionService extends IService<RolePermission> {

    /**
     * 保存角色的权限信息
     * @param roleId    角色id
     * @param permissionCodeList 权限代码的集合
     */
    void savePermission(String roleId, List<String> permissionCodeList);
}
