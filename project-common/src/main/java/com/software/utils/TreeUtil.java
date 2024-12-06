package com.software.utils;

import com.software.dto.Tree;
import com.software.entity.VueRouter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wang Hao
 * @date 2022/10/16 22:46
 * @description 树形结构工具类
 */
public class TreeUtil {

    /**
     * 用于构建菜单或部门树
     *
     * @param nodes nodes
     * @return <T> List<? extends Tree>
     */
    public static <T> List<? extends Tree<?>> build(List<? extends Tree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<>();

        ou: for (Tree<T> node : nodes) {
            Integer pid = node.getParentId();
            if (pid == null) {
                topNodes.add(node);
                continue ou;
            }
            in: for (Tree<T> n : nodes) {
                Integer id = n.getId();
                if (id != null && id.equals(pid)) {
                    if (n.getChildren() == null) {
                        n.initChildren();
                    }
                    n.getChildren().add(node);
                    node.setHasParent(true);
                    n.setHasChildren(true);
                    n.setHasParent(true);
                    continue ou;
                }
            }
            if (topNodes.isEmpty()) {
                topNodes.add(node);
            }
        }
        return topNodes;
    }


    /**
     * 构造前端路由
     *
     * @param routes routes
     * @param <T>    T
     * @return ArrayList<VueRouter < T>>
     */
    public static <T> List<VueRouter<T>> buildVueRouter(List<VueRouter<T>> routes) {
        if (routes == null) {
            return null;
        }
        List<VueRouter<T>> topRoutes = new ArrayList<>();
        VueRouter<T> router = new VueRouter<>();
        routes.forEach(route -> {
            Integer parentId = route.getParentId();
            if (parentId == null) {
                topRoutes.add(route);
                return;
            }
            for (VueRouter<T> parent : routes) {
                Integer id = parent.getId();
                if (id != null && id.equals(parentId)) {
                    if (parent.getChildren() == null) {
                        parent.initChildren();
                    }
                    parent.getChildren().add(route);
                    parent.setAlwaysShow(true);
                    parent.setRedirect("noredirect");
                    parent.setHasChildren(true);
                    route.setHasParent(true);
                    parent.setHasParent(true);
                    return;
                }
            }
        });
        //VueRouter<T> router404 = new VueRouter<>();
        //router404.setName("404");
        //router404.setComponent("error-page/404");
        //router404.setPath("*");
        //
        //topRoutes.add(router404);
        return topRoutes;
    }
}