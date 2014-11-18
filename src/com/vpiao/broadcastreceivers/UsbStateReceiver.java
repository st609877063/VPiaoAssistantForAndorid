package com.vpiao.broadcastreceivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.vpiao.print.sdk.PrinterConstants;
import com.vpiao.utils.consts.Const;

/**
 * HID设备接收广播信息
 * Created by suntao on 2014/11/14.
 */
public final class UsbStateReceiver extends  BroadcastReceiver{

    private static final String TAG="UsbStateReceiver";

    private Handler handler;
    private PendingIntent pendingIntent;
    public UsbStateReceiver(Handler handler,PendingIntent pendingIntent){
        super();
        this.handler=handler;
        this.pendingIntent=pendingIntent;
    }




    @Override
    public void onReceive(Context context, Intent intent) {
        if(this.handler==null){
            Log.v(TAG,"handler is null");
            return;
        }
        Log.v(TAG,"action="+intent.getAction());
        Message msg=Message.obtain();
        msg.what= Const.USB_STATE_MSG;
        msg.obj=intent;
        String action=intent.getAction();
        if(action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)){
            msg.arg1=Const.USB_STATE_ON;
        }else if(action.equals(Const.HID_USB_PERMISSION)){
            msg.arg1=Const.USB_HID_STATE_ON;
        }else if(action.equals(PrinterConstants.Device.PRINT_USB_PERMISSION)){
            msg.arg1=Const.USB_PRINT_STATE_ON;
        }
        else{
            msg.arg1=Const.USB_STATE_OFF;
        }
        this.handler.sendMessage(msg);
    }
}
