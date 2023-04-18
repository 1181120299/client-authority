package com.jack.clientauthority.entity;

import lombok.*;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

/**
 * 角色-权限 对应关系
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:36
 */
@Data
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_role_permission")
public class RolePermission {
	
	/**
	* 角色id
	*/
	@TableId(value = "role_id", type = IdType.ASSIGN_UUID)
	private String roleId;

	/**
	* 权限代码
	*/
	private String permissionCode;

}
