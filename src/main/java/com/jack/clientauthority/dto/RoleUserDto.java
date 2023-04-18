package com.jack.clientauthority.dto;

import java.util.Date;

import lombok.*;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;

/**
 * 用户-角色 对应关系Dto
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:35
 */
@Data
public class RoleUserDto {
	
	/**
	* 用户名
	*/
	@Length(max = 255, message = "用户名最多255个字符")
	private String username;

	/**
	* 角色id
	*/
	@Length(max = 255, message = "角色id最多255个字符")
	@NotBlank(message = "角色id不能为空")
	private String roleId;

}
