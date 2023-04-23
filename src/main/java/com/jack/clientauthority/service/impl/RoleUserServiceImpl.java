package com.jack.clientauthority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jack.clientauthority.mapper.RoleUserMapper;
import com.jack.clientauthority.entity.RoleUser;
import com.jack.clientauthority.service.RoleUserService;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service("roleUserService")
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser> implements RoleUserService {

    @Override
    public void saveRoleUser(String roleId, String[] usernameArray) {
        Assert.notNull(roleId, "roleId can not be null");
        baseMapper.delete(new LambdaQueryWrapper<RoleUser>().eq(RoleUser::getRoleId, roleId));
        if (usernameArray == null || usernameArray.length == 0) {
            return;
        }

        List<RoleUser> roleUserList = new ArrayList<>();
        for (String username : new HashSet<>(List.of(usernameArray))) {
            RoleUser item = RoleUser.builder()
                    .username(username)
                    .roleId(roleId)
                    .build();
            roleUserList.add(item);
        }

        saveBatch(roleUserList);
    }
}
