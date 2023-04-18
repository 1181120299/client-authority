package com.jack.clientauthority.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jack.clientauthority.mapper.RoleMapper;
import com.jack.clientauthority.entity.Role;
import com.jack.clientauthority.service.RoleService;

@Slf4j
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
