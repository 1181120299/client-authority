package com.jack.clientauthority.vo;

import com.jack.clientauthority.entity.Menu;
import lombok.Data;
import lombok.ToString;

/**
 * 菜单ztree树节点 简单数据
 */
@Data
@ToString
public class MenuTreeNode extends Menu {
    private boolean open;
}
