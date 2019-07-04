package com.myfutech.common.excel.v2.util;

import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.myfutech.common.excel.v2.vo.CallServerStatisticsVO;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtilsTest {

    @Test
    public void testExcel()throws Exception{
        List<CallServerStatisticsVO> list = initData();
        File file =new File("D:\\测试excel.xlsx");
        if (!file.exists()){
            file.createNewFile();
        }
        OutputStream output = new FileOutputStream(file);
        //执行导出后传入数据，会被清理
        int size = list.size();
        ExcelUtils.exportExcel(list, CallServerStatisticsVO.class,ExcelType.XSSF,output);
        output.close();
        System.out.println("导出成功，总条数：" + size);

        InputStream input = new FileInputStream(file);
        List<CallServerStatisticsVO> voList = ExcelUtils.importExcel(input, 0, 1, CallServerStatisticsVO.class);
        input.close();
        System.out.println("导入成功，总条数：" + voList.size());

        file.delete();
    }

    private List<CallServerStatisticsVO> initData() {
        List<CallServerStatisticsVO> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            CallServerStatisticsVO vo = new CallServerStatisticsVO();
            vo.setName("测试"+i);
            vo.setPhone("52"+i);
            list.add(vo);
        }
        return list;
    }
}
