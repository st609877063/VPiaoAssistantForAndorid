package com.vpiao.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * HID设备接收广播信息
 * Created by suntao on 2014/11/14.
 */
public final class UsbStateReceiver extends  BroadcastReceiver{

    private static final String TAG="UsbStateReceiver";
    public static final int USB_STATE_MSG = 0x00020;
    public static final int USB_STATE_ON = 0x00021;
    public static final int USB_STATE_OFF = 0x00022;
    private Handler handler;
    public UsbStateReceiver(Handler handler){
        super();
        this.handler=handler;
    }

//    private void registerReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
//        filter.addAction(Intent.ACTION_MEDIA_CHECKING);
//        filter.addAction(Intent.ACTION_MEDIA_EJECT);
//        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
//        filter.addDataScheme("file");
//        this.context.registerReceiver(this, filter);
//    }

//    public void unregisterReceiver(){
//        this.context.unregisterReceiver(this);
//    }



    @Override
    public void onReceive(Context context, Intent intent) {
        if(this.handler==null){
            Log.v(TAG,"handler is null");
            return;
        }
        Log.v(TAG,"action="+intent.getAction());
        Message msg=Message.obtain();
        msg.what=USB_STATE_MSG;
        if(intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)||intent.getAction().equals(Intent.ACTION_MEDIA_CHECKING)){
            msg.arg1=USB_STATE_ON;
        }else{
            msg.arg1=USB_STATE_OFF;
        }
        this.handler.sendMessage(msg);
    }
}
