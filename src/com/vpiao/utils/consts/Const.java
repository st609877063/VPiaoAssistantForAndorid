package com.vpiao.utils.consts;

import com.vpiao.domain.UsbDeviceInfo;

/**
 * Created by suntao on 2014/11/12.
 * 系统常量
 */
public final  class Const {
    /**
     * 文件服务器IP
     */
    public static final String FILE_SERVER_IP="210.14.151.229";
    /**
     * 文件服务器端口号
     */
    public static final int FILE_SERVER_PORT=8803;
    /**
     * 商户编号
     */
    public static final String SUPPLIER_CODE="100264";
    /**
     * 下载文件命令
     */
    public static final short DOWNLOAD_CMD=1005;
    /**
     * 下载文件版本
     */
    public static final byte DOWNLOAD_VERSION=1;
    /**
     * 下载请求的消息
     */
    public static final short DOWNLOAD_MSG=1;
    /**
     * 下载请求的超时间
     */
    public static final int DOWNLOAD_TIMEOUT=5*1000;
    /**
     * socket接收区缓存，默认值为8k
     */
    public static final int SOCKET_RECEIVE_BUFFER_SIZE=1024*8;

    public static final String DATA_PATH="/data";
    /**
     * 用户离线数据文件名
     */
    public static final String USER_DATE_FILE_NAME="Users.xml";
    /**
     * 请求加密的token
     */
    public static final  String REQUEST_TOKEN="Y2Y0NTlkYzMtY2EzOC00OTQ2";

    /**
     * 相应加密的token
     */
    public static final  String RESPONSE_TOKEN="Y2Y0NTlkYzMtY2EzOC00OTQ2";
    /**
     * HID设备
     */
    public static final UsbDeviceInfo HID=new UsbDeviceInfo(1061,33113,"HID");
    /**
     * 新北洋POST打印机设备
     */
    public static final UsbDeviceInfo SNBC_POS_PRINT=new UsbDeviceInfo(5455,5455,"SNBC");

    public static final int USB_STATE_MSG = 0x00020;
    public static final int USB_STATE_ON = 0x00021;
    public static final int USB_STATE_OFF = 0x00022;
    /**
     * HID设备USB权限
     */
    public static final String ACTION_USB_PERMISSION = "com.vpiao.HID.USB_PERMISSION";



}
