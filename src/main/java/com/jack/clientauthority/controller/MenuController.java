package com.jack.clientauthority.controller;

import com.jack.clientauthority.vo.MenuTreeNode;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

	@GetMapping("/getMenuTreeData")
	@ResponseBody
	public R getMenuTreeData(String openId) {
		List<Menu> menuList = menuService.list();
		List<MenuTreeNode> treeNodeList = new ArrayList<>(menuList.size());
		menuList.forEach(menu -> {
			MenuTreeNode treeNode = new MenuTreeNode();
			treeNode.setId(menu.getId());
			treeNode.setParentId(menu.getParentId());
			treeNode.setName(menu.getName());

			treeNodeList.add(treeNode);
		});

		MenuTreeNode rootNode = new MenuTreeNode();
		rootNode.setId("0");
		rootNode.setParentId("0");
		rootNode.setName("根节点");
		rootNode.setOpen(true);
		treeNodeList.add(rootNode);

		// 递归设置需要open的节点
		setOpenNode(treeNodeList, openId);

		return R.ok().setData(treeNodeList);
	}

	/**
	 * 递归设置需要open的节点
	 * @param treeNodeList	所有节点的数据
	 * @param openId	需要open的节点id
	 */
	private void setOpenNode(List<MenuTreeNode> treeNodeList, String openId) {
		if (CollectionUtils.isEmpty(treeNodeList) || StringUtils.isEmpty(openId)) {
			return;
		}

		List<MenuTreeNode> targetNodeList = treeNodeList.stream()
				.filter(node -> node.getId().equals(openId))
				.toList();
		if (CollectionUtils.isEmpty(targetNodeList)) {
			return;
		}

		MenuTreeNode targetNode = targetNodeList.get(0);
		targetNode.setOpen(true);

		if ("0".equals(targetNode.getParentId())) {
			return;
		}

		setOpenNode(treeNodeList, targetNode.getParentId());
	}

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
	 * <p></p>
	 * 会级联删除下级菜单
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
