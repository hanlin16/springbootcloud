package com.myfutech.common.util.tree;

import com.myfutech.common.util.vo.SelectVO;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;

import static com.myfutech.common.util.tree.TreeUtils.buildTree;

public class TreeUtilsTest {
    public static void main(String[] args) {
        List<SelectVO<Long, Long>> list = new ArrayList<>(10000000);
        for (int i = 0; i < 10000000; i++) {
            SelectVO<Long, Long> selectVO = SelectVO.newSelectVO((long)i, (long)i);
            list.add(selectVO);
        }
        long start = System.currentTimeMillis();
        List<TreeVO<Long>> list1 = buildTree(list, new TreeAdapter<Long, SelectVO<Long,Long>>() {
            @Override
            public Long getRootId() {
                return 0L;
            }

            @Override
            public Long getParentId(SelectVO<Long, Long> longLongSelectVO) {
                return RandomUtils.nextLong(0, longLongSelectVO.getValue() % 20);
            }

            @Override
            public TreeVO<Long> getTreeNode(SelectVO<Long, Long> longLongSelectVO) {
                return new TreeVO<Long>(longLongSelectVO.getValue(), longLongSelectVO.getValue() + "", longLongSelectVO.getName());
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("cost time: " + (end - start) + " ms");
    }
}
