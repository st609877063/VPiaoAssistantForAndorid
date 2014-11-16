package com.vpiao.utils;

import android.util.Log;
import com.vpiao.utils.consts.Const;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.io.*;

/**
 * Created by suntao on 2014/11/12.
 * 与接口的socket通信操作帮助类
 */
public final class SocketHelper {

    /**
     * 创建socket链接
     * @param hostName 主机主机名（IP）
     * @param port 端口号
     */
    private static Socket createSocket(String hostName,int port) throws IOException {
        InetAddress addr = InetAddress.getByName(hostName);
        Socket socket = new Socket(addr, port);
        //socket.setReceiveBufferSize(Const.SOCKET_RECEIVE_BUFFER_SIZE);
        Log.v("socket","socket connected");
        return socket;
    }

    /**
     * 关闭Socket链接
     * @param socket
     */
    private static void closeSocket(Socket socket) {
        if(socket!=null&&socket.isClosed()==false){
            try {
                socket.close();
                Log.v("socket","socket closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载数据文件
     * @return 文件的路径
     */
    public static String downloadDataFile() throws IOException {
        Socket socket=null;
        DataInputStream inputStream=null;
        OutputStream outputStream=null;
        try {
            socket=createSocket(Const.FILE_SERVER_IP,Const.FILE_SERVER_PORT);
            socket.setSoTimeout(Const.DOWNLOAD_TIMEOUT);

            //服务器输出
            inputStream=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            //向服务器发送数据
            outputStream=socket.getOutputStream();
            //版本号
            outputStream.write(new byte[]{Const.DOWNLOAD_VERSION});
            //商户，与windows平台通信，string前需要添加""
            outputStream.write(("\u0006"+Const.SUPPLIER_CODE).getBytes());
            //下载命令
            outputStream.write(ByteHelper.toLH(Const.DOWNLOAD_CMD));
            //下载消息
            outputStream.write(ByteHelper.toLH(Const.DOWNLOAD_MSG));
            outputStream.write(ByteHelper.toLH(0));
            outputStream.flush();
            Log.v("socket","send complete");
            String fileName=saveFile(inputStream,Const.SUPPLIER_CODE+".zip");
            return fileName;
        }
        finally {
            if(outputStream!=null){
                outputStream.close();
            }
            if(inputStream!=null){
                inputStream.close();
            }
            closeSocket(socket);
        }
    }

    /**
     * 保存文件，优先考虑外置存储（SD卡），如果没有使用设备存储
     * @param stream
     * @param fileName
     * @return 文件路径名称
     */
    private static String saveFile(DataInputStream stream,String fileName) throws IOException {
        if(stream==null){
            throw new IOException("stream = null");
        }
        String path=FileHelper.getUseRootPath();
        int size=FileHelper.createDowloadFile(stream,path,fileName);
        return size>0? path+File.separator+fileName:"";
    }

}
