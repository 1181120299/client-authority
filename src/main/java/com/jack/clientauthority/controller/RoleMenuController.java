package com.jack.clientauthority.controller;

import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;



import com.jack.clientauthority.entity.RoleMenu;
import com.jack.clientauthority.dto.RoleMenuDto;
import com.jack.clientauthority.service.RoleMenuService;

import com.jack.utils.web.R;

/**
 * 角色-菜单 对应关系
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:36
 */
@Validated
@RestController
@RequestMapping("/rolemenu")
public class RoleMenuController {

	@Resource
	private RoleMenuService roleMenuService;

	/**
	 * 信息
	 */
	@GetMapping("/info/{roleId}")
	public R info(@PathVariable("roleId") String roleId){
		RoleMenu roleMenu = roleMenuService.getById(roleId);
		
		return R.ok().setData(roleMenu);
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public R save(@RequestBody @Validated RoleMenuDto roleMenuDto){
		RoleMenu roleMenu = new RoleMenu();
		BeanUtils.copyProperties(roleMenuDto, roleMenu);
		roleMenuService.save(roleMenu);
		
		return R.ok();
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	public R update(@RequestBody RoleMenu roleMenu){
		roleMenuService.updateById(roleMenu);
		
		return R.ok();
	}

	/**
	 * 删除
	 */
	@GetMapping("/delete")
	public R delete(@NotNull(message = "roleId不能为空") String roleId){
		roleMenuService.removeById(roleId);
		
		return R.ok();
	}

	/**
	 * 列表
	 */
	@GetMapping("/page")
	public R page(@RequestParam(required = false, defaultValue = "1") Integer current,
				  @RequestParam(required = false, defaultValue = "10") Integer size) {
		IPage<RoleMenu> page = new Page<>(current, size);
		LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<RoleMenu>()
				.orderByDesc(RoleMenu::getRoleId);
		page = roleMenuService.page(page, wrapper);

		return R.ok().setData(page);
	}
}
