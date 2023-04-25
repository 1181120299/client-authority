package com.jack.clientauthority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jack.clientauthority.mapper.RoleMenuMapper;
import com.jack.clientauthority.entity.RoleMenu;
import com.jack.clientauthority.service.RoleMenuService;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Override
    public void save(String roleId, String menuIds) {
        Assert.hasText(roleId, "roleId can not be empty");
        baseMapper.delete(new LambdaQueryWrapper<RoleMenu>()
                .eq(RoleMenu::getRoleId, roleId));
        if (!StringUtils.hasText(menuIds)) {
            return;
        }

        String[] menuIdArray = StringUtils.delimitedListToStringArray(menuIds, ",");
        List<RoleMenu> roleMenuList = new ArrayList<>();
        for (String menuId : menuIdArray) {
            RoleMenu roleMenu = RoleMenu.builder()
                    .menuId(menuId)
                    .roleId(roleId)
                    .build();
            roleMenuList.add(roleMenu);
        }

        this.saveBatch(roleMenuList);
    }
}
