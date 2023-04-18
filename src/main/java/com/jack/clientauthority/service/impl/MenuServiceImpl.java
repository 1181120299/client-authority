package com.jack.clientauthority.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jack.clientauthority.mapper.MenuMapper;
import com.jack.clientauthority.entity.Menu;
import com.jack.clientauthority.service.MenuService;

@Slf4j
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

}
