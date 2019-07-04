package com.myfutech.common.util.tree;

public interface TreeAdapter<T,E> {

    Long getRootId();

    Long getParentId(E e);

    TreeVO<T> getTreeNode(E e);


}
