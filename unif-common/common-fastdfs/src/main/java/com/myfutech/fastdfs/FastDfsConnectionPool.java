package com.myfutech.fastdfs;

import com.myfutech.fastdfs.exception.FastDfsException;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;


import java.util.Properties;

/**FastDfsConnection连接池
 * Created by Lq on 2019/2/28.
 */
public class FastDfsConnectionPool {
    private static final GenericObjectPool<FastDfsConnection> fastDfsCommonPool
            = new GenericObjectPool<>(new FastDfsConnectionFactory());

    /**
     * 初始化连接池
     * @param props Properties
     */
    public static void init(Properties props) {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        String maxTotal = props.getProperty("fastdfs.max_total");
        String minIdle = props.getProperty("fastdfs.min_Idle");
        String maxIdle = props.getProperty("fastdfs.max_Idle");
        // 池中的最大连接数
        config.setMaxTotal(null == maxTotal || maxTotal.isEmpty() ? 50 : Integer.valueOf(maxTotal));
        // 最少的空闲连接数
        config.setMinIdle(null == minIdle || minIdle.isEmpty() ? 5 : Integer.valueOf(minIdle));
        // 最多的空闲连接数
        config.setMaxIdle(null == maxIdle || maxIdle.isEmpty() ? 8 : Integer.valueOf(maxIdle));
        // 当连接池资源耗尽时,调用者最大阻塞的时间,超时时抛出异常 单位:毫秒数
        config.setMaxWaitMillis(5000);
        // 连接池存放池化对象方式,true放在空闲队列最前面,false放在空闲队列最后
        config.setLifo(true);
        //连接空闲的最小时间,达到此值后空闲连接可能会被移除,默认30分钟
        config.setMinEvictableIdleTimeMillis(1000L * 60L * 30L);
        // 连接耗尽时是否阻塞,默认为true
        config.setBlockWhenExhausted(true);
        fastDfsCommonPool.setConfig(config);
    }

    /**
     *  从连接池获取一个FastDfsCommon对象
     * @return FastDfsConnection
     */
    public static FastDfsConnection borrow() {
        try {
            return fastDfsCommonPool.borrowObject();
        } catch (Exception ex) {
            throw new FastDfsException("get FastDfsClient Failed ",ex);
        }
    }

    /**
     * 释放FastDfsCommon对象
     * @param fastDfsCommon FastDfsConnection
     */
    public static void release(final FastDfsConnection fastDfsCommon) {
        if (null != fastDfsCommon) {
            fastDfsCommonPool.returnObject(fastDfsCommon);
        }
    }


}
