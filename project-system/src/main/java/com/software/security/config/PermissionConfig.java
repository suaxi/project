package com.software.security.config;

import com.software.utils.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wang Hao
 * @date 2023/4/13 21:55
 */
@Service("pre")
public class PermissionConfig {

    public boolean check(String... permissions) {
        List<String> authorities = SecurityUtils.getCurrentUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return authorities.contains("admin") || Arrays.stream(permissions).anyMatch(authorities::contains);
    }
}
