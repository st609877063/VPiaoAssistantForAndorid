package com.vpiao.print.sdk.usb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.*;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.vpiao.broadcastreceivers.UsbStateReceiver;
import com.vpiao.print.sdk.IPrinterPort;
import com.vpiao.utils.consts.Const;

/**
 * USB打印机接口
 * Created by suntao on 2014/11/17.
 */
public class USBPort implements IPrinterPort {
    private static final String TAG = "USBPrinter";
    private UsbManager mUsbManager;
    private UsbDevice mUsbDevice;
    private UsbInterface usbInterface;
    private UsbEndpoint inEndpoint;
    private UsbEndpoint outEndpoint;
    private Handler mHandler;
    private int mState;
    private Context mContext;
    private ConnectThread mConnectThread;
    private UsbDeviceConnection connection;
    private BroadcastReceiver mUsbReceiver;
    public USBPort(Context context, UsbDevice usbDevice, Handler handler)
    {
        this.mContext = context;
        this.mUsbManager = ((UsbManager)this.mContext.getSystemService(Context.USB_SERVICE));
        this.mUsbDevice = usbDevice;
        this.mHandler = handler;
        this.mState = 103;
    }

    private class ConnectThread extends Thread
    {
        private ConnectThread() {}

        public void run()
        {
            boolean hasError = true;
            if (USBPort.this.mUsbManager.hasPermission(USBPort.this.mUsbDevice))
            {
                try
                {
                    USBPort.this.usbInterface = USBPort.this.mUsbDevice.getInterface(0);
                    for (int i = 0; i < USBPort.this.usbInterface.getEndpointCount(); i++)
                    {
                        UsbEndpoint ep = USBPort.this.usbInterface.getEndpoint(i);
                        if (ep.getType() == 2) {
                            if (ep.getDirection() == 0) {
                                USBPort.this.outEndpoint = ep;
                            } else {
                                USBPort.this.inEndpoint = ep;
                            }
                        }
                    }
                    USBPort.this.connection = USBPort.this.mUsbManager.openDevice(USBPort.this.mUsbDevice);
                    if ((USBPort.this.connection != null) &&
                            (USBPort.this.connection.claimInterface(USBPort.this.usbInterface, true))) {
                        hasError = false;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            synchronized (this)
            {
                USBPort.this.mConnectThread = null;
            }
            if (hasError)
            {
                USBPort.this.setState(102);
                USBPort.this.close();
            }
            else
            {
                USBPort.this.setState(101);
            }
        }
    }
    /**
     * 打开打印机
     */
    @Override
    public void open() {
        Log.d("USBPrinter", "connect to: " + this.mUsbDevice.getDeviceName());
        if (this.mState != 103) {
            close();
        }
        if (isUsbPrinter(this.mUsbDevice))
        {
            if (this.mUsbManager.hasPermission(this.mUsbDevice))
            {
                connect();
            }
            else
            {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent(Const.PRINT_USB_PERMISSION), 0);
                IntentFilter filter = new IntentFilter(Const.PRINT_USB_PERMISSION);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
                filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
                this.mUsbReceiver=new UsbStateReceiver(handler,pendingIntent);
                this.mContext.registerReceiver(this.mUsbReceiver, filter);
                this.mUsbManager.requestPermission(this.mUsbDevice, pendingIntent);
            }
        }
        else {
            setState(102);
        }
    }

    /**
     * 连接打印机
     */
    private void connect()
    {
        this.mConnectThread = new ConnectThread();
        this.mConnectThread.start();
    }

    /**
     * 关闭打印机
     */
    @Override
    public void close() {
        Log.v(TAG,"close");
        if (this.connection != null)
        {
            this.connection.releaseInterface(this.usbInterface);
            this.connection.close();
            this.connection = null;
        }
        this.mConnectThread = null;
        if (this.mState != 102) {
            setState(103);
        }
        USBPort.this.mContext.unregisterReceiver(USBPort.this.mUsbReceiver);
    }

    /**
     * 打印
     *
     * @param paramArrayOfByte
     * @return
     */
    @Override
    public int write(byte[] paramArrayOfByte) {

        if (this.connection != null) {
            Log.d(TAG,"start print");
            return this.connection.bulkTransfer(this.outEndpoint, paramArrayOfByte, paramArrayOfByte.length, 3000);
        }
        Log.e(TAG,"connection is null");
        return -1;
    }

    @Override
    public byte[] read() {
        if (this.connection != null)
        {
            byte[] retData = new byte[64];
            int readLen = this.connection.bulkTransfer(this.inEndpoint, retData, retData.length, 3000);
            Log.w("USBPrinter", "read length:" + readLen);
            if (readLen > 0)
            {
                if (readLen == 64) {
                    return retData;
                }
                byte[] realData = new byte[readLen];
                System.arraycopy(retData, 0, realData, 0, readLen);
                return realData;
            }
        }
        return null;
    }

    /**
     * 处理usbReceiver的handler
     */
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1==Const.USB_PRINT_STATE_ON){
                Log.d(TAG,"the printer is attached");
                Intent intent= (Intent) msg.obj;
                synchronized (this){
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if ((intent.getBooleanExtra("permission", false)) && (USBPort.this.mUsbDevice.equals(device)))
                    {
                        USBPort.this.connect();
                    }
                    else
                    {
                        USBPort.this.setState(102);
                        Log.e("USBPrinter", "permission denied for device " + device);
                    }
                }
            }else{
                Log.d(TAG,"the printer are detached");
            }


            super.handleMessage(msg);
        }
    };

    /**
     * 判断是否为usb打印设备
     * @param device
     * @return
     */
    public static boolean isUsbPrinter(UsbDevice device)
    {
        int vendorId = device.getVendorId();
        int productId = device.getProductId();
        Log.v(TAG, "device name: " + device.getDeviceName());
        Log.v(TAG, "vid:" + vendorId + " pid:" + productId);
        if (((1155 == vendorId) && (22304 == productId))
                || ((1659 == vendorId) && (8965 == productId))
                || ((5455 == vendorId) && (5455 == productId))
                ) {
            return true;
        }
        return true;
    }
    private synchronized void setState(int state)
    {
        Log.v(TAG, "setState() " + this.mState + " -> " + state);
        if (this.mState != state)
        {
            this.mState = state;
            if (this.mHandler != null) {
                this.mHandler.obtainMessage(this.mState).sendToTarget();
            }
        }
    }

    /**
     * 获取打印机的状态
     *
     * @return
     */
    @Override
    public int getState()
    {
        return this.mState;
    }
}
