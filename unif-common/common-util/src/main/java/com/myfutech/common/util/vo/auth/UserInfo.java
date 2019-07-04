package com.myfutech.common.util.vo.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 已登录用户信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfo{

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户数据权限部门id集合")
    @Deprecated
    private Set<Long> dataPowerSectionIdSet;

    @ApiModelProperty("用户数据权限部门id集合")
    private Map<String,Set<Long>> dataPowerSectionIdMap;

    @ApiModelProperty("用户数据权限部门id集合,包含所有父级部门id")
    private Map<String,Set<Long>> dataPowerFullSectionIdMap;

    @ApiModelProperty("用户资源权限code集合")
    private Set<String> resourceCodeSet;

    @ApiModelProperty("员工id")
    private Long employeeId;

    @ApiModelProperty("员工编号")
    private String employeeCode;

    @ApiModelProperty("邮箱")
    private String workEmail;

    @ApiModelProperty("用户手机号")
    private String mobilePhone;

    @ApiModelProperty("员工图像")
    private String userImage;

    @ApiModelProperty("员工职位（多个采用,隔开）")
    private String position;

    @ApiModelProperty("用户管理的部门")
    private List<Long> managerSectionId;

    @ApiModelProperty("用户管理的业务部门")
    private List<Long> managerBusinessSectionId;

    @ApiModelProperty("用户的业务部门")
    private List<Long> businessSectionId;

    @ApiModelProperty("部门职位信息")
    private List<SectionPositionInfoVO> sectionPositionInfoList;

    @ApiModelProperty("当前部门职位信息")
    private SectionPositionInfoVO currentSectionPosition;

    @ApiModelProperty("用户及所在部门权限销售的产品")
    private Set<Long> productId;

    @ApiModelProperty("用户及所在部门权限销售的产品详情信息")
    private List<ProductRelatedInfoVO> productRelatedInfoList;

    @ApiModelProperty("用户编号")
    private String userCode;

    @ApiModelProperty("真实姓名")
    private String realName;

}
