package com.myfutech.common.util.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用于解析Post body中键值队的列表信息
 */
@Data
@ApiModel(value = "CodeValueVO", description = "编码")
public class CodeValueVO {

    @ApiModelProperty("编码")
    @NotBlank
    private String code;

    @ApiModelProperty("名称")
    @NotBlank
    private String value;

    public static CodeValueVO newCodeValueVO(String code, String value) {
        return new CodeValueVO(code, value);
    }

    public CodeValueVO() {
    }

    public CodeValueVO(@NotNull String code, @NotBlank String value) {
        this.code = code;
        this.value = value;
    }
}
