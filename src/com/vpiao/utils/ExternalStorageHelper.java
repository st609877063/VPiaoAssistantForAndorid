package com.vpiao.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.IOException;

/**
 * Created by suntao on 2014/11/12.
 * 外部存储帮助类（主要是SDCard）
 */
public final class ExternalStorageHelper {
    /**
     * 判断SD卡是否可用
     * @return
     */
    public static boolean canUse(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡文件路径
     * @return
     * @throws IOException
     */
    public static String getPath() throws IOException {
        if(canUse()){
            return Environment.getExternalStorageDirectory().getPath();
        }
        throw new IOException("not ready");
    }

    /**
     * 获取空闲空间(单位KB)
     * @return
     */
    public static long getAvailableSize() throws IOException {
        String path=getPath();
        StatFs statFs=new StatFs(path);
        //获取block的大小
        long blockSize=statFs.getBlockSizeLong();
        long availableBlock=statFs.getAvailableBlocksLong();
        return (blockSize*availableBlock)/1024;
    }
}
