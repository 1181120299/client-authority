package com.jack.clientauthority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jack.clientauthority.entity.Role;
import com.jack.clientauthority.mapper.RoleMapper;
import com.jack.utils.web.RRException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jack.clientauthority.mapper.RolePermissionMapper;
import com.jack.clientauthority.entity.RolePermission;
import com.jack.clientauthority.service.RolePermissionService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service("rolePermissionService")
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void savePermission(String roleId, List<String> permissionCodeList) {
        Role role = roleMapper.selectById(roleId);
        if (Objects.isNull(role)) {
            throw new RRException("角色不存在");
        }

        if (permissionCodeList == null) {
            permissionCodeList = Collections.emptyList();
        }

        List<RolePermission> rpList = new ArrayList<>();
        permissionCodeList.forEach(permissionCode -> {
            RolePermission rp = new RolePermission();
            rp.setRoleId(roleId);
            rp.setPermissionCode(permissionCode);
            rpList.add(rp);
        });

        // 全部删掉，保存新的
        baseMapper.delete(new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, roleId));
        if (CollectionUtils.isNotEmpty(rpList)) {
            saveBatch(rpList);
        }
    }
}
