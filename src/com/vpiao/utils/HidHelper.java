package com.vpiao.utils;

import android.content.Context;
import android.util.Log;
import com.Routon.iDRHIDLib.iDRHIDDev;

/**
 *身份证扫描设备帮助类
 * Created by suntao on 2014/11/14.
 */
public final class HidHelper {
    private static final String TAG="HidHelper";

    /**
     * 读取身份证信息
     * @param iDRHIDDev
     * @param sIDInfo
     * @return
     */
    public static int gerSecondIDInfo(iDRHIDDev iDRHIDDev,iDRHIDDev.SecondIDInfo sIDInfo,Context context){
        /**
         * 读取安全模块的状态
         */
        int ret = iDRHIDDev.GetSamStaus();
        if(ret<0){
            //设备未准备好
            Log.d(TAG, "hid may not be ready");
            return -1;
        }
        iDRHIDDev.SamIDInfo samIDInfo  = iDRHIDDev.new SamIDInfo();
        //读安全模块号
        ret = iDRHIDDev.GetSamId(samIDInfo);
        //找卡
        ret = iDRHIDDev.Authenticate();
        Log.v(TAG,"found card id : "+ret);
        if(ret>=0){
            ret = iDRHIDDev.ReadBaseMsg(sIDInfo, context.getFilesDir().getAbsolutePath());
            if(ret<0){
                Log.v(TAG,"read idcard failed");
                //读卡失败
                return -2;
            }
            Log.v(TAG,"read idcard success");
            //蜂鸣，灯光
            iDRHIDDev.BeepLed(true, true, 500);

            return 0;
        }else{
            //找卡失败
            return -3;
        }
    }

}
