package com.jack.clientauthority.mapper;

import com.jack.clientauthority.entity.RoleUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户-角色 对应关系
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:35
 */
@Mapper
public interface RoleUserMapper extends BaseMapper<RoleUser> {
	
}
