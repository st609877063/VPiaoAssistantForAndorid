package com.vpiao.threads;

import android.app.*;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import com.example.VpiaoAssistant.R;
import com.vpiao.ui.dialogs.NoNetworkAlertDialog;
import com.vpiao.ui.views.Login;
import com.vpiao.utils.NetworkHelper;
import com.vpiao.utils.SocketHelper;
import com.vpiao.utils.ZipHelper;


/**
 * Created by suntao on 2014/11/13.
 * 下载数据
 */
public class DownloadDataThread implements Runnable {

    private Activity activity;
    private ProgressDialog progressDialog;
    /**
     * 构造函数
     * @param activity
     */
    public DownloadDataThread(Activity activity){
        this.activity=activity;
        //判断网络是否可用
        if(NetworkHelper.isNetworkAvailable(this.activity)){
            progressDialog=ProgressDialog.show(activity,null,activity.getString(R.string.download_data));
        }else{
            new NoNetworkAlertDialog(this.activity);
        }
    }
    /**
     * 下载数据
     */
    @Override
    public void run() {
        try {
            SocketHelper.downloadDataFile();
//            Message msg = Message.obtain();
//            msg.what = R.integer.download_completed;
//            handler.sendMessage(msg);
            ZipHelper.unZip();
            Intent intent=new Intent();
            intent.setClass(activity, Login.class);
            activity.startActivity(intent);
            activity.finish();
//            msg=Message.obtain();
//            msg.what = R.integer.unzip_completed;
//            handler.sendMessage(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            progressDialog.dismiss();
        }
    }

    /**
     * 更新说明文档
     */
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            //下载完成
            if(msg.what==R.integer.download_completed){
                progressDialog.setMessage(activity.getString(R.string.unzip_data));
            }else if(msg.what==R.integer.unzip_completed){
                //切换至登陆页面
                //progressDialog.dismiss();
                Intent intent=new Intent();
                intent.setClass(activity, Login.class);
                activity.startActivity(intent);
                activity.finish();
            }
        }
    };

}
