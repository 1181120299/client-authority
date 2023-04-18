package com.jack.clientauthority.controller;

import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;



import com.jack.clientauthority.entity.RolePermission;
import com.jack.clientauthority.dto.RolePermissionDto;
import com.jack.clientauthority.service.RolePermissionService;

import com.jack.utils.web.R;

/**
 * 角色-权限 对应关系
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:36
 */
@Validated
@RestController
@RequestMapping("/rolepermission")
public class RolePermissionController {

	@Resource
	private RolePermissionService rolePermissionService;

	/**
	 * 信息
	 */
	@GetMapping("/info/{roleId}")
	public R info(@PathVariable("roleId") String roleId){
		RolePermission rolePermission = rolePermissionService.getById(roleId);
		
		return R.ok().setData(rolePermission);
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public R save(@RequestBody @Validated RolePermissionDto rolePermissionDto){
		RolePermission rolePermission = new RolePermission();
		BeanUtils.copyProperties(rolePermissionDto, rolePermission);
		rolePermissionService.save(rolePermission);
		
		return R.ok();
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	public R update(@RequestBody RolePermission rolePermission){
		rolePermissionService.updateById(rolePermission);
		
		return R.ok();
	}

	/**
	 * 删除
	 */
	@GetMapping("/delete")
	public R delete(@NotNull(message = "roleId不能为空") String roleId){
		rolePermissionService.removeById(roleId);
		
		return R.ok();
	}

	/**
	 * 列表
	 */
	@GetMapping("/page")
	public R page(@RequestParam(required = false, defaultValue = "1") Integer current,
				  @RequestParam(required = false, defaultValue = "10") Integer size) {
		IPage<RolePermission> page = new Page<>(current, size);
		LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<RolePermission>()
				.orderByDesc(RolePermission::getRoleId);
		page = rolePermissionService.page(page, wrapper);

		return R.ok().setData(page);
	}
}
