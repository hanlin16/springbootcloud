package com.myfutech.common.util.vo.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "CommonUserInfo",description = "用户基本信息")
public class CommonUserInfo {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("员工编号")
    private String employeeCode;

    @ApiModelProperty("员工图像")
    private String userImage;

    @ApiModelProperty("部门职位信息")
    private List<SectionPositionInfoVO> sectionPositionInfoList;

    @ApiModelProperty("员工id")
    private Long employeeId;

    @ApiModelProperty("用户编号")
    private String userCode;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("是否是总监团队负责人")
    private Boolean isTeamManager;

}
