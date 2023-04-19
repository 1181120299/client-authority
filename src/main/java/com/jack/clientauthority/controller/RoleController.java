package com.jack.clientauthority.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jack.clientauthority.dto.RoleDto;
import com.jack.clientauthority.entity.Role;
import com.jack.clientauthority.service.RoleService;
import com.jack.utils.web.R;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 应用角色
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:36
 */
@Validated
@Controller
@RequestMapping("/role")
public class RoleController {

	@Resource
	private RoleService roleService;

	/**
	 * 列表
	 */
	@GetMapping("/page")
	public String page(@RequestParam(required = false, defaultValue = "1") Integer current,
					   @RequestParam(required = false, defaultValue = "10") Integer size,
					   Model model) {
		IPage<Role> page = new Page<>(current, size);
		LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<Role>()
				.orderByDesc(Role::getId);
		page = roleService.page(page, wrapper);

		model.addAttribute("page", page);
		return "role";
	}

	@GetMapping("/toSave")
	public String toSave() {
		return "createRole";
	}

	/**
	 * 检查角色信息是否已经存在
	 * @param text	要检查的文本（角色名称、角色编码）
	 * @param type	类型（name：角色名称。code：角色编码）
	 * @return	2000：可以使用。2999：已存在
	 */
	@GetMapping("/checkRole")
	@ResponseBody
	public R checkRole(String text, String type) {
		roleService.checkRole(text, type);
		return R.ok("可以使用");
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	@ResponseBody
	public R save(@RequestBody @Validated RoleDto roleDto){
		Role role = new Role();
		BeanUtils.copyProperties(roleDto, role);
		roleService.save(role);

		return R.ok();
	}

	@GetMapping("/toUpdate/{id}")
	public String toUpdate(@PathVariable("id") String id, Model model) {
		Role role = roleService.getById(id);
		model.addAttribute("role", role);
		return "updateRole";
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ResponseBody
	public R update(@RequestBody Role role){
		roleService.updateById(role);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@GetMapping("/delete")
	@ResponseBody
	public R delete(@NotNull(message = "id不能为空") String id){
		roleService.removeById(id);

		return R.ok();
	}

//	/**
//	 * 信息
//	 */
//	@GetMapping("/info/{id}")
//	public R info(@PathVariable("id") String id){
//		Role role = roleService.getById(id);
//
//		return R.ok().setData(role);
//	}
//
//	/**
//	 * 保存
//	 */
//	@PostMapping("/save")
//	public R save(@RequestBody @Validated RoleDto roleDto){
//		Role role = new Role();
//		BeanUtils.copyProperties(roleDto, role);
//		roleService.save(role);
//
//		return R.ok();
//	}
//
//	/**
//	 * 修改
//	 */
//	@PostMapping("/update")
//	public R update(@RequestBody Role role){
//		roleService.updateById(role);
//
//		return R.ok();
//	}
//
//	/**
//	 * 删除
//	 */
//	@GetMapping("/delete")
//	public R delete(@NotNull(message = "id不能为空") String id){
//		roleService.removeById(id);
//
//		return R.ok();
//	}
}
