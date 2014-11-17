package com.vpiao.fragments;

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
import com.vpiao.utils.HidHelper;
import com.vpiao.utils.consts.Const;

/**
 * 身份证读取
 * Created by TaoSun on 2014/11/16.
 */
public class CheckWayIdCard extends Fragment {

    private static final String TAG="CheckWayIdCard";
    private BroadcastReceiver usbReceiver;
    private PendingIntent mPermissionIntent;
    private UsbManager usbManager;
    private iDRHIDDev mHIDDev;
    private UsbDevice mDevice;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.check_way_id_card,null);
        mHIDDev=new iDRHIDDev();
        ReadIdCardClick();
        return view;
    }

    /**
     * 读取二代身份证事件
     */
    private void ReadIdCardClick(){

        final Button readIdCardButton= (Button) view.findViewById(R.id.btn_read_idcard);
        final TextView textView= (TextView) view.findViewById(R.id.text_view_card_id);
        final Context context=view.getContext();
        readIdCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"start read idCard");

                if(mDevice==null){
                    Log.d(TAG,"please insert card reader");
                    Toast.makeText(view.getContext(),R.string.note_insert_card_read,Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    iDRHIDDev.SecondIDInfo sIDInfo = mHIDDev.new SecondIDInfo();
                    int result = HidHelper.gerSecondIDInfo(mHIDDev, sIDInfo, context);
                    switch (result) {
                        case -1:
                            Toast.makeText(view.getContext(), R.string.note_device_not_ready, Toast.LENGTH_SHORT).show();
                            return;
                        case -2:
                            Toast.makeText(view.getContext(), R.string.note_read_id_card_failed, Toast.LENGTH_SHORT).show();
                            return;
                        case -3:
                            Toast.makeText(view.getContext(), R.string.note_not_found_idcard, Toast.LENGTH_SHORT).show();
                            return;
                        case 0:
                            textView.setText(sIDInfo.name);
                            return;
                    }
                }
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        usbManager=(UsbManager) view.getContext().getSystemService(Context.USB_SERVICE);
        usbReceiver=new UsbStateReceiver(handler,null);
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(Const.HID_USB_PERMISSION);
        view.getContext().registerReceiver(usbReceiver, filter);
        mPermissionIntent = PendingIntent.getBroadcast(view.getContext(), 0, new Intent(Const.HID_USB_PERMISSION), 0);
        for (UsbDevice device :  usbManager.getDeviceList().values()) {
            if( device.getVendorId() == 1061 && device.getProductId() == 33113 ) {
                usbManager.requestPermission(device, mPermissionIntent);
                Log.d(TAG, "usb device: checked");
            }
        }
    }

    @Override
    public void onPause() {
        view.getContext().unregisterReceiver(usbReceiver);
        super.onPause();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            if(msg.what==Const.USB_STATE_MSG){
                Intent intent = (Intent) msg.obj;
                UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if(msg.arg1==Const.USB_HID_STATE_ON){
                    //Usb设备插入
                    if(mDevice!=null){
                        mHIDDev.closeDevice();
                        mDevice=null;
                    }
                    if(device!=null){
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
                        Log.d(TAG,"hid device has detached");
                    }
                }
            }
        }
    };
}