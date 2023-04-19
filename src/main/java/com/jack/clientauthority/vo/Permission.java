package com.jack.clientauthority.vo;

import lombok.Data;

@Data
public class Permission {
    /**
     * 权限代码
     */
    private String code;
    /**
     * 权限描述
     */
    private String description;
    /**
     * 来源的类名
     */
    private String fromClassName;
    /**
     * 来源的类名，简单名称
     */
    private String fromClassSimpleName;
    /**
     * 来源的类的描述，如果该类上有swagger的注解@Api，则取注解的描述信息
     */
    private String fromClassDesc;
}
