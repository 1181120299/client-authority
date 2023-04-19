package com.jack.clientauthority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jack.utils.web.RRException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.jack.clientauthority.mapper.MenuMapper;
import com.jack.clientauthority.entity.Menu;
import com.jack.clientauthority.service.MenuService;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public boolean save(Menu entity) {
        if (!entity.getCode().startsWith("/")) {
            entity.setCode("/" + entity.getCode());
        }

        if (entity.getSort() == null) {
            entity.setSort(0);
        }

        // 根据父级菜单，拼接完整的菜单编码
        String completeCode = generateCompleteCodeByParent(entity.getParentId(), entity.getCode());
        entity.setCompleteCode(completeCode);
        int row = baseMapper.insert(entity);
        return row > 0;
    }

    /**
     * 根据父级菜单，拼接完整的菜单编码
     * @param parentId  父级菜单id
     * @param code  当前菜单的编码
     * @return  完整的菜单编码
     */
    private String generateCompleteCodeByParent(String parentId, String code) {
        if ("0".equals(parentId)) {
            return code;
        }

        Menu parentMenu = baseMapper.selectById(parentId);
        if (parentMenu == null) {
            throw new RRException("父级菜单不存在");
        }

        return StringUtils.hasText(parentMenu.getCompleteCode())
                ? parentMenu.getCompleteCode() + code
                : code;
    }

    @Override
    public boolean updateById(Menu entity) {
        if (!entity.getCode().startsWith("/")) {
            throw new RRException("菜单编码需要以斜杠开头");
        }

        // 根据父级菜单，拼接完整的菜单编码
        String completeCode = generateCompleteCodeByParent(entity.getParentId(), entity.getCode());
        entity.setCompleteCode(completeCode);
        int row = baseMapper.updateById(entity);

        // 修改所有子菜单的完整菜单编码
        List<Menu> allMenuCacheList = baseMapper.selectList(new LambdaQueryWrapper<>());
        modifyChildMenuCompleteCode(entity.getId(), completeCode, allMenuCacheList);

        return row > 0;
    }

    /**
     * 修改子菜单的完整菜单编码
     *
     * @param parentId               父级菜单id
     * @param parentCompleteCode      父级菜单编码
     * @param allMenuCacheList 所有菜单的缓存集合
     */
    private void modifyChildMenuCompleteCode(String parentId, String parentCompleteCode, List<Menu> allMenuCacheList) {
        Assert.notNull(parentId, "parentId can not be null");
        Assert.notNull(parentCompleteCode, "parentCompleteCode can not be null");

        List<Menu> childMenuList = allMenuCacheList.stream()
                .filter(childMenu -> parentId.equals(childMenu.getParentId()))
                .toList();
        if (CollectionUtils.isEmpty(childMenuList)) {
            return;
        }

        childMenuList.forEach(childMenu -> {
            String childCompleteCode = parentCompleteCode + childMenu.getCode();
            childMenu.setCompleteCode(childCompleteCode);
            baseMapper.updateById(childMenu);

            modifyChildMenuCompleteCode(childMenu.getId(), childMenu.getCompleteCode(), allMenuCacheList);
        });
    }
}
