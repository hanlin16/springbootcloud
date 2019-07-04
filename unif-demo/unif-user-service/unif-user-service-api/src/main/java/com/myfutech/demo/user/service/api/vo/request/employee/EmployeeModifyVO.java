package com.myfutech.demo.user.service.api.vo.request.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@ApiModel(value = "EmployeeModifyVO",description = "员工信息")
public class EmployeeModifyVO {

    public interface Create {}
    public interface Update extends Create {}

    @NotNull(groups = Update.class)
    @ApiModelProperty("主键")
    private Long id;

    @NotEmpty(groups = Create.class)
    @ApiModelProperty(value = "员工编号", required = true)
    private String employeeCode;

    @NotEmpty(groups = Create.class)
    @ApiModelProperty(value = "员工姓名", required = true)
    private String employeeName;

    @NotEmpty(groups = Create.class)
    @ApiModelProperty(value = "手机号", required = true)
    private String phoneNumber;

    @NotEmpty(groups = Create.class)
    @ApiModelProperty(value = "用户头像", required = true)
    private String userImage;

    @Min(groups = Create.class, value = 0)
    @Max(groups = Create.class, value = 1)
    @ApiModelProperty(value = "删除状态（0：正常，1：删除）", required = true)
    private Integer deleteStatus;

    @Email(groups = Create.class)
    @ApiModelProperty(value = "公司邮箱", required = true)
    private String workEmail;
}
