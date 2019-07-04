package com.myfutech.common.util.vo.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "SectionPositionInfoVO",description = "部门职位信息")
public class SectionPositionInfoVO {

    @ApiModelProperty("部门id")
    private Long sectionId;

    @ApiModelProperty("部门名称")
    private String sectionName;

    @ApiModelProperty("部门类型编码")
    private String sectionTypeCode;

    @ApiModelProperty("上级部门id")
    private Long parentId;

    @ApiModelProperty("部门全称")
    private String sectionFullName;

    @ApiModelProperty("部门串")
    private String treeNode;

    @ApiModelProperty("职位id")
    private Long positionId;

    @ApiModelProperty("是否部门负责人  1:是；0:否")
    private Integer isLeader;

    @ApiModelProperty("职位名称")
    private String positionName;

    @ApiModelProperty("部门类型 1、业务部门；2、职能部门；3、综合部门")
    private Integer assort;

    public SectionPositionInfoVO(Long sectionId, String sectionName, String sectionTypeCode, Long parentId, String sectionFullName, Long positionId, Integer isLeader, String positionName, Integer assort) {
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.sectionTypeCode = sectionTypeCode;
        this.parentId = parentId;
        this.sectionFullName = sectionFullName;
        this.positionId = positionId;
        this.isLeader = isLeader;
        this.positionName = positionName;
        this.assort = assort;
    }
}
