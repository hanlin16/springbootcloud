package com.myfutech.demo.user.service.provider.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Data
@ApiModel(value = "Employee",description = "员工")
@Entity(name="basic_employee")
public class Employee {

    @ApiModelProperty("主键")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ApiModelProperty("员工编号")
    @Column(name="employeeCode")
    private String employeeCode;

    @ApiModelProperty("员工姓名")
    @Column(name="employeeName")
    private String employeeName;

    @ApiModelProperty("手机号")
    @Column(name="phoneNumber")
    private String phoneNumber;

    @ApiModelProperty("用户头像")
    @Column(name="userImage")
    private String userImage;

    @ApiModelProperty("删除状态（0：正常，1：删除）")
    @Column(name="deleteStatus")
    private Integer deleteStatus;

    @ApiModelProperty("创建人")
    @Column(name="creator")
    private Long creator;

    @ApiModelProperty("修改人")
    @Column(name="modifier")
    private Long modifier;

    @ApiModelProperty("创建时间")
    @Column(name="createTime")
    private Date createTime;

    @ApiModelProperty("修改时间")
    @Column(name="modifyTime")
    private Date modifyTime;

    @ApiModelProperty("公司邮箱")
    @Column(name="workEmail")
    private String workEmail;
}