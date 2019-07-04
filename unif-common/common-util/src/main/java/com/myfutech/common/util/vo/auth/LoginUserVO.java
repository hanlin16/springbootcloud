package com.myfutech.common.util.vo.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LoginUserVO {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户编号")
    private String userCode;

    @ApiModelProperty("员工id")
    private Long employeeId;

    @ApiModelProperty("员工编号")
    private String employeeCode;

    @ApiModelProperty("员工职位（多个采用,隔开）")
    private String position;

    @ApiModelProperty("用户姓名")
    private String realName;

    @ApiModelProperty("用户数据权限部门id集合")
    private Set<Long> dataPowerSectionIdSet;

    @ApiModelProperty("用户数据权限部门id集合,包含所有父级部门id")
    private Set<Long> dataPowerFullSectionIdSet;

    @ApiModelProperty("用户及所在部门权限销售的产品")
    private Set<Long> productIdSet;
}
