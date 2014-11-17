package com.vpiao.print.sdk.usb;

import com.vpiao.print.sdk.IPrinterPort;

/**
 * USB打印机接口
 * Created by suntao on 2014/11/17.
 */
public class USBPort implements IPrinterPort {
    /**
     * 打开打印机
     */
    @Override
    public void open() {

    }

    /**
     * 关闭打印机
     */
    @Override
    public void close() {

    }

    /**
     * 打印
     *
     * @param paramArrayOfByte
     * @return
     */
    @Override
    public int write(byte[] paramArrayOfByte) {
        return 0;
    }

    @Override
    public byte[] read() {
        return new byte[0];
    }

    /**
     * 获取打印机的状态
     *
     * @return
     */
    @Override
    public int getState() {
        return 0;
    }
}
