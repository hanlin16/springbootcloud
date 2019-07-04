package com.myfutech.fastdfs;

import com.myfutech.fastdfs.exception.FastDfsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * FastDfsClient工具类
 */
public class FastDfsClient {

    public static String SERVER_PATH_PREFIX;

    public FastDfsClient(Properties props) {
        SERVER_PATH_PREFIX = props.getProperty("fastdfs.server_url");
        if (SERVER_PATH_PREFIX == null) {
            throw new FastDfsException("fastdfs.server_url（资源URL前缀，即nginx的ip和端口） 不能为空。");
        } else {
            if (!SERVER_PATH_PREFIX.endsWith("/")) {
                SERVER_PATH_PREFIX += "/";
            }
        }
        try {
            ClientGlobal.initByProperties(props);
            FastDfsConnectionFactory.props = props;
            FastDfsConnectionPool.init(props);
        } catch (Exception e) {
            throw new FastDfsException("初始化失败", e);
        }
    }

    /**
     * 上传文件
     *
     * @param file     文件对象
     * @param fileName 文件名
     */
    public String uploadFile(File file, String fileName) {
        return uploadFile(file, fileName, null);
    }

    /**
     * 上传文件
     *
     * @param file     文件对象
     * @param fileName 文件名
     * @param metaList 文件元数据
     */
    public String uploadFile(File file, String fileName, Map<String, String> metaList) {
        if (file == null || fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("文件上传参数异常");
        }
        try {
            byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
            return uploadFile(bytes, fileName, metaList);
        } catch (IOException e) {
            throw new FastDfsException("文件上传异常", e);
        }
    }

    /**
     * base64图片上传
     *
     * @param base64Img
     * @return
     */
    public String uploadFile(String base64Img) {
        if (base64Img == null || base64Img == "") {
            throw new IllegalArgumentException("文件上传参数异常");
        }
        String[] d = base64Img.split("base64,");
        if (d == null || d.length != 2) {
            throw new IllegalArgumentException("文件上传参数异常");
        }
        FastDfsConnection dfsCommon = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(d[1]);
            NameValuePair[] nameValuePairs = getNameValuePairs(null);
            String fileName = d[0];
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length() - 1);
            dfsCommon = FastDfsConnectionPool.borrow();
            String fileId = dfsCommon.getStorageClient1().upload_file1(b, fileName, nameValuePairs);
            return SERVER_PATH_PREFIX + fileId;
        } catch (Exception e) {
            throw new FastDfsException("文件上传异常", e);
        } finally {
            FastDfsConnectionPool.release(dfsCommon);
        }
    }

    /**
     * 上传文件
     *
     * @param bytes    文件bytes
     * @param fileName 文件名
     * @param metaList 文件元数据
     */
    public String uploadFile(byte[] bytes, String fileName, Map<String, String> metaList) {
        if (bytes == null || fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("文件上传参数异常");
        }
        FastDfsConnection dfsCommon = null;
        try {
            NameValuePair[] nameValuePairs = getNameValuePairs(metaList);
            dfsCommon = FastDfsConnectionPool.borrow();
            String fileId = dfsCommon.getStorageClient1().upload_file1(bytes, fileName.substring(fileName.lastIndexOf(".") + 1), nameValuePairs);
            return SERVER_PATH_PREFIX + fileId;
        } catch (Exception e) {
            throw new FastDfsException("文件上传异常", e);
        } finally {
            FastDfsConnectionPool.release(dfsCommon);
        }
    }

    private NameValuePair[] getNameValuePairs(Map<String, String> metaList) {
        NameValuePair[] nameValuePairs = null;
        if (metaList != null) {
            nameValuePairs = new NameValuePair[metaList.size()];
            int index = 0;
            for (Map.Entry<String, String> entry : metaList.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();
                nameValuePairs[index++] = new NameValuePair(name, value);
            }
        }
        return nameValuePairs;
    }

    /**
     * 获取文件元数据
     *
     * @param fileUrl 文件url
     */
    public Map<String, String> getFileMetadata(String fileUrl) {

        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new IllegalArgumentException("获取文件元数据参数异常");
        }
        FastDfsConnection dfsCommon = null;
        try {
            String fileId = fileUrl.substring(SERVER_PATH_PREFIX.length());
            dfsCommon = FastDfsConnectionPool.borrow();
            NameValuePair[] metaList = dfsCommon.getStorageClient1().get_metadata1(fileId);
            HashMap<String, String> map = new HashMap<>();
            if (metaList != null) {
                for (NameValuePair metaItem : metaList) {
                    map.put(metaItem.getName(), metaItem.getValue());
                }
            }
            return map;
        } catch (Exception e) {
            throw new FastDfsException("获取文件元数据异常", e);
        } finally {
            FastDfsConnectionPool.release(dfsCommon);
        }
    }


    /**
     * 获取文件元数据
     *
     * @param fileUrl 文件url
     */
    public boolean putFileMetadata(String fileUrl, Map<String, String> map) {
        if (fileUrl == null || fileUrl.isEmpty() || map == null || map.isEmpty()) {
            throw new IllegalArgumentException("获取文件元数据参数异常");
        }
        FastDfsConnection dfsCommon = null;
        try {
            String fileId = fileUrl.substring(SERVER_PATH_PREFIX.length());
            NameValuePair[] metaList = getNameValuePairs(map);
            dfsCommon = FastDfsConnectionPool.borrow();
            int result = dfsCommon.getStorageClient1().set_metadata1(fileId, metaList, ProtoCommon.STORAGE_SET_METADATA_FLAG_MERGE);
            return result == 0;
        } catch (Exception e) {
            throw new FastDfsException("获取文件元数据异常", e);
        } finally {
            FastDfsConnectionPool.release(dfsCommon);
        }
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件URL
     */
    public boolean deleteFile(String fileUrl) {

        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new IllegalArgumentException("删除文件参数异常");
        }
        String fileId = fileUrl.substring(SERVER_PATH_PREFIX.length());
        FastDfsConnection dfsCommon = null;
        try {
            dfsCommon = FastDfsConnectionPool.borrow();
            int result = dfsCommon.getStorageClient1().delete_file1(fileId);
            return result == 0;
        } catch (Exception e) {
            throw new FastDfsException("删除文件异常", e);
        } finally {
            FastDfsConnectionPool.release(dfsCommon);
        }
    }

    /**
     * 下载文件
     *
     * @param fileUrl 文件URL
     * @param outFile 文件下载保存位置
     * @return
     */
    public int downloadFile(String fileUrl, File outFile) {

        if (outFile == null) {
            throw new IllegalArgumentException("文件下载参数异常");
        }
        try {
            byte[] content = downloadFile(fileUrl);
            FileUtils.writeByteArrayToFile(outFile, content);
            return content.length;
        } catch (Exception e) {
            throw new FastDfsException("文件下载异常", e);
        }
    }

    /**
     * 下载文件
     *
     * @param fileUrl      文件URL
     * @param outputStream 文件下载流
     * @return
     */
    public int downloadFile(String fileUrl, OutputStream outputStream) {

        if (outputStream == null) {
            throw new IllegalArgumentException("文件下载参数异常");
        }
        try {
            byte[] content = downloadFile(fileUrl);
            IOUtils.write(content, outputStream);
            return content.length;
        } catch (Exception e) {
            throw new FastDfsException("文件下载异常", e);
        }
    }

    /**
     * 下载文件
     *
     * @param fileUrl 文件URL
     * @return byte[]
     */
    public byte[] downloadFile(String fileUrl) {

        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new IllegalArgumentException("文件下载参数异常");
        }
        FastDfsConnection dfsCommon = null;
        try {
            String fileId = fileUrl.substring(SERVER_PATH_PREFIX.length());
            dfsCommon = FastDfsConnectionPool.borrow();
            return dfsCommon.getStorageClient1().download_file1(fileId);
        } catch (Exception e) {
            throw new FastDfsException("文件下载异常", e);
        } finally {
            FastDfsConnectionPool.release(dfsCommon);
        }
    }
}
