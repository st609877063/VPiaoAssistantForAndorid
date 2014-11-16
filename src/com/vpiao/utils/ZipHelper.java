package com.vpiao.utils;

import android.util.Log;
import com.vpiao.utils.consts.Const;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
/**
 * Created by suntao on 2014/11/13.
 * zip解压
 */
public final class ZipHelper {

    /**
     * 解压缩
     */
    public static void unZip(){
        try {
            String path=FileHelper.getUseRootPath()+File.separator;
            String fileName=path + Const.SUPPLIER_CODE+".zip";
            String targetName=path+Const.SUPPLIER_CODE+File.separator;
            unZip(fileName,targetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 解压zip文件
     * @param unZipfileName
     * @param mDestPath
     */
    public static void unZip(String unZipfileName, String mDestPath) throws IOException {

        //删除原来的路径
        FileHelper.delete(mDestPath);
        FileOutputStream fileOut = null;
        ZipInputStream zipIn = null;
        ZipEntry zipEntry = null;
        File file = null;
        int readedBytes = 0;
        byte buf[] = new byte[1024];
        try {
            zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(unZipfileName)));
            while ((zipEntry = zipIn.getNextEntry()) != null) {
                file = new File(mDestPath + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    file.mkdirs();
                } else {
                    // 如果指定文件的目录不存在,则创建.
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    fileOut = new FileOutputStream(file);
                    while ((readedBytes = zipIn.read(buf)) > 0) {
                        fileOut.write(buf, 0, readedBytes);
                    }
                    fileOut.close();
                }
                zipIn.closeEntry();
            }
        } catch (IOException ioe) {
           throw ioe;
        }
    }
}
