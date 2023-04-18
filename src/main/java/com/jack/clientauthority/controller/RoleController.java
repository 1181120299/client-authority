package com.jack.clientauthority.controller;

import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;



import com.jack.clientauthority.entity.Role;
import com.jack.clientauthority.dto.RoleDto;
import com.jack.clientauthority.service.RoleService;

import com.jack.utils.web.R;

/**
 * 应用角色
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:36
 */
@Validated
@RestController
@RequestMapping("/role")
public class RoleController {

	@Resource
	private RoleService roleService;

	/**
	 * 信息
	 */
	@GetMapping("/info/{id}")
	public R info(@PathVariable("id") String id){
		Role role = roleService.getById(id);
		
		return R.ok().setData(role);
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public R save(@RequestBody @Validated RoleDto roleDto){
		Role role = new Role();
		BeanUtils.copyProperties(roleDto, role);
		roleService.save(role);
		
		return R.ok();
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	public R update(@RequestBody Role role){
		roleService.updateById(role);
		
		return R.ok();
	}

	/**
	 * 删除
	 */
	@GetMapping("/delete")
	public R delete(@NotNull(message = "id不能为空") String id){
		roleService.removeById(id);
		
		return R.ok();
	}

	/**
	 * 列表
	 */
	@GetMapping("/page")
	public R page(@RequestParam(required = false, defaultValue = "1") Integer current,
				  @RequestParam(required = false, defaultValue = "10") Integer size) {
		IPage<Role> page = new Page<>(current, size);
		LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<Role>()
				.orderByDesc(Role::getId);
		page = roleService.page(page, wrapper);

		return R.ok().setData(page);
	}
}
