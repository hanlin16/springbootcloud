package com.myfutech.common.rocketmq.producer;

import com.myfutech.common.rocketmq.constants.RocketMQErrorEnum;
import com.myfutech.common.rocketmq.exception.RocketMQException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by Liuxd on 2018/12/27.
 */
public class RocketMqProvider {

    private static Logger LOGGER = LoggerFactory.getLogger(RocketMqProvider.class);

    private String groupName = "qd-rocketmq";

    private DefaultMQProducer defaultMQProducer;

    public RocketMqProvider(Properties props) {

        String namesrvAddr = props.getProperty("namesrvAddr");
        if (StringUtils.isEmpty(namesrvAddr)) {
            throw new RocketMQException(RocketMQErrorEnum.PARAMM_NULL, "namesrvAddr is blank", false);
        }

        defaultMQProducer = new DefaultMQProducer(groupName);
        defaultMQProducer.setNamesrvAddr(namesrvAddr);
        defaultMQProducer.setVipChannelEnabled(false);
        defaultMQProducer.setRetryTimesWhenSendFailed(5);
        defaultMQProducer.setRetryTimesWhenSendAsyncFailed(5);
        defaultMQProducer.setRetryAnotherBrokerWhenNotStoreOK(true);
        defaultMQProducer.setMaxMessageSize(64);
        defaultMQProducer.setSendMsgTimeout(3000);
        //如果发送消息失败，设置重试次数，默认为2次
        defaultMQProducer.setRetryTimesWhenSendFailed(2);
        //如果需要同一个jvm中不同的producer往不同的mq集群发送消息，需要设置不同的instanceName
        //defaultMQProducer.setInstanceName(instanceName);

        try {
            defaultMQProducer.start();

            LOGGER.info(String.format("defaultMQProducer is start ! groupName:[%s],namesrvAddr:[%s]"
                    , props.getProperty("groupName"), props.getProperty("namesrvAddr")));

        } catch (MQClientException e) {
            LOGGER.error(String.format("defaultMQProducer is error {}", e.getMessage(), e));
            throw new RocketMQException(e);
        }

    }

    /**
     * 向rocketMq发送消息
     *
     * @param topic
     * @param tag
     * @param msg
     * @return
     * @throws MQClientException
     * @throws RemotingException
     * @throws MQBrokerException
     * @throws InterruptedException
     */
    public String send(String topic, String tag, String msg) {
        LOGGER.info("开始向rocketMq发送消息：topic:" + topic + ",tag:" + tag + ",msg:{}" + msg);
        Message sendMsg = new Message(topic, tag, msg.getBytes());

        SendResult sendResult = null;
        try {
            sendResult = defaultMQProducer.send(sendMsg);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }


        LOGGER.info("消息发送响应信息：" + sendResult.toString());

        return sendResult.toString();
    }
}
