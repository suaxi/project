package com.software.security.config;

import com.software.security.filter.JwtAuthenticationTokenFilter;
import com.software.security.handler.JwtAccessDeniedHandler;
import com.software.security.handler.JwtAuthenticationEntryPointHandler;
import com.software.security.properties.SecurityProperties;
import com.software.security.service.OnlineUserService;
import com.software.security.service.UserCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Wang Hao
 * @date 2022/10/15 13:44
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties properties;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private OnlineUserService onlineUserService;

    @Autowired
    private UserCacheManager userCacheManager;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private JwtAuthenticationEntryPointHandler jwtAuthenticationEntryPointHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        //去除 ROLE_ 前缀
        return new GrantedAuthorityDefaults("");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf
                .csrf().disable()
                //不通过session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //静态资源
                .antMatchers(HttpMethod.GET, "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/websocket/**").permitAll()
                //swagger
                .antMatchers("/doc.html", "/webjars/**", "/swagger-resources/**", "/v2/**").permitAll()
                //druid
                .antMatchers("/druid/**").permitAll()
                //认证授权接口
                .antMatchers("/auth/**").anonymous()
                .anyRequest().authenticated();

        //jwt认证过滤器
        JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter = new JwtAuthenticationTokenFilter(properties, tokenProvider, onlineUserService, userCacheManager);
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //认证授权处理器
        http.exceptionHandling()
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPointHandler);
    }
}
