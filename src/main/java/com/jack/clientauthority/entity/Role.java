package com.jack.clientauthority.entity;

import lombok.*;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

/**
 * 应用角色
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
@TableName("t_role")
public class Role {
	
	/**
	* 主键
	*/
	@TableId(value = "id", type = IdType.ASSIGN_UUID)
	private String id;

	/**
	* 角色名称，不可重复
	*/
	private String name;

	/**
	* 角色编码，不可重复
	*/
	private String code;

	/**
	* 描述
	*/
	private String description;

}
