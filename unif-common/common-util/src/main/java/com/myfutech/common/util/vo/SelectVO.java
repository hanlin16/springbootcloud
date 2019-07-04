package com.myfutech.common.util.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SelectVO",description = "下拉框选择")
public class SelectVO<K1,K2> {

    public static <K1,K2>  SelectVO<K1,K2>  newSelectVO(K1 name, K2 value){
        return new SelectVO<>(name, value);
    }

    @ApiModelProperty("对应的展示值")
    private K1 name;
    @ApiModelProperty("对应的传值")
    private K2 value;

    public SelectVO() {
    }

    public SelectVO(K1 name, K2 value) {
        this.name = name;
        this.value = value;
    }
}
