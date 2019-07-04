package com.myfutech.common.util.vo;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 用于解析Post body中只有两个属性的情况
 */
public class TwoParamVO<P1,P2> {

    public static <P1,P2> TwoParamVO<P1,P2> newTwoParamVO( P1 param1, P2 param2){
        return new TwoParamVO<>(param1, param2);
    }

    @ApiModelProperty("属性字段1")
    @NotNull
    private P1 param1;

    @ApiModelProperty("属性字段2")
    @NotNull
    private P2 param2;

    public TwoParamVO() {
    }

    public TwoParamVO(@NotNull P1 param1, @NotNull P2 param2) {
        this.param1 = param1;
        this.param2 = param2;
    }

    public P1 getParam1() {
        return param1;
    }

    public void setParam1(P1 param1) {
        this.param1 = param1;
    }

    public P2 getParam2() {
        return param2;
    }

    public void setParam2(P2 param2) {
        this.param2 = param2;
    }
}
