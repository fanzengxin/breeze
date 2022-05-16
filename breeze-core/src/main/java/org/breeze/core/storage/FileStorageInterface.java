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
     * @return 文件路径
     */
    String upload(InputStream inputStream);

    /**
     * 文件下载
     *
     * @param filePath      文件路径
     * @param dataBaseIndex 文件库
     * @return 文件下载流
     */
    InputStream download(String filePath, String dataBaseIndex);

    /**
     * 文件删除
     *
     * @param filePath      文件路径
     * @param dataBaseIndex 文件库
     * @return 删除成功标记
     */
    boolean remove(String filePath, String dataBaseIndex);

    /**
     * 获取文件外部访问链接
     *
     * @param filePath      文件路径
     * @param dataBaseIndex 文件库
     * @return 文件访问地址
     */
    String getUrl(String filePath, String dataBaseIndex);
}
