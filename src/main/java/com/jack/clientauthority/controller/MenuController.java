package com.jack.clientauthority.controller;

import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;



import com.jack.clientauthority.entity.Menu;
import com.jack.clientauthority.dto.MenuDto;
import com.jack.clientauthority.service.MenuService;

import com.jack.utils.web.R;

/**
 * 应用菜单项
 * 
 * @author chenjiabao
 * @email 1181120299@qq.com
 * @date 2023-04-17 21:00:36
 */
@Validated
@RestController
@RequestMapping("/menu")
public class MenuController {

	@Resource
	private MenuService menuService;

	/**
	 * 信息
	 */
	@GetMapping("/info/{id}")
	public R info(@PathVariable("id") String id){
		Menu menu = menuService.getById(id);
		
		return R.ok().setData(menu);
	}

	/**
	 * 保存
	 */
	@PostMapping("/save")
	public R save(@RequestBody @Validated MenuDto menuDto){
		Menu menu = new Menu();
		BeanUtils.copyProperties(menuDto, menu);
		menuService.save(menu);
		
		return R.ok();
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	public R update(@RequestBody Menu menu){
		menuService.updateById(menu);
		
		return R.ok();
	}

	/**
	 * 删除
	 */
	@GetMapping("/delete")
	public R delete(@NotNull(message = "id不能为空") String id){
		menuService.removeById(id);
		
		return R.ok();
	}

	/**
	 * 列表
	 */
	@GetMapping("/page")
	public R page(@RequestParam(required = false, defaultValue = "1") Integer current,
				  @RequestParam(required = false, defaultValue = "10") Integer size) {
		IPage<Menu> page = new Page<>(current, size);
		LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<Menu>()
				.orderByDesc(Menu::getId);
		page = menuService.page(page, wrapper);

		return R.ok().setData(page);
	}
}
