package com.myfutech.common.util.vo.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "ProductRelatedInfoVO",description = "产品和销售部门信息")
public class ProductRelatedInfoVO {

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty("相关信息id")
    private List<RelatedInfoVO> relatedInfoList;
}
