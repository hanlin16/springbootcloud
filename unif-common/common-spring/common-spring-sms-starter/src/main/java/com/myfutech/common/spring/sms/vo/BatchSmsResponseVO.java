package com.myfutech.common.spring.sms.vo;

import lombok.Data;

import java.util.List;

@Data
public class BatchSmsResponseVO {

    /**
     * 成功数
     */
    private Integer successNum;
    /**
     * 失败数
     */
    private Integer failNum;
    /**
     * 失败列表
     */
    private List<FailSmsResponseVO> result;

    public BatchSmsResponseVO() {
    }

    public BatchSmsResponseVO(Integer successNum, Integer failNum, List<FailSmsResponseVO> result) {
        this.successNum = successNum;
        this.failNum = failNum;
        this.result = result;
    }
}
