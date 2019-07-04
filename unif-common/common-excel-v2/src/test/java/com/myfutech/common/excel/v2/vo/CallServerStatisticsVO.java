package com.myfutech.common.excel.v2.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 *
 */
public class CallServerStatisticsVO {

    @Excel(name="姓名")
    private String name;

    @Excel(name="手机号")
    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "com.myfutech.common.excel.v2.vo.CallServerStatisticsVO{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
