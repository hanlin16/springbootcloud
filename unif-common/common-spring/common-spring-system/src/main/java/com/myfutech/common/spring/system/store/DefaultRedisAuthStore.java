package com.myfutech.common.spring.system.store;

import com.alibaba.fastjson.JSON;
import com.myfutech.common.util.constant.AuthInfo;
import com.myfutech.common.util.vo.auth.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class DefaultRedisAuthStore implements IAuthStore<UserInfo> {

    private StringRedisTemplate stringRedisTemplate;

    public DefaultRedisAuthStore(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public UserInfo get(String authKey) {
       return getAttach(authKey,AuthInfo.AUTH_REDIS_USER_INFO, UserInfo.class);
    }

    @Override
    public void put(String authKey, UserInfo value, long seconds) {
        putAttach(authKey, AuthInfo.AUTH_REDIS_USER_INFO, value);
        stringRedisTemplate.expire(authKey, seconds, TimeUnit.SECONDS);
    }

    @Override
    public <E> void putAttach(String authKey, String key, E value) {
        String data = JSON.toJSONStringWithDateFormat(value, "yyyy-MM-dd HH:mm:ss");
        stringRedisTemplate.opsForHash().put(authKey, key, data);
    }

    @Override
    public <E> E getAttach(String authKey, String key, Class<E> clazz) {
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        String value = hashOperations.get(authKey, key);
        if (StringUtils.isBlank(value)){
            return null;
        }
        return JSON.parseObject(value, clazz);
    }

    @Override
    public void remove(String authKey) {
        stringRedisTemplate.delete(authKey);
    }

    @Override
    public Boolean expire(String authKey, long seconds) {
        return stringRedisTemplate.expire(authKey, seconds, TimeUnit.SECONDS);
    }
}
