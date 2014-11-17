package com.vpiao.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.Routon.iDRHIDLib.iDRHIDDev;
import com.example.VpiaoAssistant.R;
import com.vpiao.broadcastreceivers.UsbStateReceiver;
import com.vpiao.utils.consts.Const;

/**
 * 身份证读取
 * Created by TaoSun on 2014/11/16.
 */
public class CheckWayIdCard extends Fragment {

    private static final String TAG="CheckWayIdCard";
    private Activity context;
    private BroadcastReceiver usbReceiver;
    private PendingIntent mPermissionIntent;
    private UsbManager usbManager;
    private iDRHIDDev mHIDDev;
    private UsbDevice mDevice;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=this.getActivity();
        View view = inflater.inflate(R.layout.check_way_id_card,null);
        mHIDDev=new iDRHIDDev();
        return view;
    }

    /**
     * 读取二代身份证事件
     */
    private void ReadIdCardClick(){
        Button readIdCardButton= (Button) context.findViewById(R.id.btn_read_idcard);
        readIdCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"start read idCard");
                TextView textView= (TextView) context.findViewById(R.id.text_view_card_id);
                if(mDevice==null){
                    Log.d(TAG,"please insert card reader");
                    Toast.makeText(context,R.string.note_insert_card_read,Toast.LENGTH_SHORT).show();
                }else{

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        usbManager=(UsbManager) context.getSystemService(Context.USB_SERVICE);
        usbReceiver=new UsbStateReceiver(handler,null);
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        context.registerReceiver(usbReceiver,filter);
        mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(Const.ACTION_USB_PERMISSION), 0);
        for (UsbDevice device :  usbManager.getDeviceList().values()) {
            if( device.getVendorId() == 1061 && device.getProductId() == 33113 ) {
                usbManager.requestPermission(device, mPermissionIntent);
                Log.d(TAG, "usb device: checked");
            }
        }
    }

    @Override
    public void onPause() {
        context.unregisterReceiver(usbReceiver);
        super.onPause();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            if(msg.what==Const.USB_STATE_MSG){
                Intent intent = context.getIntent();
                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if(msg.arg1==Const.USB_STATE_ON){
                    //Usb设备插入
                    if(mDevice!=null){
                        mHIDDev.closeDevice();
                        mDevice=null;
                    }
                    if(device!=null&&usbManager.hasPermission(device)){
                        mHIDDev.openDevice(usbManager,device);
                        mDevice=device;
                        Log.d(TAG,"hid device is open");
                    }else {
                        Log.d(TAG,"hid device not attached or no permission");
                    }
                }else{
                    if(mDevice!=null&&mDevice.equals(device)){
                        mHIDDev.closeDevice();
                        mDevice=null;
                        Log.d(TAG,"hid device has dettached");
                    }
                }
            }
        }
    };
}