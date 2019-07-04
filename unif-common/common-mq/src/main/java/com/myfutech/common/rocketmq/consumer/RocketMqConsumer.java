package com.myfutech.common.rocketmq.consumer;


import com.myfutech.common.rocketmq.constants.RocketMQErrorEnum;
import com.myfutech.common.rocketmq.exception.RocketMQException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;


import java.util.Map;

/**
 * Created by Liuxd on 2018/12/27.
 */
public class RocketMqConsumer {

    private static Logger LOGGER = LoggerFactory.getLogger(RocketMqConsumer.class);

    private DefaultMQPushConsumer defaultMQPushConsumer;

    /**
     * 必填参数：6个
     * nameServerAddr，topic，tag，consumerGroup，mqMessageListenerProcessor，messageModel
     * @param map
     */
    public RocketMqConsumer(Map<String, Object> map) {
        String namesrvAddr = map.get("namesrvAddr") == null ? "" : map.get("namesrvAddr").toString().trim();
        if (StringUtils.isEmpty(namesrvAddr)) {
            throw new RocketMQException(RocketMQErrorEnum.PARAMM_NULL, "nameServerAddr is blank", false);
        }
        String topic = map.get("topic") == null ? "" : map.get("topic").toString().trim();
        if (StringUtils.isEmpty(topic)) {
            throw new RocketMQException(RocketMQErrorEnum.PARAMM_NULL, "topic is null !!!", false);
        }
        String tag = map.get("tag") == null ? "" : map.get("tag").toString().trim();
        if (StringUtils.isEmpty(tag)) {
            throw new RocketMQException(RocketMQErrorEnum.PARAMM_NULL, "tag is null !!!", false);
        }
        String consumerGroup = map.get("consumerGroup") == null ? "" : map.get("consumerGroup").toString().trim();
        if (StringUtils.isEmpty(consumerGroup)) {
            throw new RocketMQException(RocketMQErrorEnum.PARAMM_NULL, "consumerGroup is null !!!", false);
        }
        MessageListenerConcurrently mqMessageListenerProcessor = (MessageListenerConcurrently) map.get("mqMessageListenerProcessor");

        if (null == mqMessageListenerProcessor) {
            throw new RocketMQException(RocketMQErrorEnum.PARAMM_NULL, "mqMessageListenerProcessor is null !!!", false);
        }

        /**
         * 模式：广播或集群
         */
        MessageModel messageModel = (MessageModel) map.get("messageModel");

        if (null == messageModel) {
            throw new RocketMQException(RocketMQErrorEnum.PARAMM_NULL, "messageModel is null !!!", false);
        }

        defaultMQPushConsumer = new DefaultMQPushConsumer(consumerGroup);
        defaultMQPushConsumer.setNamesrvAddr(namesrvAddr);
        defaultMQPushConsumer.setConsumeThreadMin(20);
        defaultMQPushConsumer.setConsumeThreadMax(60);

        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        /**
         * 设置一次消费消息的条数，默认为1条
         */
        defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
        //关闭VIP通道，避免接收不了消息
        defaultMQPushConsumer.setVipChannelEnabled(false);


        // 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
        // 如果非第一次启动，那么按照上次消费的位置继续消费
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        //设置为集群消费(区别于广播消费)
        defaultMQPushConsumer.setMessageModel(messageModel);

        /**
         * 注册监听
         */
        defaultMQPushConsumer.registerMessageListener(mqMessageListenerProcessor);

        try {

            // 订阅Topic下Tag的消息
            defaultMQPushConsumer.subscribe(topic, tag);

            defaultMQPushConsumer.start();
            LOGGER.info("defaultMQPushConsumer is start !!! groupName:"+consumerGroup+",topic:"+topic+",namesrvAddr:"+namesrvAddr);
        } catch (MQClientException e) {
            LOGGER.error("defaultMQPushConsumer is start !!! groupName:"+consumerGroup+",topic:"+topic+",namesrvAddr:"+namesrvAddr);
            throw new RocketMQException(e);
        }
    }


}
