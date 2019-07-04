package com.myfutech.common.util.vo;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@ApiModel(value = "Page",description = "分页响应结果")
public class Page<T> extends Pageable{

    public interface DataView extends BaseView{}

    @ApiModelProperty("当前页数据")
    @JsonView(DataView.class)
    private List<T> data;

    public Page() {
    }

    public Page(List<T> data) {
        this.data = data;
    }

    public Page(Integer pageSize, Long recordCount, List<T> data) {
        super(pageSize, recordCount);
        this.data = data;
    }

    public Page(Integer pageSize, Integer pageNum, Long recordCount, List<T> data) {
        super(pageSize, pageNum, recordCount);
        this.data = data;
    }

    public Page(Pageable pageable, List<T> data) {
        super(pageable.getPageSize(), pageable.getPageNum(), pageable.getTotalNum());
        this.data = data;
    }

    public Page(Pageable pageable) {
        super(pageable.getPageSize(), pageable.getPageNum(), pageable.getTotalNum());
        this.data = Collections.emptyList();
    }
}
