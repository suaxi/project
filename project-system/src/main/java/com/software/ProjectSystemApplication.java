package com.software;

import com.software.utils.SpringContextHolder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Wang Hao
 * @date 2022/10/8 22:47
 */
@EnableSwagger2
@MapperScan({"com.software.mapper"})
@SpringBootApplication
public class ProjectSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectSystemApplication.class, args);
    }

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }
}
