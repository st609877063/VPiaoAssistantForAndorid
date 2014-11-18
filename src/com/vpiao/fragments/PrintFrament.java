package com.vpiao.fragments;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.VpiaoAssistant.R;

/**
 * 打印测试
 * Created by suntao on 2014/11/17.
 */
public class PrintFrament extends Fragment {
    private static final String TAG="CheckWayIdCard";
    private BroadcastReceiver usbReceiver;
    private PendingIntent mPermissionIntent;
    private UsbManager usbManager;
    private UsbDevice mDevice;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.check_way_id_card,null);
        return view;
    }
}
