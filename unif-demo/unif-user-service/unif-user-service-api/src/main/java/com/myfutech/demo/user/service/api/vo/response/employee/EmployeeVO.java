package com.myfutech.demo.user.service.api.vo.response.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "Employee",description = "员工")
public class EmployeeVO {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("员工编号")
    private String employeeCode;

    @ApiModelProperty("员工姓名")
    private String employeeName;

    @ApiModelProperty("手机号")
    private String phoneNumber;

    @ApiModelProperty("用户头像")
    private String userImage;

    @ApiModelProperty("删除状态（0：正常，1：删除）")
    private Integer deleteStatus;

    @ApiModelProperty("创建人")
    private Long creator;

    @ApiModelProperty("修改人")
    private Long modifier;

    @ApiModelProperty(value = "创建时间", example = "2018-01-01 00:00:00")
    private Date createTime;

    @ApiModelProperty(value = "修改时间", example = "2018-01-01 00:00:00")
    private Date modifyTime;

    @ApiModelProperty("公司邮箱")
    private String workEmail;

}