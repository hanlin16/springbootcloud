package com.myfutech.common.excel.util;

import com.myfutech.fastdfs.FastDfsClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.*;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
* Excel导入导出工具
*/
public class ExcelUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);
    private static final String TEMPLATE_FOLDER = "templates";
    private static final String EXPORT_FILE_FORMAT = ".xls";
    private static final String EXPORT_FILE_FORMATX = ".xlsx";

    /**
     * 将数据按照指定的模板导出到指定的文件名中
     *
     * @param templateName       模板文件名称
     * @param targetFileName     目标文件名称
     * @param client             导出的文件放入fastDFS上
     * @param data               需要导出的数据
     * @return                  fastDFS上的文件url
     */
    public static String exportExcel(String templateName, String targetFileName, FastDfsClient client, Context data) throws RuntimeException {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        Path temp = null;
        try {
            if (!targetFileName.contains(".")) {
                if (templateName.endsWith(EXPORT_FILE_FORMATX)) {
                    targetFileName += EXPORT_FILE_FORMATX;
                } else {
                    targetFileName += EXPORT_FILE_FORMAT;
                }
            }
            inputStream = ExcelUtils.class.getClassLoader().getResourceAsStream(TEMPLATE_FOLDER + File.separator + templateName);

            temp = Files.createTempFile("export", targetFileName);
            outputStream = Files.newOutputStream(temp, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            exportByComment(inputStream, outputStream, data);

            Map<String,String> metaList = new HashMap<>(1);
            metaList.put("targetFileName", targetFileName);
            return client.uploadFile(temp.toFile(), targetFileName, metaList);
        }catch (Exception e){
            throw new RuntimeException("文件导出失败",e);
        }finally {
                try {
                    if(inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null){
                        outputStream.close();
                    }
                    if (temp != null) {
                        Files.deleteIfExists(temp);
                    }
                } catch (IOException e) {
                }
        }
    }

    /**
     * 导出数据到excel 模板中
     *
     * @param templateInputStream excel模板文件流
     * @param targetOutputStream  目标输出流
     * @param context             需要导出的数据
     * @throws Exception
     */
    public static void exportByComment(InputStream templateInputStream, OutputStream targetOutputStream, Context context) throws RuntimeException {

        if (templateInputStream == null || targetOutputStream == null || context == null) {
            throw new RuntimeException("templateInputStream or targetOutputStream is null");
        }

        InputStream inputStream = getWorkbookInputStream(templateInputStream);
//        JxlsHelper.getInstance().processTemplate(inputStream, targetOutputStream, context);
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer  = jxlsHelper.createTransformer(inputStream, targetOutputStream);
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator)transformer.getTransformationConfig().getExpressionEvaluator();
        Map<String, Object> map = new HashMap<>();
        //添加自定义功能
        map.put("utils", new JxlsUtils());
        evaluator.getJexlEngine().setFunctions(map);
        try {
            jxlsHelper.processTemplate(context, transformer);
        } catch (IOException e) {
            throw new RuntimeException("文件导出失败",e);
        }

    }

    /**
     * 导出数据到excel 模板中
     *
     * @param templatePath       excel模板全路径
     * @param targetOutputStream 目标输出流
     * @param context            需要导出的数据
     * @throws Exception
     */
    public static void exportByTemplatePath(String templatePath, OutputStream targetOutputStream, Context context) throws RuntimeException {
        InputStream templateInputStream = getWorkbookInputStream(templatePath);
        exportByComment(templateInputStream, targetOutputStream, context);
    }

    /**
     * 导出数据到excel 模板中
     *
     * @param templateName       excel模板名
     * @param targetOutputStream 目标输出流
     * @param context            需要导出的数据
     * @throws Exception
     */
    public static void exportByTemplateName(String templateName, OutputStream targetOutputStream, Context context) throws RuntimeException {
        InputStream inputStream = ExcelUtils.class.getClassLoader().getResourceAsStream(TEMPLATE_FOLDER + File.separator + templateName);
        exportByComment(inputStream, targetOutputStream, context);
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public static List<List<Object>> importExcel(InputStream inputStream) {


        return importExcel(inputStream, 1);
    }

    public static List<List<Object>> importExcel(InputStream inputStream, int startRowIndex) {


        return importExcel(inputStream, startRowIndex, 0);
    }

    public static List<List<Object>> importExcel(String excelFilePath) {

        return importExcel(excelFilePath, 0);
    }

    public static List<List<Object>> importExcel(String excelFilePath, int startRowIndex) {
        return importExcel(excelFilePath, startRowIndex, 0);
    }

    public static List<List<Object>> importExcel(String excelFilePath, int startRowIndex, int startColumnIndex) {
        if (StringUtils.isEmpty(excelFilePath)) {
            LOGGER.error("excel excel File Path is null");
            return new ArrayList<>();
        }
        InputStream inputStream = getWorkbookInputStream(excelFilePath);
        return importExcel(inputStream, startRowIndex, startColumnIndex);
    }

    /**
     * 读取Excel数据返回
     *
     * @param inputStream      excel文件流
     * @param startRowIndex    开始读取数据的行数(行数从0开始)
     * @param startColumnIndex 开始读取数据的列数(列数从0开始)
     * @param size             读取行数（-1为没有限制）
     * @return
     */
    public static List<List<Object>> importExcel(InputStream inputStream, int startRowIndex, int startColumnIndex, int size) {
        List<List<Object>> retData = new ArrayList<>();
        if (inputStream == null) {
            LOGGER.error("excel InputStream is null");
            return retData;
        }
        if (startColumnIndex < 0) {
            startColumnIndex = 0;
        }
        if (startRowIndex < 0) {
            startRowIndex = 0;
        }
        Workbook workbook = getWorkbook(inputStream);
        if (workbook == null) {
            LOGGER.error("excel InputStream build  Workbook exception,please check excel file");
            return retData;
        }

        int sheets = workbook.getNumberOfSheets();
        if (sheets < 1) {
            LOGGER.warn("excel InputStream Number Of Sheets :{}", sheets);
            return retData;
        }

        //读取sheet(页)
        end: for (int sheetNum = 0; sheetNum < sheets; sheetNum++) {
            Sheet sheet = workbook.getSheetAt(sheetNum);
            if (sheet == null) {
                continue;
            }
            int totalRows = sheet.getLastRowNum();
            if (totalRows < 0 || startRowIndex > totalRows) {
                LOGGER.warn("the sheet={} row-num={}, startRowIndex={}", sheet.getSheetName(), totalRows, startRowIndex);
                continue;
            }

            //读取Row,从指定的行开始
            for (int rowNum = startRowIndex; rowNum <= totalRows; rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                int rowCellNum = row.getLastCellNum();
                if (rowCellNum < 0) {
                    continue;
                }
                List<Object> rowData = new ArrayList<>();
                retData.add(rowData);

                //读取列，从指定的列开始
                for (int column = startColumnIndex; column <= rowCellNum + 1; column++) {
                    //获取的内容
                    Cell cell = row.getCell(column);
                    if (cell == null) {
                        rowData.add(null);
                        continue;
                    }
                    rowData.add(getCellValue(cell));
                }

                if (size > 0 && retData.size() >= size){
                    break end;
                }
            }

            startRowIndex = startRowIndex - totalRows;
            if (startRowIndex < 0){
                startRowIndex = 0;
            }
        }
        return retData;
    }

    /**
     * 读取Excel数据返回
     *
     * @param inputStream      excel文件流
     * @param startRowIndex    开始读取数据的行数(行数从0开始)
     * @param startColumnIndex 开始读取数据的列数(列数从0开始)
     * @return
     */
    public static List<List<Object>> importExcel(InputStream inputStream, int startRowIndex, int startColumnIndex) {
        return importExcel(inputStream, startRowIndex, startColumnIndex, -1);
    }


    private static InputStream getWorkbookInputStream(String templatePath) {
        if (StringUtils.isEmpty(templatePath)) {
            LOGGER.error("excel file path is null");
            return null;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(templatePath);
        } catch (Exception e) {
            LOGGER.error("excel file[{}] is not found", e);
            return null;
        }

        return getWorkbookInputStream(fileInputStream);


    }

    private static InputStream getWorkbookInputStream(InputStream templateInputStream) {
        if (templateInputStream == null) {
            return null;
        }
        Workbook workbook = getWorkbook(templateInputStream);
        if (workbook == null) {
            LOGGER.error("excel-template InputStream build  Workbook exception,please check excel file");
            return null;
        }


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
        } catch (IOException e) {
            LOGGER.error("", e);
            return null;
        }

        return new ByteArrayInputStream(out.toByteArray());

    }

    private static Workbook getWorkbook(InputStream inputStream) {
        Workbook workbook = null;
        if (inputStream == null) {
            return workbook;
        }

        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (Exception e) {
            LOGGER.error("excel InputStream build  Workbook exception,please check excel file" , e);
        }
        return workbook;

    }

    public static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellTypeEnum();
        switch (cellType) {
            case STRING:
                return String.valueOf(cell.getStringCellValue()).trim();
            case BOOLEAN:
                return Boolean.valueOf(cell.getBooleanCellValue());
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return DateUtil.getJavaDate(cell.getNumericCellValue());
                }
                return Double.valueOf(cell.getNumericCellValue());
            case FORMULA:
            case ERROR:
            case BLANK:
            case _NONE:
            default:
                return String.valueOf(cell.getStringCellValue()).trim();
        }
    }

    public static class JxlsUtils{
        // 日期格式化
        public String dateFmt(Date date, String fmt) {
            if (date == null) {
                return "";
            }
            try {
                return DateFormatUtils.format(date, fmt);
            } catch (Exception e) {
                LOGGER.error("", e);
            }
            return "";
        }
    }
}
