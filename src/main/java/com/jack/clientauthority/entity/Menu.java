package com.jack.clientauthority.entity;

import lombok.*;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

/**
 * 应用菜单项
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-18 19:43:31
 */
@Data
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_menu")
public class Menu {
	
	/**
	* 主键id
	*/
	@TableId(value = "id", type = IdType.ASSIGN_UUID)
	private String id;

	/**
	* 父级菜单id，根节点id = 0
	*/
	private String parentId;

	/**
	* 菜单名称
	*/
	private String name;

	/**
	* 菜单编码，以/开头，英文字母
	*/
	private String code;

	/**
	* 完整的菜单编码，即包含了父级菜单的编码。以/开头，英文字母
	*/
	private String completeCode;

	/**
	* 顺序，值越小排越前面
	*/
	private Integer sort;

	/**
	* 描述
	*/
	private String description;

}
