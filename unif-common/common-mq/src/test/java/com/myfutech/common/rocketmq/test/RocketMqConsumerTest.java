package com.myfutech.common.rocketmq.test;

import com.myfutech.common.rocketmq.consumer.RocketMqConsumer;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Liuxd on 2018/12/28.
 */

public class RocketMqConsumerTest {

    @Test
    public void TestRocketMqConsumer() throws Exception{
        InputStream is = getClass().getClassLoader().getResourceAsStream("rocketMq.properties");
        Properties properties = new Properties();
        properties.load(is);

        Map<String, Object> map = new HashMap<String, Object>((Map) properties);
        MQConsumeMsgListenerProcessor mqConsumeMsgListenerProcessor = new MQConsumeMsgListenerProcessor();
        map.put("mqMessageListenerProcessor",mqConsumeMsgListenerProcessor);
        map.put("messageModel", MessageModel.CLUSTERING);

        RocketMqConsumer rocketMqConsumer = new RocketMqConsumer(map);

        Thread.sleep(100000);

    }

//    public static void main(String[] args) throws InterruptedException, MQClientException {
//
//        // Instantiate with specified consumer group name.
//        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name");
//
//        // Specify name server addresses.
//        consumer.setNamesrvAddr("192.168.30.150:9876");
//
//        // Subscribe one more more topics to consume.
//        consumer.subscribe("DemoTopic", "demoTag");
//        // Register callback to execute on arrival of messages fetched from brokers.
//        consumer.registerMessageListener(new MessageListenerConcurrently() {
//
//            @Override
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//            }
//        });
//
//        //Launch the consumer instance.
//        consumer.start();
//
//        System.out.printf("Consumer Started.%n");
//    }
}
