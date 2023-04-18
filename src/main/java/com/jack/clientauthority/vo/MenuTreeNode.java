package com.jack.clientauthority.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 菜单ztree树节点 简单数据
 */
@Data
@ToString
public class MenuTreeNode {
    private String id;
    private String parentId;
    private String name;
    private boolean open;
}
