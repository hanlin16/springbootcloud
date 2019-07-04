package com.myfutech.fastdfs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class FastDfsClientTest {

    @Test
    public void fastDfsClientTest() throws Exception{
        InputStream is = getClass().getClassLoader().getResourceAsStream("fastdfs-client.properties");
        Properties properties = new Properties();
        properties.load(is);
        FastDfsClient fastDfsClient = new FastDfsClient(properties);
        String path = fastDfsClient.uploadFile(new File("D:\\test.png"), "test.png");
        Assert.assertNotNull(path);
        System.out.println("path = " + path);
        int length = fastDfsClient.downloadFile(path, new File("D:\\2.jpeg"));
        System.out.println("length = " + length);
        boolean result = fastDfsClient.deleteFile(path);
        Assert.assertTrue(result);
    }

    @Test
    public void fastDfsClientThreadTest() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("fastdfs-client.properties");
        Properties properties = new Properties();
        properties.load(is);
        FastDfsClient fastDfsClient = new FastDfsClient(properties);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                Long StartTime = System.currentTimeMillis();
                String path = fastDfsClient.uploadFile(new File("D:\\test2.jpg"), "test2.jpg");
                System.out.println("上传文件耗时 -> " + (System.currentTimeMillis() - StartTime));
                Assert.assertNotNull(path);
                System.out.println("path = " + path);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
    }
}
