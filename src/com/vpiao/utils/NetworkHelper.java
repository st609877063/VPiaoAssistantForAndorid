package com.vpiao.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by suntao on 2014/11/12.
 * 网络帮助类
 */
public final class NetworkHelper {
    /**
     * 判断设备是否联网
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager!=null &&  manager.getActiveNetworkInfo()!=null && manager.getActiveNetworkInfo().isAvailable();
    }


}
