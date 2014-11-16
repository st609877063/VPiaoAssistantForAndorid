package com.vpiao.print.sdk;

/**
 * 打印机接口
 * Created by suntao on 2014/11/14.
 */
public interface IPrinterPort {
    /**
     * 打开打印机
     */
    public abstract void open();

    /**
     * 关闭打印机
     */
    public abstract void close();

    /**
     * 打印
     * @param paramArrayOfByte
     * @return
     */
    public abstract int write(byte[] paramArrayOfByte);

    public abstract byte[] read();

    /**
     * 获取打印机的状态
     * @return
     */
    public abstract int getState();
}
