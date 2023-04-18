package com.jack.clientauthority.entity;

import lombok.*;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

/**
 * 用户-角色 对应关系
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:35
 */
@Data
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_role_user")
public class RoleUser {
	
	/**
	* 用户名
	*/
	@TableId(value = "username", type = IdType.ASSIGN_UUID)
	private String username;

	/**
	* 角色id
	*/
	private String roleId;

}
