package com.myfutech.common.spring.sms;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.ValidSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.BatchSMSPayload;
import cn.jsms.api.common.model.BatchSMSResult;
import cn.jsms.api.common.model.RecipientPayload;
import cn.jsms.api.common.model.SMSPayload;
import cn.jsms.api.template.SendTempSMSResult;
import cn.jsms.api.template.TemplatePayload;
import com.myfutech.common.spring.sms.exception.SmsException;
import com.myfutech.common.spring.sms.vo.BatchSmsRequestVO;
import com.myfutech.common.spring.sms.vo.BatchSmsResponseVO;
import com.myfutech.common.spring.sms.vo.FailSmsResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author kou
 * @date 2019-02-27
 */
@Slf4j
public class SmsService {

    public static final String ERROR_CODE = "-1";

    private SMSClient client;

    public SmsService(SMSClient smsClient) {
        this.client = smsClient;
    }

    /**
     * 发送验证码
     *
     * @param mobile        必传   手机号码
     * @param templateId    必传   模板ID
     * @param signId        非必传 签名ID，该字段为空则使用应用默认签名
     * @return  短信id
     */
    public String sendAuthCode(String mobile, Integer templateId, Integer signId){
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber(mobile)
                .setTempId(templateId)
                 .setSignId(signId)
                .build();
        try {
            SendSMSResult res = client.sendSMSCode(payload);
            log.info("发送验证码结果:{}", res.toString());
            Assert.isTrue(res.isResultOK(),"发送验证码失败");
            return res.getMessageId();
        } catch (APIConnectionException e) {
            throw new SmsException("发送验证码失败",e);
        } catch (APIRequestException e) {
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Message: " + e.getMessage());
            throw new SmsException("发送验证码失败",e);
        }
    }

    /**
     * 校验验证码
     *
     * @param messageId     必传   短信id
     * @param code          必传   验证码
     * @return  是否正确
     */
    public Boolean validAuthCode(String messageId, String code){
        try {
            ValidSMSResult res = client.sendValidSMSCode(messageId, code);
            log.info("校验验证码结果:{}", res.toString());
            return res.getIsValid();
        } catch (APIConnectionException e) {
            throw new SmsException("校验验证码失败",e);
        } catch (APIRequestException e) {
            log.error("HTTP Status: " + e.getStatus());
            log.error("Error Message: " + e.getMessage());

            //HTTP Status: 403
            // Error Message: {"is_valid":false,"error":{"code":50010,"message":"invalid code"}}
            if (e.getStatus() == 403){
                return false;
            }

            throw new SmsException("校验验证码失败",e);
        }
    }

    /**
     * 发送单个短信
     *
     * @param mobile        必传   手机号码
     * @param templateId    必传   模板ID
     * @param signId        非必传 签名ID，该字段为空则使用应用默认签名
     * @param templateParams 非必传 模版参数
     * @return  短信id
     */
    public String sendSms(String mobile, Integer templateId, Integer signId, Map<String, String> templateParams){

        Assert.notNull(mobile, "手机号码不能为空");
        Assert.notNull(templateId, "模板ID不能为空");

        if (templateParams == null){
            templateParams = Collections.emptyMap();
        }

        try {
            SMSPayload payload = SMSPayload.newBuilder()
                    .setMobileNumber(mobile)
                    .setTempId(templateId)
                    .setSignId(signId)
                    .setTempPara(templateParams)
                    .build();
            SendSMSResult res = client.sendTemplateSMS(payload);
            Assert.isTrue(res.isResultOK(), "发送短信失败");
            log.info("发送短信结果:{}", res.toString());
            return res.getMessageId();
        } catch (APIConnectionException e) {
            log.error("发送短信失败",e);
        } catch (APIRequestException e) {
            log.error("HTTP Status: " + e.getStatus());
            log.error("Error Message: " + e.getMessage());
            log.error("发送短信失败",e);
        }
        return ERROR_CODE;
    }

    /**
     * 批量发送短信
     *
     * @param list          必传   批量传参
     * @param templateId    必传   模板ID
     * @param signId        非必传 签名ID，该字段为空则使用应用默认签名
     * @return  批量结果
     */
    public BatchSmsResponseVO batchSendSms(List<BatchSmsRequestVO> list, Integer templateId, Integer signId){

        Assert.notEmpty(list, "手机号码不能为空");
        Assert.notNull(templateId, "模板ID不能为空");

        RecipientPayload[] recipientPayloads = new RecipientPayload[list.size()];

        for (int i = 0; i < list.size(); i++) {
            BatchSmsRequestVO batchSmsRequestVO = list.get(i);
            RecipientPayload recipientPayload = new RecipientPayload.Builder()
                    .setMobile(batchSmsRequestVO.getMobile())
                    .setTempPara(batchSmsRequestVO.getTemplateParams() == null ? Collections.emptyMap() : batchSmsRequestVO.getTemplateParams())
                    .build();
            recipientPayloads[i] = recipientPayload;
        }

        BatchSMSPayload smsPayload = BatchSMSPayload.newBuilder()
                .setTempId(templateId)
                 .setSignId(signId)
                .setRecipients(recipientPayloads)
                .build();

        try {
            BatchSMSResult result = client.sendBatchTemplateSMS(smsPayload);
            log.info("批量发送短信结果:{}", result.toString());
            List<FailSmsResponseVO> failList = new ArrayList<>();
            BatchSmsResponseVO responseVO = new BatchSmsResponseVO(result.getSuccessCount(), result.getFailureCount(), failList);
            if (CollectionUtils.isNotEmpty(result.getFailureRecipients())) {
                for (BatchSMSResult.FailureRecipients fail : result.getFailureRecipients()) {
                    FailSmsResponseVO vo = new FailSmsResponseVO(fail.getMobile(), fail.getErrorCode(), fail.getErrorMessage());
                    failList.add(vo);
                }
            }
            return responseVO;
        } catch (APIConnectionException e) {
            log.error("发送短信失败",e);
        } catch (APIRequestException e) {
            log.error("HTTP Status: " + e.getStatus());
            log.error("Error Message: " + e.getMessage());
            log.error("发送短信失败",e);
        }
        List<FailSmsResponseVO> failList = new ArrayList<>();
        BatchSmsResponseVO responseVO = new BatchSmsResponseVO(0, list.size(), failList);
        for (BatchSmsRequestVO requestVO : list) {
            FailSmsResponseVO vo = new FailSmsResponseVO(requestVO.getMobile(), ERROR_CODE, "发送短信失败");
            failList.add(vo);
        }
        return responseVO;
    }

    /**
     * 创建模版
     *
     * @param template  必传   模板内容
     * @param type      必传   模板类型，1为验证码类，2为通知类，3为营销类
     * @param ttl       非必传 验证码有效期，必须大于 0 且不超过 86400 ，单位为秒（当模板类型为1时必传）
     * @param remark    非必传 请简略描述正文模版的发送场景及发送对象，不超过100字
     * @return  模版id
     */
    public Integer createTemplate(String template, Integer type, Integer ttl, String remark){
        try {
            TemplatePayload payload = TemplatePayload.newBuilder()
                    .setTemplate(template)
                    .setType(type)
                    .setTTL(ttl)
                    .setRemark(remark)
                    .build();
            SendTempSMSResult result = client.createTemplate(payload);
            log.info("创建模版结果:{}", result.toString());
            return result.getTempId();
        } catch (APIConnectionException e) {
            throw new SmsException("创建模版失败",e);
        } catch (APIRequestException e) {
            log.error("HTTP Status: " + e.getStatus());
            log.error("Error Message: " + e.getMessage());
            throw new SmsException("创建模版失败",e);
        }
    }

    /**
     * 更新模版
     *
     * @param templateId 必传   模板ID
     * @param template  必传   模板内容
     * @param type      必传   模板类型，1为验证码类，2为通知类，3为营销类
     * @param ttl       非必传 验证码有效期，必须大于 0 且不超过 86400 ，单位为秒（当模板类型为1时必传）
     * @param remark    非必传 请简略描述正文模版的发送场景及发送对象，不超过100字
     * @return  模版id
     */
    public void updateTemplate(Integer templateId, String template, Integer type, Integer ttl, String remark) {
        try {
            TemplatePayload payload = TemplatePayload.newBuilder()
                    .setTempId(templateId)
                    .setTemplate(template)
                    .setType(type)
                    .setTTL(ttl)
                    .setRemark(remark)
                    .build();
            SendTempSMSResult result = client.updateTemplate(payload, templateId);
            log.info("更新模版结果:{}", result.toString());
        } catch (APIConnectionException e) {
            throw new SmsException("更新模版失败",e);
        } catch (APIRequestException e) {
            log.error("HTTP Status: " + e.getStatus());
            log.error("Error Message: " + e.getMessage());
            throw new SmsException("更新模版失败",e);
        }
    }

    /**
     * 删除模版id
     * 
     * @param templateId  必传   模板ID
     */
    public void deleteTemplate(Integer templateId){
        try {
            ResponseWrapper result = client.deleteTemplate(templateId);
            log.info("删除模版结果:{}", result.toString());
        } catch (APIConnectionException e) {
            throw new SmsException("删除模版失败",e);
        } catch (APIRequestException e) {
            log.error("HTTP Status: " + e.getStatus());
            log.error("Error Message: " + e.getMessage());
            throw new SmsException("删除模版失败",e);
        }
    }


}
