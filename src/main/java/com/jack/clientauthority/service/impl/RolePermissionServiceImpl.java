package com.jack.clientauthority.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jack.clientauthority.mapper.RolePermissionMapper;
import com.jack.clientauthority.entity.RolePermission;
import com.jack.clientauthority.service.RolePermissionService;

@Slf4j
@Service("rolePermissionService")
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

}
