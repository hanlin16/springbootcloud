package com.myfutech.common.spring.jpa.base;

import java.util.List;

public interface BaseService<T> {

    List<T> findAll();

    List<T> findAllById(Iterable<Long> ids);

    <S extends T> List<S> saveAll(Iterable<S> entities);

    <S extends T> S save(S entity);

    <S extends T> S saveAndFlush(S entity);

    T getOne(Long id);

    long count();

    void deleteById(Long id);

    void delete(T entity);

    void deleteAll(Iterable<? extends T> entities);

    void deleteInBatch(Iterable<T> entities);
    /**
     *  只保存非空字段
     */
    T saveNotNull(T t);

}
