package com.jack.clientauthority.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jack.clientauthority.mapper.RoleUserMapper;
import com.jack.clientauthority.entity.RoleUser;
import com.jack.clientauthority.service.RoleUserService;

@Slf4j
@Service("roleUserService")
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser> implements RoleUserService {

}
