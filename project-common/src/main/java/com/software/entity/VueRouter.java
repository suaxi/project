package com.software.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/16 22:46
 * @description Vue路由
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VueRouter<T> implements Serializable {

    @JsonIgnore
    private Integer id;
    @JsonIgnore
    private Integer parentId;

    private String path;
    private String name;
    private String component;
    private String redirect;
    private RouterMeta meta;
    private Boolean hidden = false;
    private Boolean alwaysShow = false;
    private List<VueRouter<T>> children;

    @JsonIgnore
    private Boolean hasParent = false;

    @JsonIgnore
    private Boolean hasChildren = false;

    public void initChildren() {
        this.children = new ArrayList<>();
    }

}
