package org.breeze.core.storage;

import java.io.InputStream;

/**
 * 文件存储接口
 *
 * @Name FileStorageInterface
 * @Author 樊增鑫
 * @Date 2022/5/10 09:21
 * @Version V1.0.0
 **/
public interface FileStorageInterface {

    /**
     * 文件上传
     *
     * @param inputStream
     * @return
     */
    String upload(InputStream inputStream);
}
