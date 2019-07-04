package com.myfutech.common.spring.jpa.base.impl;

import com.myfutech.common.spring.jpa.base.BaseDao;
import com.myfutech.common.util.exception.DaoException;
import com.myfutech.common.util.vo.Page;
import com.myfutech.common.util.vo.Pageable;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseJpaRepositoryImpl<T> extends SimpleJpaRepository<T,Long> implements BaseDao<T> {

    private final static Map<Class,AliasToBeanResultTransformer> TRANSFORMER_MAP = new HashMap<>();

    private final EntityManager entityManager;
    private final JpaEntityInformation<T, Long> entityInformation;

    public BaseJpaRepositoryImpl(JpaEntityInformation<T, Long> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }

    @Override
    public T getOne(Long id) {
        Assert.notNull(id, "The given id must not be null!");
        return findById(id).orElse(null);
    }

    @Override
    public long nativeCount(String sql, Object... params) {
        Query countQuery = entityManager.createNativeQuery(sql);
        initParams(params, countQuery);
        BigInteger records = (BigInteger) countQuery.getSingleResult();
        if(records == null || records.longValue() < 1){
            return 0;
        }
        return records.longValue();
    }

    @Override
    public long nativeCount(String sql, Map<String, Object> params) {
        Query countQuery = entityManager.createNativeQuery(sql);
        initParams(params, countQuery);
        BigInteger records = (BigInteger) countQuery.getSingleResult();
        if(records == null || records.longValue() < 1){
           return 0;
        }
        return records.longValue();
    }

    @Override
    public <E> List<E> nativeFindAll(String sql, Map<String, Object> params, Class<E> domainClazz) {
        Query query = createLocalQuery(domainClazz , sql);
        initParams(params, query);
        return query.getResultList();
    }

    @Override
    public <E> List<E> nativeFindAll(String sql, Class<E> domainClazz, Object... params) {
        Query query = createLocalQuery(domainClazz , sql);
        initParams(params, query);
        return query.getResultList();
    }

    @Override
    public <E> E nativeFindOne(String sql, Map<String, Object> params, Class<E> domainClazz) {
        List<E> data = nativeFindAll(sql, params, domainClazz);
        if (data == null  || data.size() == 0){
            return null;
        }
        if (data.size() != 1){
            throw new DaoException("查询结果不止一个");
        }
        return data.get(0);
    }

    @Override
    public <E> E nativeFindOne(String sql, Class<E> domainClazz, Object... params) {
        List<E> data = nativeFindAll(sql, domainClazz, params);
        if (data == null || data.size() == 0 ){
            return null;
        }
        if (data.size() != 1){
            throw new DaoException("查询结果不止一个");
        }
        return data.get(0);
    }

    @Override
    public <E> Page<E> nativeFindPage(String sql, Class<E> domainClazz, Pageable pageable, Object... params) {
        if(pageable == null){
            return new Page<>(nativeFindAll(sql,domainClazz, params));
        }

        String countSql = "select count(1) from (" + sql + ") tmp";
        Long totalCount = nativeCount(countSql, params);
        pageable.setTotalNum(totalCount);

        if (totalCount <= 0){
            return new Page<>(pageable);
        }
        Query query = createLocalQuery(domainClazz , sql);
        initParams(params, query);
        int startIndex = (pageable.getPageNum() - 1) * pageable.getPageSize();

        if (startIndex > totalCount){
            return new Page<>(pageable);
        }

        query.setFirstResult(startIndex);
        query.setMaxResults(pageable.getPageSize());
        List data = query.getResultList();
        return new Page<E>(pageable,data);
    }

    @Override
    public <E> Page<E> nativeFindPage(String sql, Map<String, Object> params, Class<E> domainClazz, Pageable pageable) {
        if(pageable == null){
            return new Page<>(nativeFindAll(sql,params,domainClazz));
        }

        String countSql = "select count(1) from (" + sql + ") tmp";
        Long totalCount = nativeCount(countSql, params);
        pageable.setTotalNum(totalCount);

        if (totalCount <= 0){
            return new Page<>(pageable);
        }
        Query query = createLocalQuery(domainClazz , sql);
        initParams(params, query);
        int startIndex = (pageable.getPageNum() - 1) * pageable.getPageSize();

        if (startIndex > totalCount){
            return new Page<>(pageable);
        }

        query.setFirstResult(startIndex);
        query.setMaxResults(pageable.getPageSize());
        List data = query.getResultList();
        return new Page<E>(pageable,data);
    }

    private void initParams(Map<String, Object> params, Query query) {
        if (params != null && !params.isEmpty()){
            for (String key : params.keySet()) {
                query.setParameter(key , params.get(key));
            }
        }
    }

    private void initParams(Object[] params, Query query) {
        if (params != null && params.length != 0){
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i+1 , params[i]);

            }
        }
    }

    private Query createLocalQuery(Class<?> domainClazz , String sql){
        Query query = null;
        if(domainClazz != null){
            if(domainClazz.getAnnotation(Entity.class) != null){
                query = entityManager.createNativeQuery( sql , domainClazz);
            }else{
                query = entityManager.createNativeQuery( sql);
                if(domainClazz.isAssignableFrom(Map.class)){
                    query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                }else if (domainClazz.isAssignableFrom(List.class)){
                    query.unwrap(NativeQuery.class).setResultTransformer(Transformers.TO_LIST);
                }else {
                    AliasToBeanResultTransformer transformer = TRANSFORMER_MAP.get(domainClazz);
                    if (transformer == null){
                        synchronized (TRANSFORMER_MAP){
                            transformer = TRANSFORMER_MAP.get(domainClazz);
                            if (transformer == null){
                                transformer = new AliasToBeanResultTransformer(domainClazz);
                                TRANSFORMER_MAP.put(domainClazz, transformer);
                            }
                        }
                    }
                    query.unwrap(NativeQuery.class).setResultTransformer(transformer);
                }
            }
        }else {
            query = entityManager.createNativeQuery( sql);
        }
        return  query;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T saveNotNull(T t) {
        Long id =  entityInformation.getId(t);

        if (id == null){
            return save(t);
        }

       T old = findById(id).orElse(null);
        if (old == null){
            throw new DaoException("没有找到对应对象");
        }
        Field[] fields = t.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                //去除静态属性
                if (Modifier.isStatic(field.getModifiers())){
                    continue;
                }

                field.setAccessible(true);
                Object value = field.get(t);
                if (value == null){
                    continue;
                }
                field.set(old, value);
            }
        } catch (IllegalAccessException e) {
           throw new DaoException("对象属性存取权限错误", e);
        }
        return saveAndFlush(old);
    }
}
