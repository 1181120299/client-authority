package com.jack.clientauthority.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jack.clientauthority.mapper.RoleMenuMapper;
import com.jack.clientauthority.entity.RoleMenu;
import com.jack.clientauthority.service.RoleMenuService;

@Slf4j
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
