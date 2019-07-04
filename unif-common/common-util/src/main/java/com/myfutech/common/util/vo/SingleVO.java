package com.myfutech.common.util.vo;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 用于解析Post body中只有一个属性的情况
 * @param <T>
 */
public class SingleVO<T> {

    public static <T> SingleVO<T> newSingleVO(T data){
        return new SingleVO<>(data);
    }

    @ApiModelProperty("属性字段")
    @NotNull
    private T data;

    public SingleVO() {
    }

    public SingleVO(T data) {
        this.data = data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
