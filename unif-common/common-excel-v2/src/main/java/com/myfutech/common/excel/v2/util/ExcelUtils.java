package com.myfutech.common.excel.v2.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.NoSuchElementException;

/**
* Excel导入导出工具
*/
public class ExcelUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * 功能描述：导出Excel
     *
     * @param list 导出的实体类
     * @param pojoClass 映射的实体类
     * @param type 导出文件类型   HSSF : xls ; XSSF : xlsx
     * @param os 输出流
     */
    public static  void exportExcel(List<?> list, Class<?> pojoClass, ExcelType type, OutputStream os) {
        ExportParams exportParams = new ExportParams();
        exportParams.setSheetName("Sheet1");
        exportParams.setType(type);
        exportExcel(list, pojoClass, exportParams, os);
    }
    /**
     * 功能描述：导出Excel
     *
     * @param list 导出的实体类
     * @param pojoClass 映射的实体类
     * @param exportParams 文件导出参数配置
     * @param os 输出流
     */
    public static  void exportExcel(List<?> list, Class<?> pojoClass, ExportParams exportParams, OutputStream os) {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        if (workbook != null) {
            downLoadExcel(os,workbook);
        }
    }

    /**
     * 功能描述：Excel导出
     *
     *    response.setCharacterEncoding("UTF-8");
     *    response.setHeader("content-Type", "application/vnd.ms-excel");
     *    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
     *
     * @param os 输出流
     * @param workbook Excel对象
     * @return
     */
    private static void downLoadExcel(OutputStream os, Workbook workbook) {
        try {
            workbook.write(os);
            workbook.close();
        } catch (IOException e) {
            throw new  RuntimeException(e);
        }
    }

    /**
     * 功能描述：根据接收的Excel文件来导入Excel,并封装成实体类
     *
     * @param is 上传的文件流
     * @param titleRows 表标题的行数
     * @param headerRows 表头行数
     * @param pojoClass Excel实体类
     */
    public static <T> List<T> importExcel(InputStream is, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (is == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(is, pojoClass, params);
        } catch (NoSuchElementException e) {
            throw new RuntimeException("excel文件不能为空", e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return list;
    }
}
