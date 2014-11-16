package com.vpiao.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import com.example.VpiaoAssistant.R;

/**
 * 网络连接不可用时的对话框
 * Created by suntao on 2014/11/13.
 */
public class NoNetworkAlertDialog {
    /**
     * 网络连接不可用时的对话框
     * @param activity
     */

    public NoNetworkAlertDialog(final Activity activity){
        final Dialog alertDialog = new  AlertDialog.Builder(activity).setTitle(R.string.alert_title)
                .setMessage(R.string.network_unable_msg)
                .setPositiveButton(activity.getString(R.string.button_text_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.v("network", "yes");
                        Intent intent = null;
                        //大于10，则说明是3.0以上版本
                        if (Build.VERSION.SDK_INT > 10) {
                            intent = new Intent(Settings.ACTION_SETTINGS);
                        } else {
                            intent = new Intent();
                            ComponentName component=new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                            intent.setComponent(component);
                            intent.setAction("android.intent.action.VIEW");
                        }
                        activity.startActivity(intent);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.button_text_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.v("network", "no");
                        dialogInterface.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }
}
