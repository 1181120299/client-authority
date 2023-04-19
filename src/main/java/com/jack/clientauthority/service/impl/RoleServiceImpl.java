package com.jack.clientauthority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jack.utils.web.RRException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jack.clientauthority.mapper.RoleMapper;
import com.jack.clientauthority.entity.Role;
import com.jack.clientauthority.service.RoleService;

import java.util.List;

@Slf4j
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public void checkRole(String text, String type) {
        LambdaQueryWrapper<Role> queryWrapper;
        if ("name".equals(type)) {
            queryWrapper = new LambdaQueryWrapper<Role>().eq(Role::getName, text);
        } else if ("code".equals(type)) {
            queryWrapper = new LambdaQueryWrapper<Role>().eq(Role::getCode, text);
        } else {
            throw new IllegalArgumentException("不支持的检查类型(type)：" + type);
        }

        List<Role> existedRoleList = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(existedRoleList)) {
            String message;
            if ("name".equals(type)) {
                message = "该角色名称已存在";
            } else {
                message = "该角色编码已存在";
            }

            throw new RRException(message);
        }
    }

    @Override
    public boolean save(Role entity) {
        checkRole(entity.getName(), "name");
        checkRole(entity.getCode(), "code");
        return super.save(entity);
    }

    @Override
    public boolean updateById(Role entity) {
        List<Role> nameList = baseMapper.selectList(new LambdaQueryWrapper<Role>().eq(Role::getName, entity.getName()));
        List<Role> otherNameList = nameList.stream().filter(item -> !item.getId().equals(entity.getId())).toList();
        if(CollectionUtils.isNotEmpty(otherNameList)) {
            throw new RRException("角色名称已存在");
        }

        List<Role> codeList = baseMapper.selectList(new LambdaQueryWrapper<Role>().eq(Role::getCode, entity.getCode()));
        List<Role> otherCodeList = codeList.stream().filter(item -> !item.getId().equals(entity.getId())).toList();
        if (CollectionUtils.isNotEmpty(otherCodeList)) {
            throw new RRException("角色编码已存在");
        }

        return super.updateById(entity);
    }
}
