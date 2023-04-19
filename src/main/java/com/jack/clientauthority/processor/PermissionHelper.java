package com.jack.clientauthority.processor;

import com.jack.clientauthority.annotation.NeedPermission;
import com.jack.clientauthority.vo.Permission;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class PermissionHelper implements ApplicationContextAware {

    private static ApplicationContext ac;

    private static List<Permission> PERMISSION_CACHE = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
    }

    /**
     * 获取应用定义的所有权限
     * <p></p>
     *
     * 此方法第一次执行的时候需要遍历spring管理的所有bean，会比较耗时。
     * 后续访问直接从缓存取数据会快很多。
     *
     * <p></p>
     *
     * @return  权限集合
     */
    public static List<Permission> getAllPermissions() {
        if (CollectionUtils.isNotEmpty(PERMISSION_CACHE)) {
            log.debug("get permission from cache");
            return PERMISSION_CACHE;
        }

        long startTime = System.currentTimeMillis();

        List<Permission> permissionList = new ArrayList<>();
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            Object beanObj = ac.getBean(beanName);
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(beanObj.getClass());
            for(Method method : methods) {
                NeedPermission needPermission = method.getAnnotation(NeedPermission.class);
                if (Objects.isNull(needPermission)) {
                    continue;
                }

                String takeOutText = "$$SpringCGLIB$$0";
                Permission permission = new Permission();
                String classReference = beanObj.getClass().getName().replace(takeOutText, "");
                String code = PermissionCodeGenerator.generate(classReference, method.getName());
                permission.setCode(code);
                permission.setDescription(needPermission.value());
                permission.setFromClassName(beanObj.getClass().getName().replace(takeOutText, ""));
                permission.setFromClassSimpleName(beanObj.getClass().getSimpleName().replace(takeOutText, ""));

                Api api = beanObj.getClass().getAnnotation(Api.class);
                if (api != null) {
                    String[] tags = api.tags();
                    if (tags != null && tags.length > 0) {
                        permission.setFromClassDesc(StringUtils.arrayToDelimitedString(tags, ", "));
                    } else if (StringUtils.hasText(api.value())) {
                        permission.setFromClassDesc(api.value());
                    } else {
                        log.info("@Api has no description message. class = {}", beanObj.getClass().getName());
                    }
                }

                permissionList.add(permission);
            }
        }

        long endTime = System.currentTimeMillis();
        log.info("get permission by spring bean. cost time: {}ms", (endTime - startTime));

        PERMISSION_CACHE = permissionList;
        return permissionList;
    }
}
