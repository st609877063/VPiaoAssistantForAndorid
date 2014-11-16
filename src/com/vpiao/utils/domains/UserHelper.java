package com.vpiao.utils.domains;

import android.util.Log;
import android.util.Xml;
import com.vpiao.domain.User;
import com.vpiao.utils.FileHelper;
import com.vpiao.utils.Md5Helper;
import com.vpiao.utils.consts.Const;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户类
 * Created by suntao on 2014/11/14.
 */
public final class UserHelper {
    /**
     * 根据登陆名获取用户信息
     * @param loginName
     * @return
     */
    public static User getUser(String loginName){
        return null;
    }

    /**
     * 验证用户登陆
     * @param loginName
     * @param password
     * @return
     */
    public static boolean checkLogin(String loginName,String password,User user){

        try {
            String md5Password=Md5Helper.getMD5(password);
            Log.v("login_password", String.format("%s|%s", md5Password, user.getPassword().toLowerCase()));
            return user.getPassword().toLowerCase().equals(md5Password);
        }catch (NoSuchAlgorithmException ex){
            Log.e("login",ex.getMessage());
        }

        return false;
    }

    /**
     * 在离线数据文件中加载所用用户信息
     * @return
     */
    public static List<User> getUserList() throws IOException, XmlPullParserException {
        String path=FileHelper.getUseRootPath()+File.separator+ Const.SUPPLIER_CODE+File.separator+Const.USER_DATE_FILE_NAME;
        return deserializeFromXml(path);
    }



    /**
     * 反序列化为User
     * @param path 文件路径
     * @return
     */
    private static List<User> deserializeFromXml(String path) throws IOException, XmlPullParserException {
        if(path==null||path.isEmpty()){
            throw new IllegalArgumentException("path can not null or empty");
        }
        FileInputStream stream=null;
        try {
            stream=new FileInputStream(path);
            return deserializeFromXml(stream);
        }catch (XmlPullParserException ex){
            ex.printStackTrace();
            throw ex;
        }
        finally {
            if(stream!=null){
                stream.close();
            }
        }
    }
    /**
     *反序列化为User
     * @param inputStream 输入的xml字节流
     * @return Map　key=登陆名 V=用户对象
     */
    private static List<User> deserializeFromXml(InputStream inputStream) throws XmlPullParserException, IOException {
        List<User> list = null;
        XmlPullParser parser = Xml.newPullParser();

        parser.setInput(inputStream, "UTF-8");
        int eventType = parser.getEventType();
        String tagName = "";
        String value = "";
        User userInfo = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    list = new ArrayList<User>();
                    break;
                case XmlPullParser.START_TAG:
                    tagName = parser.getName();
                    if ("CNName".equals(tagName)) {
                        userInfo = new User();
                        value = parser.nextText();
                        userInfo.setCnName(value);
                    } else if ("LoginName".equals(tagName)) {
                        value = parser.nextText();
                        userInfo.setLoginName(value);
                    } else if ("Password".equals(tagName)) {
                        value = parser.nextText();
                        userInfo.setPassword(value);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = parser.getName();
                    if ("SupplierUserBoDto".equals(tagName)) {
                        list.add(userInfo);
                    }
                    break;
            }
            eventType=parser.next();
        }

        return list;
    }

}
