package com.myfutech.fastdfs;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.csource.fastdfs.*;

import java.util.Properties;

/**
 * FastDfsConnection工厂
 * Created by Lq on 2019/2/28.
 */
public class FastDfsConnectionFactory extends BasePooledObjectFactory<FastDfsConnection> {

    public static Properties props;

    @Override
    public FastDfsConnection create() throws Exception {
        ClientGlobal.initByProperties(props);
        TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
        TrackerServer trackerServer = trackerClient.getConnection();
        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
        return new FastDfsConnection(storageServer, trackerServer, storageClient1);
    }

    @Override
    public PooledObject<FastDfsConnection> wrap(FastDfsConnection fastDfsCommon) {
        return new DefaultPooledObject<>(fastDfsCommon);
    }

    @Override
    public boolean validateObject(PooledObject<FastDfsConnection> p) {
        FastDfsConnection fastDfsConnection = p.getObject();
        boolean flag = false;
        if (null == fastDfsConnection) {
            return false;
        }
        try {
            if (null == fastDfsConnection.getTrackerServer() || null == fastDfsConnection.getStorageServer()) {
                return false;
            }
            flag = ProtoCommon.activeTest(fastDfsConnection.getTrackerServer().getSocket())
                    && ProtoCommon.activeTest(fastDfsConnection.getStorageServer().getSocket());
        } catch (Exception ex) {
            flag = false;
        } finally {
            if (!flag) {
                try {
                    if (null != fastDfsConnection.getTrackerServer()) {
                        fastDfsConnection.getTrackerServer().close();
                    }
                    if (null != fastDfsConnection.getStorageServer()) {
                        fastDfsConnection.getStorageServer().close();
                    }
                } catch (Exception e) {
                }
            }
        }
        return flag;
    }
}
