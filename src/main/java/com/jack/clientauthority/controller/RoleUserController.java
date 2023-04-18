package com.jack.clientauthority.controller;

import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;



import com.jack.clientauthority.entity.RoleUser;
import com.jack.clientauthority.dto.RoleUserDto;
import com.jack.clientauthority.service.RoleUserService;

import com.jack.utils.web.R;

/**
 * 用户-角色 对应关系
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:35
 */
@Validated
@RestController
@RequestMapping("/roleuser")
public class RoleUserController {

	@Resource
	private RoleUserService roleUserService;

	/**
	 * 信息
	 */
	@GetMapping("/info/{username}")
	public R info(@PathVariable("username") String username){
		RoleUser roleUser = roleUserService.getById(username);
		
		return R.ok().setData(roleUser);
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public R save(@RequestBody @Validated RoleUserDto roleUserDto){
		RoleUser roleUser = new RoleUser();
		BeanUtils.copyProperties(roleUserDto, roleUser);
		roleUserService.save(roleUser);
		
		return R.ok();
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	public R update(@RequestBody RoleUser roleUser){
		roleUserService.updateById(roleUser);
		
		return R.ok();
	}

	/**
	 * 删除
	 */
	@GetMapping("/delete")
	public R delete(@NotNull(message = "username不能为空") String username){
		roleUserService.removeById(username);
		
		return R.ok();
	}

	/**
	 * 列表
	 */
	@GetMapping("/page")
	public R page(@RequestParam(required = false, defaultValue = "1") Integer current,
				  @RequestParam(required = false, defaultValue = "10") Integer size) {
		IPage<RoleUser> page = new Page<>(current, size);
		LambdaQueryWrapper<RoleUser> wrapper = new LambdaQueryWrapper<RoleUser>()
				.orderByDesc(RoleUser::getUsername);
		page = roleUserService.page(page, wrapper);

		return R.ok().setData(page);
	}
}
