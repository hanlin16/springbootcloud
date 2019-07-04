package com.myfutech.common.rocketmq.test;

import com.myfutech.common.rocketmq.producer.RocketMqProvider;
import org.junit.Test;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Liuxd on 2018/12/28.
 */
public class RocketMqProviderTest {

    @Test
    public void testRocketMqProducer() throws Exception{
        InputStream is = getClass().getClassLoader().getResourceAsStream("rocketMq.properties");
        Properties properties = new Properties();
        properties.load(is);
        RocketMqProvider rocketMqProvider = new RocketMqProvider(properties);


        String msg = "客户端测试发送消息";
        System.out.println("开始发送消息："+msg);
        String topic = "DemoTopic";
        String tag = "demoTag";


        String sendResult = rocketMqProvider.send(topic,tag,msg);
        System.out.println("消息发送响应信息："+sendResult);
    }
}
