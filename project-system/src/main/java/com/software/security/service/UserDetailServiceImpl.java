package com.software.security.service;

import com.software.exception.BadRequestException;
import com.software.security.dto.LoginUserDto;
import com.software.system.entity.User;
import com.software.system.service.MenuService;
import com.software.system.service.RoleService;
import com.software.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/14 23:43
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserCacheManager userCacheManager;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        LoginUserDto loginUser = userCacheManager.getUserCache(s);
        if (loginUser == null) {
            User user = userService.queryByName(s);

            if (user == null) {
                throw new UsernameNotFoundException("用户名不存在");
            } else if (!user.getEnabled()) {
                throw new BadRequestException("账号未激活，请联系管理员");
            }

            //权限信息
            //TODO 根据当前用户部门id及数据权限查询对应的部门id
            List<Long> daScopeList = roleService.queryDataScopeByUserId(user.getId());
            //菜单权限
            List<String> permissionList = menuService.queryUserPermissionByUserId(user.getId());
            loginUser = new LoginUserDto(user, daScopeList, permissionList);
            //添加缓存
            userCacheManager.addUserCache(user.getUsername(), loginUser);
        }
        return loginUser;
    }
}
