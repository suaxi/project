package com.software.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.software.system.entity.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/13 21:36
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户id查询用户菜单权限
     *
     * @param userId 用户id
     * @return
     */
    List<String> queryUserPermissionByUserId(Long userId);

    /**
     * 获取用户路由
     *
     * @param userId 用户名
     * @param type 菜单类型
     * @return 用户路由
     */
    List<Menu> getUserRouters(@Param("userId") Long userId, @Param("type") int type);
}
