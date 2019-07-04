package com.myfutech.common.util.tree;

import com.myfutech.common.util.http.HttpClientUtils;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Lq on 2019/3/1.
 */
public class HttpClientUtilsTest {

    @Test
    public void testHttpClientGet() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(100);
        Long StartTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                Long StartTime1 = System.currentTimeMillis();
                HttpClientUtils.doGet("http://localhost:8083/users/list", null);
                System.out.println("耗时-> " + (System.currentTimeMillis() - StartTime1));
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println("总耗时-> " + (System.currentTimeMillis() - StartTime));
    }

    @Test
    public void testPost() {
        String xml = "<?xml version=\"1.0\" encoding=\"GBK\"?><Packet type=\"REQUEST\" version=\"1.0\" ><Head><REQUEST_TYPE>08</REQUEST_TYPE><USER>macn_test</USER><PASSWORD>111111</PASSWORD></Head><Body><baseDto><idNum>420322199010263351</idNum><issueCompany></issueCompany><phoneNum></phoneNum><companyCode></companyCode><inProductCode>1043001</inProductCode><visitCount></visitCount><province></province><age>29</age><regionInfo>420322</regionInfo><claimFlag></claimFlag><yearClaimFlag></yearClaimFlag><buyZjcpFlag></buyZjcpFlag><declineFlag></declineFlag><healthyDeclineFlag></healthyDeclineFlag><orgId></orgId></baseDto></Body></Packet>";
        String s = HttpClientUtils.doPostWithXml("https://testqcar.apiins.com/prpall/GuNoCarProposalServlet",xml);
        System.out.println(s);
    }
}
