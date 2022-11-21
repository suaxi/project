package com.software.security.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.software.system.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Wang Hao
 * @date 2022/10/14 23:31
 */
@Data
@NoArgsConstructor
public class LoginUserDto implements UserDetails {

    private User user;

    private List<Long> dataScopes;

    private List<String> permissions;

    private List<GrantedAuthority> authorities;

    public LoginUserDto(User user, List<Long> dataScopes, List<String> permissions) {
        this.user = user;
        this.dataScopes = dataScopes;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities != null && authorities.size() > 0) {
            return authorities;
        }
        authorities = permissions.stream().filter(StringUtils::isNotBlank).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @JSONField(serialize = false)
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
