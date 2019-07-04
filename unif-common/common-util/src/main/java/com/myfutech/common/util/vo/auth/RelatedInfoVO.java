package com.myfutech.common.util.vo.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "RelatedInfoVO",description = "相关信息")
public class RelatedInfoVO {

    @ApiModelProperty("佣金规则id")
    private Long ruleId;

    @ApiModelProperty("相关id")
    private Long relatedId;
    @ApiModelProperty("相关类型")
    private String relatedTypeCode;
    @ApiModelProperty("相关名称")
    private String relatedName;
}
