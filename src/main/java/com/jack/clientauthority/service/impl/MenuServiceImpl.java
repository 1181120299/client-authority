package com.jack.clientauthority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jack.clientauthority.mapper.MenuMapper;
import com.jack.clientauthority.entity.Menu;
import com.jack.clientauthority.service.MenuService;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    /**
     * 删除菜单，会级联删除下级菜单
     * @param id 主键ID
     * @return  true: 删除成功
     */
    @Override
    public boolean removeById(Serializable id) {
        Menu menu = baseMapper.selectById(id);
        if (menu == null) {
            return true;
        }

        int row = baseMapper.deleteById(menu);
        List<Menu> childMenuList = baseMapper.selectList(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId, id));
        if (CollectionUtils.isNotEmpty(childMenuList)) {
            childMenuList.forEach(child -> this.removeById(child.getId()));
        }

        return row > 0;
    }
}
