package com.myfutech.fastdfs;

import lombok.Data;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerServer;

/**FastDfsConnection连接对象
 * Created by Lq on 2019/2/28.
 */
@Data
public class FastDfsConnection {
    private StorageServer storageServer;
    private TrackerServer trackerServer;
    private StorageClient1 storageClient1;

    public FastDfsConnection() {
    }

    public FastDfsConnection(StorageServer storageServer, TrackerServer trackerServer, StorageClient1 storageClient1) {
        this.storageServer = storageServer;
        this.trackerServer = trackerServer;
        this.storageClient1 = storageClient1;
    }


}
