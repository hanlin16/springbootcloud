package com.myfutech.common.excel.util;

import com.myfutech.common.excel.vo.CallServerStatisticsVO;
import com.myfutech.fastdfs.FastDfsClient;
import org.junit.Before;
import org.junit.Test;
import org.jxls.common.Context;

import java.io.InputStream;
import java.util.*;

public class ExcelUtilsTest {

    private FastDfsClient client;

    @Before
    public void before()throws Exception{
        InputStream is = getClass().getClassLoader().getResourceAsStream("fastdfs-client.properties");
        Properties properties = new Properties();
        properties.load(is);
        client = new FastDfsClient(properties);
    }

    @Test
    public void testExport()throws Exception{
        List<CallServerStatisticsVO> list = initData();
        Context context = new Context();
        context.putVar("exportData" , list);
        String url = ExcelUtils.exportExcel("call_server_statistics_template.xlsx", "call_server_statistics", client, context);
        System.out.println("url = " + url);
        client.deleteFile(url);
    }

    private List<CallServerStatisticsVO> initData() {
        Random random = new Random();
        List<CallServerStatisticsVO> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CallServerStatisticsVO vo = new CallServerStatisticsVO();
            vo.setCountTotalTalkTimeOut(random.nextInt(100));
            vo.setEndTime(new Date());
            vo.setStartTime(new Date());
            vo.setServicerId(i+"");
            vo.setServicerName("name" + i);
            vo.setSumFeeTime(random.nextLong());
            vo.setSumTalkTimeOut(random.nextLong());
            vo.setSumPlatFee(random.nextLong()+"");
            list.add(vo);
        }
        return list;
    }
}
