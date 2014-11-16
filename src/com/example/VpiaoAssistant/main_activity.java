package com.example.VpiaoAssistant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import com.vpiao.domain.User;
import com.vpiao.threads.DownloadDataThread;

import static android.app.ProgressDialog.*;

public class main_activity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

//        final ProgressDialog progressDialog=ProgressDialog.show(this,null,this.getString(R.string.load_data));
//        Thread thread=new Thread(){
//            @Override
//            public void run() {
//                try {
//                    sleep(2000);
//                }catch (Exception ex){
//                    ex.printStackTrace();
//                }
//                progressDialog.dismiss();
//            }
//        };
//        thread.start();

        DownloadDataThread thread1=new DownloadDataThread(this);
        new Thread(thread1).start();
    }
}
