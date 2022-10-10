package com.software.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Wang Hao
 * @date 2022/10/10 23:20
 */
@Data
@Component
public class ProjectProperties {

    public static Boolean ipLocal;

    @Value("${ip.local-parsing}")
    public void setIpLocal(Boolean ipLocal) {
        ProjectProperties.ipLocal = ipLocal;
    }
}
