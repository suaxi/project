package com.software.properties;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Wang Hao
 * @date 2022/10/10 23:20
 */
public class ProjectProperties {

    public static Boolean ipLocal;

    @Value("${ip.local-parsing}")
    public void setIpLocal(Boolean ipLocal) {
        ProjectProperties.ipLocal = ipLocal;
    }
}
