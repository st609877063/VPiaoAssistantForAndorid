package com.vpiao.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.example.VpiaoAssistant.R;
import com.vpiao.threads.DownloadDataThread;

/**
 * 没有用户数据处理
 * Created by suntao on 2014/11/14.
 */
public class NoUserDataAlterDialog {
    /**
     * 构造函数
     * @param activity
     */
    public NoUserDataAlterDialog(final Activity activity){
        final AlertDialog dialog= new AlertDialog.Builder(activity)
                .setTitle(R.string.alert_title)
                .setMessage(R.string.not_user_data)
                .setPositiveButton(R.string.button_text_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DownloadDataThread thread=new DownloadDataThread(activity);
                        thread.run();
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton(R.string.button_text_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO: 退出整个应用程序
                        dialogInterface.dismiss();
                    }
                })
                .create();
        dialog.show();
    }
}
