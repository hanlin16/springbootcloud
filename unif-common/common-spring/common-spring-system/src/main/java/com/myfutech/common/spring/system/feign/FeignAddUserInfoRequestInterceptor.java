package com.myfutech.common.spring.system.feign;

import com.alibaba.fastjson.JSON;
import com.myfutech.common.spring.system.util.AuthUserUtils;
import com.myfutech.common.util.constant.HttpHeader;
import com.myfutech.common.util.vo.auth.LoginUserVO;
import com.myfutech.common.util.vo.auth.UserInfo;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Slf4j
public class FeignAddUserInfoRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String subsystemKey = request.getHeader(HttpHeader.SUBSYSTEM_KEY);
                UserInfo userInfo = AuthUserUtils.getUserInfo();
                LoginUserVO vo;
                if (userInfo != null) {

                    Map<String,Set<Long>> map = userInfo.getDataPowerSectionIdMap();
                    Set<Long> dataPowerSectionIdSet = null;
                    Set<Long> dataPowerFullSectionIdSet = null;
                    if (map != null){
                        dataPowerSectionIdSet = map.get(subsystemKey);
                    }
                    if (userInfo.getDataPowerFullSectionIdMap() != null){
                        dataPowerFullSectionIdSet = userInfo.getDataPowerFullSectionIdMap().get(subsystemKey);
                    }

                    vo = LoginUserVO.builder().id(userInfo.getId())
                            .userCode(userInfo.getUserCode())
                            .realName(userInfo.getRealName())
                            .employeeId(userInfo.getEmployeeId())
                            .employeeCode(userInfo.getEmployeeCode())
                            .dataPowerSectionIdSet(dataPowerSectionIdSet)
                            .dataPowerFullSectionIdSet(dataPowerFullSectionIdSet)
                            .productIdSet(userInfo.getProductId()).position(userInfo.getPosition())
                            .build();
                } else {
                    log.warn("服务间调用没有找到对应用户信息");
                    vo = new LoginUserVO();
                    vo.setDataPowerFullSectionIdSet(Collections.singleton(-999L));
                    vo.setDataPowerSectionIdSet(Collections.singleton(-999L));
                    vo.setProductIdSet(Collections.singleton(-999L));
                }
                template.header(HttpHeader.USER_INFO_KEY, URLEncoder.encode(JSON.toJSONString(vo), "UTF-8"));
            }
        } catch (Exception e) {
            log.debug("服务调用传递用户信息异常：", e);
        }
    }
}
