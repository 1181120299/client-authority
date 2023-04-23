package com.jack.clientauthority.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jack.clientauthority.dto.RoleDto;
import com.jack.clientauthority.entity.Role;
import com.jack.clientauthority.entity.RolePermission;
import com.jack.clientauthority.entity.RoleUser;
import com.jack.clientauthority.processor.PermissionHelper;
import com.jack.clientauthority.service.RolePermissionService;
import com.jack.clientauthority.service.RoleService;
import com.jack.clientauthority.service.RoleUserService;
import com.jack.clientauthority.service.UserDetailService;
import com.jack.clientauthority.vo.Permission;
import com.jack.utils.web.R;
import jakarta.annotation.Resource;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
	@Autowired
	private RolePermissionService rolePermissionService;
	@Autowired
	private RoleUserService roleUserService;
	@Autowired
	private UserDetailService userDetailService;

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

	/**
	 * 返回角色用户分配页面
	 */
	@GetMapping("/associateUser/{id}")
	public String associateUser(@PathVariable("id") String id, Model model) {
		// 角色信息
		Role role = roleService.getById(id);
		model.addAttribute("role", role);
		return "associateUser";
	}

	/**
	 * 返回角色分配权限的页面
	 */
	@GetMapping("/associatePermission/{id}")
	public String associatePermission(@PathVariable("id") String id, Model model) {
		// 角色信息
		Role role = roleService.getById(id);
		model.addAttribute("role", role);
		return "associatePermission";
	}

	@GetMapping("/getPermissionTreeData")
	@ResponseBody
	public R getPermissionTreeData(@RequestParam String id) {
		// 所有权限信息
		List<Permission> allPermissionList = PermissionHelper.getAllPermissions();

		// 角色已分配的权限
		List<Permission> alreadyAssociatedPermissionList = getAlreadyAssociatedPermissionList(id, allPermissionList);

		// 生成ztree树形结构需要的简单数据
		List<TreeNode> treeNodeList = generatePermissionTreeData(allPermissionList, alreadyAssociatedPermissionList);

		return R.ok().setData(treeNodeList);
	}

	@GetMapping("/getRoleUser")
	@ResponseBody
	public R getRoleUser(@RequestParam String id) {
		List<RoleUser> roleUserList = roleUserService.list(new LambdaQueryWrapper<RoleUser>()
				.eq(RoleUser::getRoleId, id));
		List<String> usernameList = roleUserList.stream()
				.map(RoleUser::getUsername)
				.toList();
		return R.ok().setData(usernameList);
	}

	/**
	 * 获取所有的用户名
	 * @return	用户名集合
	 */
	@GetMapping("/getUserList")
	@ResponseBody
	public R getUserList() {
		List<String> usernameList = userDetailService.usernameList();
		return R.ok().setData(usernameList);
	}

	/**
	 * 保存为角色选择的用户
	 * @param paramMap	roleId：角色id。usernames：用户名，多个用英文逗号拼接
	 * @return	处理结果
	 */
	@PostMapping("/saveRoleUser")
	@ResponseBody
	public R saveRoleUser(@RequestBody Map<String, Object> paramMap) {
		String roleId = MapUtils.getString(paramMap, "roleId");
		String usernames = MapUtils.getString(paramMap, "usernames");

		String[] usernameArray = StringUtils.delimitedListToStringArray(usernames, ",");
		roleUserService.saveRoleUser(roleId, usernameArray);
		return R.ok();
	}

	/**
	 * 生成ztree树形结构需要的简单数据
	 * <p></p>
	 *
	 * ztree总共有三层节点：根节点-->class节点-->权限节点
	 *
	 * <p></p>
	 * @param allPermissionList	所有权限
	 * @param alreadyAssociatedPermissionList	角色已分配的权限
	 * @return	ztree树形结构需要的简单数据
	 */
	private List<TreeNode> generatePermissionTreeData(List<Permission> allPermissionList,
													  List<Permission> alreadyAssociatedPermissionList) {
		List<TreeNode> treeNodeList = new ArrayList<>();

		// 根节点
		TreeNode rootNode = new TreeNode();
		rootNode.setId("0");
		rootNode.setParentId("0");
		rootNode.setName("根节点");
		rootNode.setOpen(true);
		treeNodeList.add(rootNode);

		// class节点
		Map<String, List<Permission>> fromClassMap = allPermissionList.stream()
				.collect(Collectors.groupingBy(Permission::getFromClassName));

		fromClassMap.forEach((name, permissionList) -> {
			Permission permission = new Permission();
			if (CollectionUtils.isNotEmpty(permissionList)) {
				permission = permissionList.get(0);
			}

			TreeNode classNode = new TreeNode();
			classNode.setId(name);
			classNode.setParentId(rootNode.getId());

			String nodeName = permission.getFromClassSimpleName();
			if (StringUtils.hasText(permission.getFromClassDesc())) {
				nodeName += "(" + permission.getFromClassDesc() + ")";
			}

			classNode.setName(nodeName);
			classNode.setOpen(false);
			treeNodeList.add(classNode);
		});

		// 权限节点
		Map<String, Permission> codeMap = alreadyAssociatedPermissionList.stream()
				.collect(Collectors.toMap(Permission::getCode, Function.identity()));
		allPermissionList.forEach(permission -> {
			TreeNode permissionNode = new TreeNode();
			permissionNode.setId(permission.getCode());
			permissionNode.setParentId(permission.getFromClassName());
			permissionNode.setName(permission.getDescription());
			permissionNode.setChecked(codeMap.containsKey(permission.getCode()));
			treeNodeList.add(permissionNode);
		});

		// 如果直接子节点全部checked，则父节点也checked
		ensureTreeNodeChecked(treeNodeList, rootNode);
		return treeNodeList;
	}

	/**
	 * 如果直接子节点全部checked，则父节点也checked
	 * <p></p>
	 *
	 * 从上往下，由根节点找到枝叶。然后再从下往上，根据枝叶的所有兄弟节点是否都checked，决定枝叶的上一级节点是否checked
	 *
	 * <p></p>
	 *
	 * 如果一个节点，它的所有子节点都checked。那么它处于close状态。如果有子节点没有勾选，则处于open状态。
	 * 有一点例外：根节点一直处于open状态。
	 *
	 * <p></p>
	 * @param treeNodeList	所有的节点
	 * @param rootNode	根节点
	 */
	private void ensureTreeNodeChecked(List<TreeNode> treeNodeList, TreeNode rootNode) {
		Assert.notNull(rootNode, "rootNode can not be null");
		if (CollectionUtils.isEmpty(treeNodeList)) {
			return;
		}

		// 找到枝叶节点
		List<TreeNode> childNodeList = new ArrayList<>();
		findAllLeafNode(childNodeList, treeNodeList, rootNode);
		if (CollectionUtils.isEmpty(childNodeList)) {
			return;
		}

		// 根据父节点分组
		Map<String, List<TreeNode>> byParentMap = childNodeList.stream()
				.collect(Collectors.groupingBy(TreeNode::getParentId));
		Map<String, TreeNode> allNodesCache = treeNodeList.stream().collect(Collectors.toMap(TreeNode::getId, Function.identity()));
		ensureGroup(byParentMap, allNodesCache);

		rootNode.setOpen(true);
	}

	private void findAllLeafNode(List<TreeNode> childNodeList, List<TreeNode> allNodeCache, TreeNode parentNode) {
		List<TreeNode> myChildList = allNodeCache.stream()
				.filter(node -> !"0".equals(node.getId()))
				.filter(node -> parentNode.getId().equals(node.getParentId()))
				.toList();
		if (CollectionUtils.isEmpty(myChildList)) {
			childNodeList.add(parentNode);
			return;
		}

		myChildList.forEach(child -> {
			findAllLeafNode(childNodeList, allNodeCache, child);
		});
	}

	private void ensureGroup(Map<String, List<TreeNode>> byParentMap, Map<String, TreeNode> nodeCache) {
		byParentMap.forEach((parentId, childNodeList) -> {
			List<TreeNode> notCheckedList = childNodeList.stream()
					.filter(node -> !node.isChecked())
					.toList();
			TreeNode parentNode = nodeCache.get(parentId);
			if (parentNode != null) {
				if (CollectionUtils.isEmpty(notCheckedList)) {
					parentNode.setChecked(true);
					parentNode.setOpen(false);
				} else {
					parentNode.setOpen(true);
				}
			}
		});
		
		// 往上递归
		List<TreeNode> parentNodeList = new ArrayList<>();
		byParentMap.keySet().forEach(id -> {
			TreeNode parentNode = nodeCache.get(id);
			if (parentNode != null) {
				parentNodeList.add(parentNode);
			}
		});
		
		if (CollectionUtils.isEmpty(parentNodeList)) {
			return;
		}

		if (parentNodeList.size() == 1
				&& "0".equals(parentNodeList.get(0).getId())) {
			return;
		}

		Map<String, List<TreeNode>> grandfatherMap = parentNodeList.stream()
				.collect(Collectors.groupingBy(TreeNode::getParentId));
		this.ensureGroup(grandfatherMap, nodeCache);
	}

	// 获取角色已分配的权限
	private List<Permission> getAlreadyAssociatedPermissionList(String id, List<Permission> allPermissionList) {
		Assert.notNull(id, "id can not be null");

		if (Objects.isNull(allPermissionList)) {
			allPermissionList = Collections.emptyList();
		}

		Map<String, Permission> codeMap = allPermissionList.stream()
				.collect(Collectors.toMap(Permission::getCode, Function.identity()));

		List<RolePermission> rolePermissionList = rolePermissionService.list(new LambdaQueryWrapper<RolePermission>()
				.select(RolePermission::getPermissionCode)
				.eq(RolePermission::getRoleId, id));
		List<String> codeList = rolePermissionList.stream().map(RolePermission::getPermissionCode).toList();

		List<Permission> permissionList = new ArrayList<>();
		codeList.forEach(code -> {
			Permission targetPermission = codeMap.get(code);
			if (targetPermission != null) {
				permissionList.add(targetPermission);
			}
		});

		return permissionList;
	}


	@PostMapping("/savePermission")
	@ResponseBody
	public R savePermission(@RequestBody Map<String, Object> paramMap) {
		String roleId = MapUtils.getString(paramMap, "roleId");
		String permissionCodeStr = MapUtils.getString(paramMap, "permissionCodes");
		List<String> permissionCodeList = List.of(StringUtils.delimitedListToStringArray(permissionCodeStr, ","));

		rolePermissionService.savePermission(roleId, permissionCodeList);
		return R.ok();
	}

	@Data
	public static final class TreeNode{
		private String id;
		private String parentId;
		private String name;
		private boolean open;
		private boolean checked;
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
