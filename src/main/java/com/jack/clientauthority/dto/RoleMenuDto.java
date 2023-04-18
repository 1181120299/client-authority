package com.jack.clientauthority.dto;

import java.util.Date;

import lombok.*;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;

/**
 * 角色-菜单 对应关系Dto
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:36
 */
@Data
public class RoleMenuDto {
	
	/**
	* 角色id
	*/
	@Length(max = 255, message = "角色id最多255个字符")
	private String roleId;

	/**
	* 菜单项id
	*/
	@Length(max = 255, message = "菜单项id最多255个字符")
	@NotBlank(message = "菜单项id不能为空")
	private String menuId;

}
