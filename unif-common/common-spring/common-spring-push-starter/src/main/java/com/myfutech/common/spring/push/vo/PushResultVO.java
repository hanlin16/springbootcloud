package com.myfutech.common.spring.push.vo;


import lombok.Data;

@Data
public class PushResultVO {

	public static PushResultVO error(String info){
		return new PushResultVO(false, null, null, info);
	}

	public static PushResultVO success(Long msgId, Integer sendNo){
		return new PushResultVO(true, msgId, sendNo, "");
	}

	public static PushResultVO success(Long msgId, Integer sendNo, String info){
		return new PushResultVO(true, msgId, sendNo, info);
	}

	private Boolean flag;
	private Long msgId;
	private Integer sendNo;
	private String info;

	public PushResultVO() {
	}

	public PushResultVO(Boolean flag, Long msgId, Integer sendNo, String info) {
		this.flag = flag;
		this.msgId = msgId;
		this.sendNo = sendNo;
		this.info = info;
	}
}
