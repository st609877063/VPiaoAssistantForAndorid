package com.vpiao.domain;

import com.vpiao.utils.consts.Const;

/**
 * 用户基本信息
 * Created by suntao on 2014/11/14.
 */
public class User {

    /**
     * 中文名称
     */
    private String  cnName;
    /**
     * 登陆名称
     */
    private String loginName;
    /**
     * 登陆密码
     */
    private String password;
    /**
     * 商户编码
     */
    private String supplierCode;

    /**
     * 获取中文名称
     * @return
     */
    public String getCnName() {
        return cnName;
    }

    /**
     * 设置中文名
     * @param cnName
     */
    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    /**
     * 获取登陆名称
     * @return
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 设置登陆名称
     * @param loginName
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * 获取密码
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取商户编号
     * @return
     */
    public String getSupplierCode() {
        return Const.SUPPLIER_CODE;
    }

    /**
     * 重写toString方法，以方便前台应用
     * @return
     */
    @Override
    public String toString(){
        return cnName;
    }

//    public void setSupplierCode(String supplierCode) {
//        this.supplierCode = supplierCode;
//    }
}
