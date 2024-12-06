package com.software.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.software.constant.StringConstant;
import com.software.dto.QueryRequest;
import com.software.entity.RouterMeta;
import com.software.entity.VueRouter;
import com.software.system.dto.MenuDto;
import com.software.system.entity.Menu;
import com.software.system.mapper.MenuMapper;
import com.software.system.service.MenuService;
import com.software.utils.SecurityUtils;
import com.software.utils.TreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
        //根节点
        if (menu.getPid().equals(0)) {
            menu.setPid(null);
        }
        menu.setCreateUser(SecurityUtils.getCurrentUsername());
        boolean flag = this.save(menu);
        if (menu.getPid() != null && flag) {
            //父节点子菜单数量处理
            this.dealParentMenuSubCount(menu.getPid());
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Menu menu) {
        //原pid
        Menu oldMenuData = this.getById(menu.getId());
        Integer pid = oldMenuData.getPid() == null ? null : oldMenuData.getPid();

        //根节点
        if (menu.getPid().equals(0)) {
            menu.setPid(null);
        }
        menu.setUpdateUser(SecurityUtils.getCurrentUsername());
        boolean flag = this.updateById(menu);
        //原父节点子菜单数量处理
        if (pid != null && flag) {
            this.dealParentMenuSubCount(pid);
        }
        //新父节点子菜单数量处理
        if (menu.getPid() != null && flag) {
            this.dealParentMenuSubCount(menu.getPid());
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(List<Integer> ids) {
        if (ids.size() > 0) {
            List<Menu> menuList = this.baseMapper.selectList(new LambdaQueryWrapper<Menu>().in(Menu::getId, ids));
            //删除叶子节点时同步更新父节点的子菜单数量
            Set<Integer> pidSet = menuList.stream().map(Menu::getPid).filter(Objects::nonNull).collect(Collectors.toSet());
            //删除父节点时同步删除子节点
            List<Integer> childrenIds = this.baseMapper.selectList(new LambdaQueryWrapper<Menu>().in(Menu::getPid, ids)).stream().map(Menu::getId).collect(Collectors.toList());
            boolean flag = this.removeByIds(ids);
            if (flag) {
                if (pidSet.size() > 0) {
                    for (Integer pid : pidSet) {
                        if (!ids.contains(pid)) {
                            Menu parentMenu = this.getById(pid);
                            int subCount = this.baseMapper.selectList(new LambdaQueryWrapper<Menu>().eq(Menu::getPid, pid)).size();
                            parentMenu.setSubCount(subCount);
                            this.updateById(parentMenu);
                        }
                    }
                }
                if (childrenIds.size() > 0) {
                    this.removeByIds(childrenIds);
                }
            }
            return flag;
        }
        return false;
    }

    @Override
    public Menu queryById(Integer id) {
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
    public Page<MenuDto> queryPage(Menu menu, QueryRequest queryRequest) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(menu.getTitle())) {
            queryWrapper.lambda().like(Menu::getTitle, menu.getTitle());
        } else {
            //默认查询一级菜单
            queryWrapper.lambda().isNull(Menu::getPid);
        }
        Page<Menu> page = this.page(new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize()), queryWrapper);
        List<MenuDto> menuDtoList = page.getRecords().stream().map(item -> {
            MenuDto menuDto = new MenuDto();
            BeanUtils.copyProperties(item, menuDto);
            return menuDto;
        }).collect(Collectors.toList());
        return new Page<MenuDto>().setRecords(menuDtoList).setSize(menuDtoList.size());
    }

    @Override
    public List<String> queryUserPermissionByUserId(Integer userId) {
        if (userId != null) {
            return menuMapper.queryUserPermissionByUserId(userId);
        }
        return null;
    }

    @Override
    public List<VueRouter<Menu>> getUserRouters(Integer userId) {
        List<VueRouter<Menu>> routers = new ArrayList<>();
        List<Menu> menuList = menuMapper.getUserRouters(userId, 2);
        if (menuList != null && menuList.size() > 0) {
            menuList.forEach(menu -> {
                VueRouter<Menu> router = new VueRouter<>();
                router.setId(menu.getId());
                router.setParentId(menu.getPid());
                router.setPath(menu.getPid() == null ? "/" + menu.getPath() : menu.getPath());
                router.setName(StringUtils.isNotEmpty(menu.getName()) ? menu.getName() : menu.getTitle());
                //是否为外链
                if (!menu.getIFrame().equals(StringConstant.TRUE)) {
                    if (menu.getPid() == null) {
                        router.setComponent(StringUtils.isEmpty(menu.getComponent()) ? "Layout" : menu.getComponent());
                    } else if (menu.getType() == 0) {
                        //如果不是一级菜单，且菜单类型为目录，则代表是多级菜单
                        router.setComponent(StringUtils.isEmpty(menu.getComponent()) ? "ParentView" : menu.getComponent());
                    } else if (StringUtils.isEmpty(menu.getComponent())) {
                        router.setComponent(menu.getComponent());
                    } else {
                        router.setComponent(menu.getComponent());
                    }
                }
                router.setMeta(new RouterMeta(menu.getTitle(), menu.getIcon(), !menu.getCache().equals(StringConstant.TRUE)));

                routers.add(router);
            });
            return TreeUtil.buildVueRouter(routers);
        }
        return Collections.emptyList();
    }

    @Override
    public List<MenuDto> queryChildListByPid(Integer pid) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        if (pid == null || pid == 0) {
            queryWrapper.isNull(Menu::getPid);
        } else {
            queryWrapper.eq(Menu::getPid, pid);
        }
        List<Menu> menuList = this.baseMapper.selectList(queryWrapper);
        if (menuList != null && menuList.size() > 0) {
            return menuList.stream().map(menu -> {
                MenuDto menuDto = new MenuDto();
                BeanUtils.copyProperties(menu, menuDto);
                return menuDto;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public Set<Menu> queryMenuListById(Integer id) {
        Set<Menu> menuSet = new HashSet<>();
        Menu menu = this.getById(id);
        menuSet.add(menu);
        List<MenuDto> menuDtoList = this.queryChildListByPid(id);
        if (menuDtoList != null && menuDtoList.size() > 0) {
            List<Menu> menuList = menuDtoList.stream().map(menuDto -> {
                Menu tempMenu = new Menu();
                BeanUtils.copyProperties(menuDto, tempMenu);
                return tempMenu;
            }).collect(Collectors.toList());
            this.queryChildMenuList(menuList, menuSet);
        }
        return menuSet;
    }

    @Override
    public List<MenuDto> querySameLevelAndSuperiorMenuListById(Integer id) {
        List<Menu> menuList = new ArrayList<>();
        Menu currentMenu = this.getById(id);
        menuList.add(currentMenu);

        //上级
        Menu superiorMenu = null;
        if (currentMenu.getPid() != null) {
            superiorMenu = this.getById(currentMenu.getPid());
            menuList.add(superiorMenu);
        }

        //同级
        if (superiorMenu != null) {
            List<Menu> sameLevelMenuList = this.baseMapper.selectList(new LambdaQueryWrapper<Menu>().eq(Menu::getPid, superiorMenu.getId()));
            menuList.addAll(sameLevelMenuList);
        }

        if (menuList != null && menuList.size() > 0) {
            return menuList.stream().map(menu -> {
                MenuDto menuDto = new MenuDto();
                BeanUtils.copyProperties(menu, menuDto);
                return menuDto;
            }).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 根据id向下递归查找子级菜单
     *
     * @param menuList 菜单列表
     * @param menuSet  查询结果集
     * @return 子级菜单列表
     */
    private Set<Menu> queryChildMenuList(List<Menu> menuList, Set<Menu> menuSet) {
        for (Menu menu : menuList) {
            menuSet.add(menu);
            List<Menu> menus = this.baseMapper.selectList(new LambdaQueryWrapper<Menu>().eq(Menu::getPid, menu.getId()));
            if (menus != null && menus.size() > 0) {
                queryChildMenuList(menus, menuSet);
            }
        }
        return menuSet;
    }

    /**
     * 父节点子菜单数量处理
     *
     * @param pid 父ID
     */
    private void dealParentMenuSubCount(Integer pid) {
        Menu parentMenu = this.queryById(pid);
        int subCount = this.baseMapper.selectList(new LambdaQueryWrapper<Menu>().eq(Menu::getPid, parentMenu.getId())).size();
        if (parentMenu.getSubCount() != subCount) {
            parentMenu.setSubCount(subCount);
            this.updateById(parentMenu);
        }
    }
}
