package com.myfutech.common.excel.vo;

import java.util.Date;

/**
 *
 */
public class CallServerStatisticsVO {

    private Date startTime;
    private Date endTime;
    private String servicerId;
    private String servicerName;
    private long sumTalkTimeOut;
    private int countTotalTalkTimeOut;
    private long sumFeeTime = 0;
    private String sumServerFee = "0";
    private String sumPlatFee = "0";


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getServicerId() {
        return servicerId;
    }

    public void setServicerId(String servicerId) {
        this.servicerId = servicerId;
    }

    public String getServicerName() {
        return servicerName;
    }

    public void setServicerName(String servicerName) {
        this.servicerName = servicerName;
    }

    public long getSumTalkTimeOut() {
        return sumTalkTimeOut;
    }

    public void setSumTalkTimeOut(long sumTalkTimeOut) {
        this.sumTalkTimeOut = sumTalkTimeOut;
    }

    public String getSumServerFee() {
        return sumServerFee;
    }

    public void setSumServerFee(String sumServerFee) {
        this.sumServerFee = sumServerFee;
    }

    public String getSumPlatFee() {
        return sumPlatFee;
    }

    public void setSumPlatFee(String sumPlatFee) {
        this.sumPlatFee = sumPlatFee;
    }

    public long getSumFeeTime() {
        return sumFeeTime;
    }

    public void setSumFeeTime(long sumFeeTime) {
        this.sumFeeTime = sumFeeTime;
    }

    public int getCountTotalTalkTimeOut() {
        return countTotalTalkTimeOut;
    }

    public void setCountTotalTalkTimeOut(int countTotalTalkTimeOut) {
        this.countTotalTalkTimeOut = countTotalTalkTimeOut;
    }
}
