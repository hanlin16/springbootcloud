package com.myfutech.common.spring.push;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.DefaultResult;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.myfutech.common.spring.push.vo.PushResultVO;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Set;

@Slf4j
public class PushService {

    private JPushClient jpushClient;

    public PushService(JPushClient jpushClient) {
        this.jpushClient = jpushClient;
    }

    public void bindMobile(String userCode, String phoneNo) {
        try {
            DefaultResult result =  jpushClient.bindMobile(userCode, phoneNo);
            log.info("Got result - " + result);
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            logErrorInfo(e);
        }
    }

    /**
     * 绑定用户和手机号和别名
     *
     * @param userCode           用户标识
     * @param phoneNo           手机号
     * @param alias             别名
     * @param tagsToAdd         标签
     */
    public void updateDeviceTagAlias(String userCode, String phoneNo, String alias, Set<String> tagsToAdd){
        try {
            bindMobile(userCode, phoneNo);
            DefaultResult result =  jpushClient.updateDeviceTagAlias(userCode, alias, tagsToAdd, null);
            log.info("Got result - " + result);
        } catch (APIConnectionException e) {
            log.error("绑定用户、手机号、别名失败", e);
        } catch (APIRequestException e) {
            logErrorInfo(e);
            log.error("绑定用户、手机号、别名失败", e);
        }
    }

    /**
     * 全员推送
     *
     * @param msg	推送消息
     * @return
     */
    public PushResultVO pushToAll(String msg,HashMap<String, String> map){

        log.info("全员推送:{}", msg);

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setNotification(initNotification(msg,map))
                .setMessage(Message.content(msg))
                .build();
        return sendPush(payload);
    }

    /**
     * 全员发送消息（不给用户提示）
     *
     * @param msg	推送消息
     * @return
     */
    public PushResultVO messageToAll(String msg, HashMap<String, String> map){

        log.info("全员消息提示:{}", msg);

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setMessage(Message.newBuilder().setMsgContent(msg).addExtras(map).build())
                .build();
        return sendPush(payload);
    }

    /**
     * 单体发送消息（不给用户提示）
     *
     * @param msg	推送消息
     * @return
     */
    public PushResultVO messageToOne(String msg, HashMap<String, String> map, String alias){

        log.info("单体消息提示:{}", msg);

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setMessage(Message.newBuilder().setMsgContent(msg).addExtras(map).build())
                .build();
        return sendPush(payload);
    }

    /**
     * 根据别名向一组用户推送
     *
     * @param msg   推送消息
     * @param alias 一组别名
     * @return
     */
    public PushResultVO pushToSome(String msg, HashMap<String, String> map,Set<String> alias){

        log.info("向一组用户进行单推:{}", msg);

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(initNotification(msg,map))
                .build();
        return sendPush(payload);
    }

    /**
     * 根据别名向一个用户推送
     *
     * @param msg	推送消息
     * @param alias 别名
     * @return
     */
    public PushResultVO pushToOne(String msg, HashMap<String, String> map, String alias){

        log.info("向{}单推:{}", alias, msg);

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(initNotification(msg,map))
                .build();
        return sendPush(payload);
    }


    /**
     * 根据标签对一组用户推送
     *
     * @param msg	推送消息
     * @param tag	推送标签
     * @return
     */
    public PushResultVO pushToSome(String msg,HashMap<String, String> map, String tag){

        log.info("向{}群推:{}", tag, msg);
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.tag(tag))
                .setNotification(initNotification(msg,map))
                .build();
        return sendPush(payload);
    }


    /**
     * 发送push，并返回push消息id
     *
     * @param payload push消息封装体
     * @return  push消息结果
     */
    private PushResultVO sendPush(PushPayload payload){
        try {
            PushResult result = jpushClient.sendPush(payload);
            log.info("Got result - " + result);
            return PushResultVO.success(result.msg_id, result.sendno);
        } catch (APIConnectionException e) {
            log.error("发送push失败", e);
            return PushResultVO.error(e.getMessage());
        } catch (APIRequestException e) {
            logErrorInfo(e);
            log.error("SendNo: " + payload.getSendno());
            log.error("发送push失败",  e);
            return PushResultVO.error(e.getErrorMessage());
        }
    }

    private void logErrorInfo(APIRequestException e) {
        log.error("HTTP Status: " + e.getStatus());
        log.error("Error Code: " + e.getErrorCode());
        log.error("Error Message: " + e.getErrorMessage());
        log.error("Msg ID: " + e.getMsgId());
    }

    private Notification initNotification(String msg,HashMap<String, String> map) {

        cn.jpush.api.push.model.notification.IosNotification.Builder ios = IosNotification
                .newBuilder()
                .setSound("sound.caf")
                .setAlert(msg)
                .setContentAvailable(true)
                .setBadge(+1);

        cn.jpush.api.push.model.notification.AndroidNotification.Builder android = AndroidNotification
                .newBuilder()
                .setAlert(msg);

        if(null == map){
            map = new HashMap<>(1);
            map.put("code", "000");
        }

        ios.addExtras(map);
        android.addExtras(map);

        return Notification.newBuilder().addPlatformNotification(ios.build())
                .addPlatformNotification(android.build()).build();
    }
}
