package com.myfutech.common.util.tree;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  树结构工具类
 */
@Slf4j
public class TreeUtils {

    /**
     * 构建树结构
     *
     * @param data      数据集合
     * @param adapter   适配器
     * @param <T>       最终树结构要携带的信息
     * @param <E>       原始数据
     * @return
     */
    public static <T,E> List<TreeVO<T>>  buildTree(Collection<E> data, TreeAdapter<T,E> adapter){
        if(CollectionUtils.isEmpty(data)){
            return Collections.emptyList();
        }
        return oneThreadDeal(data, adapter);

    }



    private static <T, E> List<TreeVO<T>> oneThreadDeal(Collection<E> data, TreeAdapter<T, E> adapter) {
        Map<Long, List<TreeVO<T>>> parentMap = new HashMap<>();
        for (E e : data) {
            Long parentId = adapter.getParentId(e);
            if (parentId == null){
                log.warn("节点 {} 没有找到parentId，丢弃", e);
                continue;
            }
            List<TreeVO<T>> childList =parentMap.computeIfAbsent(parentId, k ->new ArrayList<>());
            TreeVO<T> treeVO = adapter.getTreeNode(e);
            childList.add(treeVO);
        }
        List<TreeVO<T>> rootList = parentMap.remove(adapter.getRootId());
        dealChildList(parentMap, rootList);
        return rootList;
    }

    private static <T> void dealChildList(Map<Long, List<TreeVO<T>>> parentMap, List<TreeVO<T>> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            for (TreeVO<T> treeVO : list) {
                List<TreeVO<T>> treeList = parentMap.remove(treeVO.getId());
                if (CollectionUtils.isNotEmpty(treeList)) {
                    treeVO.setChildList(treeList);
                    dealChildList(parentMap, treeList);
                }
            }
        }
    }

    /**
     *  和单线程效率相差不大
     */
    private static <T, E> List<TreeVO<T>> parallelStreamDeal(Collection<E> data, TreeAdapter<T,E> adapter) {
        Map<Long, List<TreeVO<T>>> parentMap = new ConcurrentHashMap<>();
        data.parallelStream().forEach((e)-> {
            Long parentId = adapter.getParentId(e);
            if (parentId == null){
                log.warn("节点 {} 没有找到parentId，丢弃", e);
                return;
            }
            List<TreeVO<T>> childList = parentMap.computeIfAbsent(parentId, k ->Collections.synchronizedList(new LinkedList<>()));
            TreeVO<T> treeVO = adapter.getTreeNode(e);
            childList.add(treeVO);
        });
        List<TreeVO<T>> rootList = parentMap.remove(adapter.getRootId());
        parallelStreamDealChildList(parentMap, rootList);
        return rootList;
    }

    private static <T> void parallelStreamDealChildList(Map<Long, List<TreeVO<T>>> parentMap, List<TreeVO<T>> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            list.parallelStream().forEach((e)->{
                List<TreeVO<T>> treeList = parentMap.remove(e.getId());
                if (CollectionUtils.isNotEmpty(treeList)) {
                    e.setChildList(treeList);
                    dealChildList(parentMap, treeList);
                }
            });
        }
    }
}
