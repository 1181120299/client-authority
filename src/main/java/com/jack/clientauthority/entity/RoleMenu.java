package com.jack.clientauthority.entity;

import lombok.*;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

/**
 * 角色-菜单 对应关系
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
@TableName("t_role_menu")
public class RoleMenu {
	
	/**
	* 角色id
	*/
	@TableId(value = "role_id", type = IdType.ASSIGN_UUID)
	private String roleId;

	/**
	* 菜单项id
	*/
	private String menuId;

}
