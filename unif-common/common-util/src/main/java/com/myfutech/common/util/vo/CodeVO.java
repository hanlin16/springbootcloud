package com.myfutech.common.util.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用于解析Post body中只有code的情况
 */
@Data
@ApiModel(value = "CodeVO",description = "编码")
public class CodeVO {

    public static CodeVO newCodeVO(String code){
        return new CodeVO(code);
    }
    @ApiModelProperty("编码")
    @NotBlank
    private String code;

    public CodeVO() {
    }

    public CodeVO(@NotNull String code) {
        this.code = code;
    }
}
