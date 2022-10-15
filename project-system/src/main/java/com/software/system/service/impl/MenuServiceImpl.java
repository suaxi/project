package com.software.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.constant.StringConstant;
import com.software.dto.QueryRequest;
import com.software.system.entity.Menu;
import com.software.system.mapper.MenuMapper;
import com.software.system.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:38
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(Menu menu) {
        //TODO 创建人信息
        return this.save(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Menu menu) {
        return this.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long[] ids) {
        if (ids.length > 0) {
            return this.removeByIds(Arrays.asList(ids));
        }
        return false;
    }

    @Override
    public Menu queryById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("菜单id不能为空");
        }
        return this.getById(id);
    }

    @Override
    public Menu queryByName(String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.lambda().eq(Menu::getName, name);
        }
        return this.getOne(queryWrapper);
    }

    @Override
    public List<Menu> queryList() {
        return this.list();
    }

    @Override
    public Page<Menu> queryPage(Menu menu, QueryRequest queryRequest) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (menu.getType() != null) {
            queryWrapper.lambda().eq(Menu::getType, menu.getType());
        }
        if (StringUtils.isNotBlank(menu.getTitle())) {
            queryWrapper.lambda().eq(Menu::getTitle, menu.getTitle());
        }
        if (StringUtils.isNotBlank(menu.getName())) {
            queryWrapper.lambda().eq(Menu::getName, menu.getName());
        }
        if (menu.getSort() != null) {
            queryWrapper.lambda().eq(Menu::getSort, menu.getSort());
        }
        if (StringUtils.isNotBlank(queryRequest.getOrder())) {
            queryWrapper.orderBy(true, StringConstant.ASC.equals(queryRequest.getOrder()), queryRequest.getField());
        } else {
            queryWrapper.orderBy(true, false, "create_time");
        }
        Page<Menu> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return this.page(page, queryWrapper);
    }

    @Override
    public List<String> queryUserPermissionByUserId(Long userId) {
        if (userId != null) {
            return menuMapper.queryUserPermissionByUserId(userId);
        }
        return null;
    }
}
