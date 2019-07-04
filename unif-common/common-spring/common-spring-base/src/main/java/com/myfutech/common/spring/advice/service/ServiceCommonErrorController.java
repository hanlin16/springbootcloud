package com.myfutech.common.spring.advice.service;

import com.myfutech.common.util.ErrorResponses;
import com.myfutech.common.util.Responses;
import com.myfutech.common.util.exception.BusinessException;
import com.myfutech.common.util.exception.RemoteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 * 服务全局异常处理类
 */
@Slf4j
@RestControllerAdvice
public class ServiceCommonErrorController {

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity handleOtherExceptions(final HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        return new ResponseEntity<>(ErrorResponses.newErrorResponses(Responses.error(ex.getMessage()), null), HttpStatus.METHOD_NOT_ALLOWED);
    }
    @ExceptionHandler(value = {BusinessException.class})
    public ResponseEntity handleOtherExceptions(final BusinessException ex, HttpServletRequest request) {
        log.error("请求 "+request.getRequestURI()+" 出错", ex);
        return new ResponseEntity<>(ErrorResponses.newErrorResponses(ex.getResponses(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {RemoteException.class})
    public ResponseEntity handleOtherExceptions(final RemoteException ex, HttpServletRequest request) {
        log.error("服务间请求 "+request.getRequestURI()+"失败", ex);
        return new ResponseEntity<>(ErrorResponses.newErrorResponses(ex.getResponses(), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity handleOtherExceptions(final Exception ex, HttpServletRequest request) {
        BindingResult bindingResult = null;
        if (BindException.class.isInstance(ex)){
            bindingResult = ((BindException)ex).getBindingResult();
        }else if (MethodArgumentNotValidException.class.isInstance(ex)){
            bindingResult = ((MethodArgumentNotValidException)ex).getBindingResult();
        }
        if (bindingResult != null && bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldErrors().get(0);
            return new ResponseEntity<>(ErrorResponses.newErrorResponses(Responses.error(fieldError.getField() + ":" + fieldError.getDefaultMessage()),null), HttpStatus.PRECONDITION_FAILED);
        }
        return new ResponseEntity<>(ErrorResponses.newErrorResponses(Responses.error(ex.getMessage()), null), HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity handleOtherExceptions(final HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.error("请求 "+request.getRequestURI()+" 参数解析异常", ex);
        return new ResponseEntity<>(ErrorResponses.newErrorResponses(Responses.error("请求参数格式有误"), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity handleOtherExceptions(final Throwable ex, HttpServletRequest request) {
        log.error("请求 "+request.getRequestURI()+" 出错", ex);
        return new ResponseEntity<>(ErrorResponses.newErrorResponses(Responses.error("系统繁忙，请稍后再试"), null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}