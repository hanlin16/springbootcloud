package com.myfutech.common.util;

import com.fasterxml.jackson.annotation.JsonView;
import com.myfutech.common.util.enums.ResponseCode;
import com.myfutech.common.util.vo.BaseView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Responses", description = "响应信息")
public class Responses<T> {

    public static <T> Responses<T> success(){
        return new Responses<>(ResponseCode.SUCCESS_CODE, "", null);
    }

    public static <T> Responses<T> success(T result){
        return new Responses<>(ResponseCode.SUCCESS_CODE, "", result);
    }

    public static <T> Responses<T> success(String msg, T result){
        return new Responses<>(ResponseCode.SUCCESS_CODE, msg, result);
    }

    public static <T> Responses<T> error(String msg){
        return new Responses<>(ResponseCode.ERROR_CODE, msg, null);
    }

    public static <T> Responses<T> error(ResponseCode code){
        return new Responses<>(code, code.getDefaultMsg(), null);
    }

    public static <T> Responses<T> error(ResponseCode code, String msg){
        return new Responses<>(code, msg, null);
    }

    @JsonView(BaseView.class)
    @ApiModelProperty("响应编码")
    private String code;
    @JsonView(BaseView.class)
    @ApiModelProperty("响应消息")
    private String msg;
    @JsonView(BaseView.class)
    @ApiModelProperty("响应体")
    private T result;

    public Responses() {
    }

    private Responses(ResponseCode code, String msg, T result) {
        this.code = code.getCode();
        this.msg = msg;
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public boolean notSuccess() {
        return !ResponseCode.SUCCESS_CODE.getCode().equals(code);
    }

    public String getMsg() {
        return msg;
    }

    public T getResult() {
        return result;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
