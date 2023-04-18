package com.jack.clientauthority.dto;

import java.util.Date;

import lombok.*;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;

/**
 * 应用菜单项Dto
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:36
 */
@Data
public class MenuDto {
	
	/**
	* 主键id
	*/
	@Length(max = 255, message = "主键id最多255个字符")
	private String id;

	/**
	* 父级菜单id，根节点id = 0
	*/
	@Length(max = 255, message = "父级菜单id，根节点id = 0最多255个字符")
	@NotBlank(message = "父级菜单id，根节点id = 0不能为空")
	private String parentId;

	/**
	* 菜单名称
	*/
	@Length(max = 2000, message = "菜单名称最多2000个字符")
	@NotBlank(message = "菜单名称不能为空")
	private String name;

	/**
	* 菜单编码，以/开头，英文字母
	*/
	@Length(max = 2000, message = "菜单编码，以/开头，英文字母最多2000个字符")
	@NotBlank(message = "菜单编码，以/开头，英文字母不能为空")
	private String code;

	/**
	* 完整的菜单编码，即包含了父级菜单的编码。以/开头，英文字母
	*/
	@Length(max = 2000, message = "完整的菜单编码，即包含了父级菜单的编码。以/开头，英文字母最多2000个字符")
	@NotBlank(message = "完整的菜单编码，即包含了父级菜单的编码。以/开头，英文字母不能为空")
	private String completeCode;

	/**
	* 顺序，值越小排越前面
	*/
	@NotNull(message = "顺序，值越小排越前面不能为空")
	private Integer order;

	/**
	* 描述
	*/
	@Length(max = 2000, message = "描述最多2000个字符")
	private String description;

}
