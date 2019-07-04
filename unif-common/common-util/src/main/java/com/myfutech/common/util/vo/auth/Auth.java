package com.myfutech.common.util.vo.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 * 权限校验
 */

@Target(METHOD)
@Retention(RUNTIME)
public @interface Auth {
    String[] value() default  "";
}
