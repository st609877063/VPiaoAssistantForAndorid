package com.vpiao.utils;

import android.util.Log;
import com.vpiao.utils.consts.Const;
import org.apache.http.util.EncodingUtils;

import java.io.*;
import java.net.SocketTimeoutException;
import java.util.Date;

/**
 * Created by suntao on 2014/11/12.
 * 文件帮助类
 */
public final class FileHelper {

    private static final String TAG="FileHelper";
    /**
     * 创建目录
     * @param parentPath
     * @param path
     * @param isAutoMakeParentPath 如果父路径不存在是否自动生成
     * @throws Exception
     */
    public static void mkdir(String parentPath,String path,boolean isAutoMakeParentPath) throws Exception {
        File parentDir=new File(parentPath);
        if(parentDir.exists()==false){
            if(!isAutoMakeParentPath){
                throw new IOException("parentPath not exist");
            }else{
                //创建父目录
                parentDir.mkdirs();
            }
        }else{
            if(parentDir.isDirectory()==false){
                throw new IllegalArgumentException("parentPath is not a path");
            }
            File dir=new File(parentPath+File.separator+path);
            dir.mkdirs();
        }
    }

    /**
     * 判断指定的路径是否存在
     * @param path
     * @return
     */
    public static boolean isexists(String path){
        File file=new File(path);
        return file.exists();
    }

    /**
     * 创建目录
     * @param path
     * @return
     */
    public static boolean mkdirs(String path){
        File file=new File(path);
        if(file.exists()){
            return true;
        }
        return file.mkdirs();
    }

    /**
     * 读文件
     * @param path 路径
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
   public static String readFile(String path,String fileName) throws IOException {
      return readFile(path+File.separator+fileName);
   }

    /**
     * 读取文件
     * @param fileName 文件名称
     * @return
     * @throws IOException
     */
    public static String readFile(String fileName) throws IOException {
        FileInputStream stream=null;
        try {
            stream=new FileInputStream(fileName);
            int len=stream.available();
            byte[] buffer=new byte[len];
            stream.read(buffer);
            String out= EncodingUtils.getString(buffer,"UTF-8");
            return out;
        }finally {
            stream.close();
        }
    }

    /**
     * 根据流创建文件
     * @param inputStream
     * @param path
     * @param fileName
     * @return 接收的字节数
     */
    public static int createDowloadFile(DataInputStream inputStream,String path,String fileName) throws IOException {
        if(inputStream==null){
            throw new IOException("inputStream can not null");
        }
        int bufferSize = Const.SOCKET_RECEIVE_BUFFER_SIZE;
        byte[] buffer = new byte[bufferSize];
        int fileSize=0;
        DataOutputStream fileOut=null;
        try {
            int read=inputStream.read(buffer);
            //如果没有数据，则返回
            if(read<0){
                return 0;
            }
            fileSize=read;
            File paths=new File(path);
            if(paths.exists()==false){
                paths.mkdirs();
            }
            fileOut = new DataOutputStream(new FileOutputStream(path+File.separator+fileName));
            //如果保持的文件为zip,忽略接收字节的前8个字节
            if(fileName.endsWith(".zip")){
                fileOut.write(buffer, 16, read-16);
            }else{
                fileOut.write(buffer, 0, read);
            }
            while (read>=0){
                read=inputStream.read(buffer);
                fileOut.write(buffer, 0, read);
                fileSize+=read;
            }

        }catch (SocketTimeoutException ex){
            ex.printStackTrace();
        }finally {
            inputStream.close();
            if(fileOut!=null){
                fileOut.close();
            }
            Log.v(TAG,"filesize="+fileSize);
        }

        return fileSize;
    }

    /**
     * 删除文件或者目录（包括子目录）
     * @param path
     * @return
     */
    public static boolean delete(String path){
        File file=new File(path);
        if(file.exists()==false){
            return true;
        }
        if(file.isDirectory()){
            String[] childerns=file.list();
            for (int i = 0; i < childerns.length; i++) {
                boolean result=delete(path+File.separator+childerns[i]);
                if(result==false){
                    return false;
                }
            }
        }
        return file.delete();
    }

    /**
     * 获取app使用的跟目录，优先使用外部存储
     * @return
     */
    public static String getUseRootPath() throws IOException {
        String path="";
        if(ExternalStorageHelper.canUse()){
            path=ExternalStorageHelper.getPath()+File.separator+Const.DATA_PATH;
        }else{
            path=SystemStorageHelper.getPath()+File.separator+Const.DATA_PATH;
        }
        return path;
    }

    /**
     * 判断是否有离线数据
     * @return
     */
    public static boolean hasOfflineData() throws IOException {
        String path=getUseRootPath()+File.separator+Const.SUPPLIER_CODE+File.separator;
        return hasOfflineData(path);
    }

    private static boolean hasOfflineData(String path)throws IOException{
        return isexists(path);
    }

    /**
     * 获取离线数据的最后更新时间
     * @return
     * @throws IOException
     */
    public static Date getOfflineDataDate() throws IOException {
        String path=getUseRootPath()+File.separator+Const.SUPPLIER_CODE+File.separator;
        if(hasOfflineData(path)){
            File file=new File(path);
            long lastmodify=file.lastModified();
            return new Date(lastmodify);
        }
        throw new FileNotFoundException("offline file not exists!");
    }
}
