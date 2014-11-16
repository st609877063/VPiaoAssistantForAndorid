package com.vpiao;

import android.app.Application;
import com.vpiao.domain.User;

/**
 * 保持全局变量的app
 * Created by suntao on 2014/11/14.
 */
public class VpiaoApp extends Application {
    /**
     * 已登录的User
     */
    public User LoggedUser;
}
