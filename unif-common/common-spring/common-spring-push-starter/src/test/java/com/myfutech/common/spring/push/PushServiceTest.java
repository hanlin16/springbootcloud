package com.myfutech.common.spring.push;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import com.myfutech.common.spring.push.vo.PushResultVO;
import org.junit.BeforeClass;
import org.junit.Test;

public class PushServiceTest {

    private static PushService pushService;

    @BeforeClass
    public static void testBefore(){
        ClientConfig config = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient("d0ed117720760bf598f71a36", "a5614b7bcd3602d80c5cb72e", null, config);
        pushService = new PushService(jpushClient);
    }

    @Test
    public void pushToOne(){
        PushResultVO pushResultVO = pushService.pushToOne("这是一个测试", null, "13701165874");
    }
}
