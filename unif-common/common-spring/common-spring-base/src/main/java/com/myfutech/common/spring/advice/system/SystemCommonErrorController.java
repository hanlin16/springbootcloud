package com.myfutech.common.spring.advice.system;

import com.myfutech.common.util.Responses;
import com.myfutech.common.util.exception.BusinessException;
import com.myfutech.common.util.exception.RemoteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 *  common-spring-system 下的 com.myfutech.common.spring.system.ctrl.CommonErrorCtrl
 * 系统全局异常处理类
 */
@Slf4j
@RestControllerAdvice
@Deprecated
public class SystemCommonErrorController {

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public Responses<?> handleOtherExceptions(final HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return Responses.error(ex.getMessage());
    }
    @ExceptionHandler(value = {BusinessException.class})
    public Responses<?> handleOtherExceptions(final BusinessException ex, HttpServletRequest request) {
        log.error("请求 "+request.getRequestURI()+" 出错", ex);
        return ex.getResponses();
    }

    @ExceptionHandler(value = {RemoteException.class})
    public Responses<?> handleOtherExceptions(final RemoteException ex, HttpServletRequest request) {
        log.error("服务间请求 "+request.getRequestURI()+"失败", ex);
        return ex.getResponses();
    }

    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public Responses<?> handleOtherExceptions(final Exception ex, HttpServletRequest request) {
        BindingResult bindingResult = null;
        if (BindException.class.isInstance(ex)){
            bindingResult = ((BindException)ex).getBindingResult();
        }else if (MethodArgumentNotValidException.class.isInstance(ex)){
            bindingResult = ((MethodArgumentNotValidException)ex).getBindingResult();
        }
        if (bindingResult != null && bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldErrors().get(0);
            return Responses.error(fieldError.getField() + ":" + fieldError.getDefaultMessage());
        }
        return Responses.error(ex.getMessage());
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public Responses<?> handleOtherExceptions(final HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.error("请求 "+request.getRequestURI()+" 参数解析异常", ex);
        return Responses.error("请求参数格式有误");
    }

    @ExceptionHandler(value = {Throwable.class})
    public Responses<?> handleOtherExceptions(final Throwable ex, HttpServletRequest request) {
        log.error("请求 "+request.getRequestURI()+" 出错", ex);
        return Responses.error("系统繁忙，请稍后再试");
    }
}