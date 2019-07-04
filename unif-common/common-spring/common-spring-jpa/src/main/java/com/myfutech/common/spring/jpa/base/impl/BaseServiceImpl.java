package com.myfutech.common.spring.jpa.base.impl;

import com.myfutech.common.spring.jpa.base.BaseDao;
import com.myfutech.common.spring.jpa.base.BaseService;

import java.util.List;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

	protected abstract BaseDao<T> getDao();

	@Override
	public List<T> findAll() {
		return getDao().findAll();
	}

	@Override
	public List<T> findAllById(Iterable<Long> ids) {
		return getDao().findAllById(ids);
	}

	@Override
	public <S extends T> List<S> saveAll(Iterable<S> entities) {
		return getDao().saveAll(entities);
	}
	
	@Override
	public <S extends T> S save(S entity) {
		return getDao().save(entity);
	}

	@Override
	public <S extends T> S saveAndFlush(S entity) {
		return getDao().saveAndFlush(entity);
	}

	@Override
	public void deleteInBatch(Iterable<T> entities) {
		getDao().deleteInBatch(entities);
	}

	@Override
	public T getOne(Long id) {
		return getDao().findById(id).orElse(null);
	}

	@Override
	public long count() {
		return getDao().count();
	}

	@Override
	public void deleteById(Long id) {
		getDao().deleteById(id);
	}

	@Override
	public void delete(T entity) {
		getDao().delete(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends T> entities) {
		getDao().deleteAll(entities);
	}

	@Override
	public T saveNotNull(T entity) {
		return getDao().saveNotNull(entity);
	}
}
