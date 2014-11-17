package com.vpiao.utils;

import android.hardware.usb.UsbDevice;
import com.vpiao.domain.UsbDeviceInfo;

import java.util.List;

/**
 * USB接口帮助类
 * Created by suntao on 2014/11/14.
 */
public final class UsbHelper {


    /**
     * 检查指定的usb设备是否插入
     * @param usbDeviceList
     * @param usbDeviceInfo
     * @return
     */
    public static boolean checkUsbIsInsert(List<UsbDevice> usbDeviceList,UsbDeviceInfo usbDeviceInfo) {
        if(usbDeviceList==null||usbDeviceList.isEmpty()){
            return false;
        }
        if(usbDeviceInfo==null){
            return false;
        }
        for(UsbDevice usbDevice:usbDeviceList){
            if(usbDevice.getVendorId()==usbDeviceInfo.vendorId&&usbDevice.getProductId()==usbDeviceInfo.productId){
                return true;
            }
        }
        return false;
    }





}
