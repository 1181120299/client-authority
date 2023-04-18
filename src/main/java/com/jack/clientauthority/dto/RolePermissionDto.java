package com.jack.clientauthority.dto;

import java.util.Date;

import lombok.*;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;

/**
 * 角色-权限 对应关系Dto
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:36
 */
@Data
public class RolePermissionDto {
	
	/**
	* 角色id
	*/
	@Length(max = 255, message = "角色id最多255个字符")
	private String roleId;

	/**
	* 权限代码
	*/
	@Length(max = 500, message = "权限代码最多500个字符")
	@NotBlank(message = "权限代码不能为空")
	private String permissionCode;

}
