package com.myfutech.common.util.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@Data
public class TreeVO<T> {
    @ApiModelProperty("当前节点id")
    private Long id;
    @ApiModelProperty("当前节点名")
    private String name;
    @ApiModelProperty("当前节点下附加信息")
    private T info;
    @ApiModelProperty("当前节点的下级节点")
    private List<TreeVO<T>> childList;

    public TreeVO() {
    }

    public TreeVO(Long id, String name, T info) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.childList = new ArrayList<>();
    }
}
