package com.software.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/16 22:46
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tree<T> implements Serializable {

    private Integer id;

    private String label;

    private List<Tree<T>> children;

    private Integer parentId;

    private boolean hasParent = false;

    private boolean hasChildren = false;

    public void initChildren() {
        this.children = new ArrayList<>();
    }

}
