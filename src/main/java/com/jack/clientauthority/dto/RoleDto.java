package com.jack.clientauthority.dto;

import java.util.Date;

import lombok.*;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;

/**
 * 应用角色Dto
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:36
 */
@Data
public class RoleDto {
	
	/**
	* 主键
	*/
	@Length(max = 255, message = "主键最多255个字符")
	private String id;

	/**
	* 角色名称，不可重复
	*/
	@Length(max = 2000, message = "角色名称，不可重复最多2000个字符")
	@NotBlank(message = "角色名称，不可重复不能为空")
	private String name;

	/**
	* 角色编码，不可重复
	*/
	@Length(max = 2000, message = "角色编码，不可重复最多2000个字符")
	@NotBlank(message = "角色编码，不可重复不能为空")
	private String code;

	/**
	* 描述
	*/
	@Length(max = 2000, message = "描述最多2000个字符")
	private String description;

}
