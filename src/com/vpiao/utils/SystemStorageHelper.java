package com.vpiao.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.IOException;

/**
 * Created by suntao on 2014/11/12.
 * 系统内部存储系统帮助类
 */
public class SystemStorageHelper {
    /**
     * 获取空闲空间(单位KB)
     * @return
     */
    public static long getAvailableSize() throws IOException {
        String path= Environment.getRootDirectory().getPath();
        StatFs statFs=new StatFs(path);
        //获取block的大小
        long blockSize=statFs.getBlockSizeLong();
        long availableBlock=statFs.getAvailableBlocksLong();
        return (blockSize*availableBlock)/1024;
    }

    /**
     * 获取文件路径
     * @return
     * @throws IOException
     */
    public static String getPath() throws IOException {
        return Environment.getRootDirectory().getPath();
    }
}
