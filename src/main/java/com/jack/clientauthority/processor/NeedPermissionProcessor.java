package com.jack.clientauthority.processor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jack.clientauthority.annotation.ClientAccessUsernameStrategy;
import com.jack.clientauthority.annotation.NeedPermission;
import com.jack.clientauthority.entity.RolePermission;
import com.jack.clientauthority.entity.RoleUser;
import com.jack.clientauthority.mapper.RolePermissionMapper;
import com.jack.clientauthority.mapper.RoleUserMapper;
import com.jack.clientauthority.utils.WebClientHelper;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
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
        String username = getUsername();

        // 获取切入点的方法签名，作为权限代码。
        String classReference = joinPoint.getSignature().getDeclaringType().getName();
        String methodName = joinPoint.getSignature().getName();
        String permissionCode = PermissionCodeGenerator.generate(classReference, methodName);
        // 根据当前用户名，查询是否有对应的权限代码。如果有，允许访问。否则抛出AccessDenyException
        List<RoleUser> roleUserList = roleUserMapper.selectList(new LambdaQueryWrapper<RoleUser>()
                .eq(RoleUser::getUsername, username));
        if (CollectionUtils.isEmpty(roleUserList)) {
            log.info("The roleUserList is empty. the user: {} has not any role.", username);
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
            throw new AccessDeniedException("您没有访问权限：" + annotation.value());
        }
    }

    // 获取用户名
    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
