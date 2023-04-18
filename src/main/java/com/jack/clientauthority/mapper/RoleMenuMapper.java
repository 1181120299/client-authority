package com.jack.clientauthority.mapper;

import com.jack.clientauthority.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色-菜单 对应关系
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:36
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
	
}
