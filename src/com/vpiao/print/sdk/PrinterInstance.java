package com.vpiao.print.sdk;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.util.Log;
import com.vpiao.print.sdk.usb.USBPort;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * 打印机实例化
 * Created by suntao on 2014/11/17.
 */
public class PrinterInstance implements Serializable {
    public final static boolean DEBUG = true;
    private final static String TAG = "PrinterInstance";
    private IPrinterPort myPrinter;
    private String charsetName = "gbk";

    /**
     * 实例化蓝牙打印机（暂未实现）
     * @param context
     * @param bluetoothDevice
     * @param handler
     */
    public PrinterInstance(Context context, BluetoothDevice bluetoothDevice, Handler handler)
    {
        return;
    }

    /**
     * 实例化USB打印机
     * @param context
     * @param usbDevice
     * @param handler
     */
    public PrinterInstance(Context context, UsbDevice usbDevice, Handler handler)
    {
        this.myPrinter = new USBPort(context, usbDevice, handler);
    }

    /**
     * 实例化Wifi打印机（暂未实现）
     * @param ipAddress
     * @param portNumber
     * @param handler
     */
    public PrinterInstance(String ipAddress, int portNumber, Handler handler)
    {
        return;
    }

    /**
     * 获取编码
     * @return
     */
    public String getEncoding()
    {
        return this.charsetName;
    }

    /**
     * 设置编码
     * @param charsetName
     */
    public void setEncoding(String charsetName)
    {
        this.charsetName = charsetName;
    }

    /**
     * 打印机是否已连接
     * @return
     */
    public boolean isConnected()
    {
        return this.myPrinter.getState() == 101;
    }

    /**
     * 打开连接
     */
    public void openConnection()
    {
        this.myPrinter.open();
    }

    /**
     * 关闭连接
     */
    public void closeConnection()
    {
        this.myPrinter.close();
    }

    /**
     * 发送字节数组打印
     * @param data
     * @return
     */
    private int sendByteData(byte[] data)
    {
        if (data != null)
        {
            Log.v(TAG, "sendByteData length is: " + data.length);
            return this.myPrinter.write(data);
        }
        return -1;
    }

    /**
     * 打印字节数组
     * @param data
     * @return
     */
    private int print(byte[] data){
        return sendByteData(data);
    }

    /**
     * 打印字符串
     * @param content
     * @return
     */
    public  int print(String content)
    {
        byte[] data = null;
        try
        {
            if (this.charsetName != "") {
                data = content.getBytes(this.charsetName);
            } else {
                data = content.getBytes();
            }
        }
        catch (UnsupportedEncodingException e)
        {
            Log.e(TAG,"UnsupportedEncoding");
            e.printStackTrace();
        }
        return sendByteData(data);
    }

    /**
     * 打印表格
     * @param table
     * @return
     */
    public int print(Table table)
    {
        return print(table.getTableText());
    }

    public int setFont(int mWidth, int mHeight, int mBold, int mUnderline)
    {
        int mFontSize = 0;
        int mFontMode = 0;
        int mRetVal = 0;
        if ((mBold == 0) || (mBold == 1)) {
            mFontMode |= mBold << 3;
        } else {
            mRetVal = 3;
        }
        if ((mUnderline == 0) || (mUnderline == 1)) {
            mFontMode |= mUnderline << 7;
        } else {
            mRetVal = 4;
        }
        setPrinter(16, mFontMode);
        if ((mWidth >= 0) && (mWidth <= 7)) {
            mFontSize |= mWidth << 4;
        } else {
            mRetVal = 1;
        }
        if ((mHeight >= 0) && (mHeight <= 7)) {
            mFontSize |= mHeight;
        } else {
            mRetVal = 2;
        }
        setPrinter(17, mFontSize);

        return mRetVal;
    }


    public void init()
    {
        setPrinter(0);
    }

    public byte[] read()
    {
        return this.myPrinter.read();
    }

    public boolean setPrinter(int command)
    {
        byte[] arrayOfByte = null;
        switch (command)
        {
            case 0:
                arrayOfByte = new byte[2];
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 64;
                break;
            case 1:
                arrayOfByte = new byte[1];
                arrayOfByte[0] = 0;
                break;
            case 2:
                arrayOfByte = new byte[1];
                arrayOfByte[0] = 12;
                break;
            case 3:
                arrayOfByte = new byte[1];
                arrayOfByte[0] = 10;
                break;
            case 4:
                arrayOfByte = new byte[1];
                arrayOfByte[0] = 13;
                break;
            case 5:
                arrayOfByte = new byte[1];
                arrayOfByte[0] = 9;
                break;
            case 6:
                arrayOfByte = new byte[2];
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 50;
        }
        sendByteData(arrayOfByte);
        return true;
    }

    public boolean setPrinter(int command, int value)
    {
        byte[] arrayOfByte = new byte[3];
        switch (command)
        {
            case 0:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 74;
                break;
            case 1:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 100;
                break;
            case 2:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 33;
                break;
            case 3:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 85;
                break;
            case 4:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 86;
                break;
            case 5:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 87;
                break;
            case 6:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 45;
                break;
            case 7:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 43;
                break;
            case 8:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 105;
                break;
            case 9:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 99;
                break;
            case 10:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 51;
                break;
            case 11:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 32;
                break;
            case 12:
                arrayOfByte[0] = 28;
                arrayOfByte[1] = 80;
                break;
            case 13:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 97;
                if ((value > 2) || (value < 0)) {
                    return false;
                }
                break;
            case 17:
                arrayOfByte[0] = 29;
                arrayOfByte[1] = 33;
                break;
            case 16:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 33;
        }
        arrayOfByte[2] = ((byte)value);
        sendByteData(arrayOfByte);
        return true;
    }

    public void setCharacterMultiple(int x, int y)
    {
        byte[] arrayOfByte = new byte[3];
        arrayOfByte[0] = 29;
        arrayOfByte[1] = 33;
        if ((x >= 0) && (x <= 7) && (y >= 0) && (y <= 7))
        {
            arrayOfByte[2] = ((byte)(x * 16 + y));
            sendByteData(arrayOfByte);
        }
    }

    public void setLeftMargin(int nL, int nH)
    {
        byte[] arrayOfByte = new byte[4];
        arrayOfByte[0] = 29;
        arrayOfByte[1] = 76;

        arrayOfByte[2] = ((byte)nL);
        arrayOfByte[3] = ((byte)nH);
        sendByteData(arrayOfByte);
    }

    public void setPrintModel(boolean isBold, boolean isDoubleHeight, boolean isDoubleWidth, boolean isUnderLine)
    {
        byte[] arrayOfByte = new byte[3];
        arrayOfByte[0] = 27;
        arrayOfByte[1] = 33;

        int a = 0;
        if (isBold) {
            a += 8;
        }
        if (isDoubleHeight) {
            a += 16;
        }
        if (isDoubleHeight) {
            a += 32;
        }
        if (isDoubleHeight) {
            a += 128;
        }
        arrayOfByte[2] = ((byte)a);
        sendByteData(arrayOfByte);
    }

    public void cutPaper()
    {
        byte[] cutCommand = new byte[4];
        cutCommand[0] = 29;
        cutCommand[1] = 86;
        cutCommand[2] = 66;
        cutCommand[3] = 0;
        sendByteData(cutCommand);
    }

    public void ringBuzzer(byte time)
    {
        byte[] buzzerCommand = new byte[3];
        buzzerCommand[0] = 29;
        buzzerCommand[1] = 105;
        buzzerCommand[2] = time;
        sendByteData(buzzerCommand);
    }

    public void openCashbox(boolean cashbox1, boolean cashbox2)
    {
        if (cashbox1)
        {
            byte[] drawCommand = new byte[5];
            drawCommand[0] = 27;
            drawCommand[1] = 112;
            drawCommand[2] = 0;
            drawCommand[3] = 50;
            drawCommand[4] = 50;
            sendByteData(drawCommand);
        }
        if (cashbox2)
        {
            byte[] drawCommand = new byte[5];
            drawCommand[0] = 27;
            drawCommand[1] = 112;
            drawCommand[2] = 1;
            drawCommand[3] = 50;
            drawCommand[4] = 50;
            sendByteData(drawCommand);
        }
    }






}
