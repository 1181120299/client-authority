package com.jack.clientauthority.processor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jack.clientauthority.annotation.NeedPermission;
import com.jack.clientauthority.entity.RolePermission;
import com.jack.clientauthority.entity.RoleUser;
import com.jack.clientauthority.mapper.RolePermissionMapper;
import com.jack.clientauthority.mapper.RoleUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Aspect
public class NeedPermissionProcessor {

    @Autowired
    private RoleUserMapper roleUserMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Pointcut("@annotation(com.jack.clientauthority.annotation.NeedPermission)")
    public void NeedPermission() {}

    @Before(value = "NeedPermission() && @annotation(annotation)")
    public void before(JoinPoint joinPoint, NeedPermission annotation) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("authentication = {}", authentication);

        // 获取切入点的方法签名，作为权限代码。
        String classReference = joinPoint.getSignature().getDeclaringType().getName();
        String methodName = joinPoint.getSignature().getName();
        String permissionCode = PermissionCodeGenerator.generate(classReference, methodName);
        // 根据当前用户名，查询是否有对应的权限代码。如果有，允许访问。否则抛出AccessDenyException
        List<RoleUser> roleUserList = roleUserMapper.selectList(new LambdaQueryWrapper<RoleUser>()
                .eq(RoleUser::getUsername, authentication.getName()));
        if (CollectionUtils.isEmpty(roleUserList)) {
            log.info("roleUserList is empty. the user: {} has not any role.", authentication.getName());
            throw new AccessDeniedException("用户没有设置角色");
        }

        List<String> roleIdList = roleUserList.stream()
                .map(RoleUser::getRoleId)
                .collect(Collectors.toList());
        List<RolePermission> rolePermissionList = rolePermissionMapper.selectList(new LambdaQueryWrapper<RolePermission>()
                .in(RolePermission::getRoleId, roleIdList));
        List<String> permissionCodeList = rolePermissionList.stream()
                .map(RolePermission::getPermissionCode)
                .toList();
        if (!permissionCodeList.contains(permissionCode)) {
            throw new AccessDeniedException("没有访问权限：" + annotation.value());
        }
    }
}
