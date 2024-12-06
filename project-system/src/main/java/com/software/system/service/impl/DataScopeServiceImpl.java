package com.software.system.service.impl;

import com.software.enums.DataScopeEnum;
import com.software.system.dto.DeptDto;
import com.software.system.entity.Dept;
import com.software.system.entity.Role;
import com.software.system.entity.User;
import com.software.system.service.DataScopeService;
import com.software.system.service.DeptService;
import com.software.system.service.RoleDeptService;
import com.software.system.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Wang Hao
 * @date 2022/11/21 21:48
 */
@Service
public class DataScopeServiceImpl implements DataScopeService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleDeptService roleDeptService;

    @Override
    public List<Integer> getDeptIds(User user) {
        Set<Integer> deptIds = new HashSet<>();
        //当前用户角色
        List<Role> roleList = roleService.queryRoleListByUserId(user.getId());
        //角色对应的部门id
        for (Role role : roleList) {
            DataScopeEnum dataScopeEnum = DataScopeEnum.parseOf(role.getDataScope());
            switch (Objects.requireNonNull(dataScopeEnum)) {
                case ALL:
                    return new ArrayList<>(deptIds);
                case THIS_LEVEL:
                    deptIds.add(user.getDeptId());
                    break;
                case CUSTOMIZE:
                    deptIds.addAll(this.getCustomize(deptIds, role));
                    break;
                default:
                    break;
            }
        }
        return new ArrayList<>(deptIds);
    }

    /**
     * 查找自定义权限范围
     *
     * @param deptIds 部门id集合
     * @param role    角色信息
     * @return 自定义权限范围集合
     */
    private Set<Integer> getCustomize(Set<Integer> deptIds, Role role) {
        //当前角色对应的部门信息
        Set<Dept> deptSet = roleDeptService.queryByRoleId(role.getId());
        if (deptSet != null && deptSet.size() > 0) {
            for (Dept dept : deptSet) {
                deptIds.add(dept.getId());
                //下级部门信息（配置角色对应的数据权限时只需配置最高级部门即可获取其所有下级部门信息）
                List<DeptDto> deptDtoList = deptService.queryChildListByPid(dept.getId());
                if (deptDtoList != null && deptDtoList.size() > 0) {
                    List<Dept> deptList = deptDtoList.stream().map(deptDto -> {
                        Dept tempDept = new Dept();
                        BeanUtils.copyProperties(deptDto, tempDept);
                        return tempDept;
                    }).collect(Collectors.toList());
                    deptIds.addAll(deptService.queryDeptChildren(deptList).stream().map(Dept::getId).collect(Collectors.toList()));
                }
            }
        }
        return deptIds;
    }
}
