package com.myfutech.common.spring.system.store;

public interface IAuthStore<T> {

    @Deprecated
    T get(String authKey);

    @Deprecated
    void put(String authKey, T value, long seconds);

    <E> void putAttach(String authKey, String key, E value);

    <E> E getAttach(String authKey, String key, Class<E> clazz);

    void remove(String authKey);

    Boolean expire(String authKey, long seconds);
}
