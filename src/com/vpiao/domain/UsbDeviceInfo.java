package com.vpiao.domain;

/**
 * //usb设备基础类
 * Created by suntao on 2014/11/14.
 */
public class UsbDeviceInfo {
    public int vendorId;
    public int productId;
    public String deviceName;


    public UsbDeviceInfo(int vendorId, int productId, String deviceName) {
        this.vendorId = vendorId;
        this.productId = productId;
        this.deviceName = deviceName;
    }


}
