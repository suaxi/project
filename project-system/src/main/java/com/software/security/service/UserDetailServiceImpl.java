package com.software.security.service;

import com.software.exception.BadRequestException;
import com.software.security.dto.LoginUserDto;
import com.software.system.entity.User;
import com.software.system.service.DataScopeService;
import com.software.system.service.MenuService;
import com.software.system.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wang Hao
 * @date 2022/10/14 23:43
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private DataScopeService dataScopeService;

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

            //数据权限信息
            List<Integer> daScopeList = dataScopeService.getDeptIds(user);
            //菜单权限
            List<String> permissionList = menuService.queryUserPermissionByUserId(user.getId()).stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
            loginUser = new LoginUserDto(user, daScopeList, permissionList);
            //添加缓存
            userCacheManager.addUserCache(user.getUsername(), loginUser);
        }
        return loginUser;
    }
}
