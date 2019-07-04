package com.myfutech.common.util.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Pageable",description = "分页参数")
public class Pageable{

	@ApiModelProperty(value = "每页行数，默认10", example = "10")
	private Integer pageSize = 10;
	@ApiModelProperty(value = "当前页，默认1", example = "1")
	private Integer pageNum = 1;
	@ApiModelProperty(value = "总数据量，默认0", example = "0")
	private Long totalNum = 0L;

	public Pageable() {
	}
	
	public Pageable(Integer pageSize, Long totalNum) {
		super();
		this.pageSize = pageSize;
		this.totalNum = totalNum;
	}

	public Pageable(Integer pageSize, Integer pageNum, Long totalNum) {
		this.pageSize = pageSize;
		this.pageNum = pageNum;
		this.totalNum = totalNum;
	}
}
