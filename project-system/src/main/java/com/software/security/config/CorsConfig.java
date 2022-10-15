package com.software.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Wang Hao
 * @date 2022/10/6 22:01
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                //允许跨域的路径
                .addMapping("/**")
                //允许跨域请求的域名
                .allowedOrigins("*")
                //是否允许cookie
                .allowCredentials(true)
                //请求方式
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                //header请求头
                .allowedHeaders("*")
                //跨域允许时间
                .maxAge(3600);
    }
}
