package com.vpiao.print.sdk.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import com.vpiao.print.sdk.PrinterInstance;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * 打印机SDK帮助类
 * Created by suntao on 2014/11/17.
 */
public class Utils
{
    /**
     * 压缩图片
     * @param srcBitmap
     * @param maxLength
     * @return
     */
    public static Bitmap compressBitmap(Bitmap srcBitmap, int maxLength)
    {
        Bitmap destBitmap = null;
        try
        {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            byte[] srcBytes = bitmap2Bytes(srcBitmap);

            BitmapFactory.decodeByteArray(srcBytes, 0, srcBytes.length, opts);

            int srcWidth = opts.outWidth;
            int srcHeight = opts.outHeight;
            int destWidth = 0;
            int destHeight = 0;
            double ratio;
            if (srcWidth > srcHeight)
            {
                ratio = srcWidth / maxLength;
                destWidth = maxLength;
                destHeight = (int)(srcHeight / ratio);
            }
            else
            {
                ratio = srcHeight / maxLength;
                destHeight = maxLength;
                destWidth = (int)(srcWidth / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();

            newOpts.inSampleSize = ((int)ratio + 1);

            newOpts.inJustDecodeBounds = false;

            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;

            destBitmap = BitmapFactory.decodeByteArray(srcBytes, 0, srcBytes.length, newOpts);
        }
        catch (Exception localException) {}
        return destBitmap;
    }

    /**
     * 读取数据流到字节
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream)
            throws Exception
    {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }

    /**
     * 将bytes转换为Bitmap
     * @param bytes
     * @param opts
     * @return
     */
    public static Bitmap getImageFromBytes(byte[] bytes, BitmapFactory.Options opts)
    {
        if (bytes != null)
        {
            if (opts != null) {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return null;
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = w / width;
        float scaleHeight = h / height;
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBmp;
    }

    /**
     * Bitmap转换为byte数组
     * @param bm
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 字节数组保存为文件
     * @param b
     * @param outputFile
     * @return
     */
    public static File saveFileFromBytes(byte[] b, String outputFile)
    {
        BufferedOutputStream stream = null;
        File file = null;
        try
        {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (stream != null) {
                try
                {
                    stream.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
        finally
        {
            if (stream != null) {
                try
                {
                    stream.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    public static int printBitmap2File(Bitmap bitmap, String filePath)
    {
        File file;
        if (filePath.endsWith(".png")) {
            file = new File(filePath);
        } else {
            file = new File(filePath + ".png");
        }
        try
        {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public static byte[] bitmap2PrinterBytes(Bitmap bitmap, int left)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();


        byte[] imgbuf = new byte[(width / 8 + left + 4) * height];

        byte[] bitbuf = new byte[width / 8];
        int[] p = new int[8];
        int s = 0;

        System.out.println("+++++++++++++++ Total Bytes: " + (width / 8 + 4) * height);
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width / 8; x++)
            {
                for (int m = 0; m < 8; m++) {
                    if (bitmap.getPixel(x * 8 + m, y) == -1) {
                        p[m] = 0;
                    } else {
                        p[m] = 1;
                    }
                }
                int value = p[0] * 128 + p[1] * 64 + p[2] * 32 + p[3] * 16 + p[4] * 8 +
                        p[5] * 4 + p[6] * 2 + p[7];
                bitbuf[x] = ((byte)value);
            }
            if (y != 0) {
                imgbuf[(++s)] = 22;
            } else {
                imgbuf[s] = 22;
            }
            imgbuf[(++s)] = ((byte)(width / 8 + left));
            for (int j = 0; j < left; j++) {
                imgbuf[(++s)] = 0;
            }
            for (int n = 0; n < width / 8; n++) {
                imgbuf[(++s)] = bitbuf[n];
            }
            imgbuf[(++s)] = 21;
            imgbuf[(++s)] = 1;
        }
        Log.v("imgbuf", Arrays.toString(imgbuf));
        return imgbuf;
    }

    public static byte[] bitmap2PrinterBytes_stylus(Bitmap bitmap, int multiple, int left)
    {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth() + left;





        boolean need_0a = false;

        int maxWidth = 240;
        byte[] imgBuf;
        if (width < maxWidth)
        {
            imgBuf = new byte[(height / 8 + 1) * (width + 6)];
            need_0a = true;
        }
        else
        {
            imgBuf = new byte[(height / 8 + 1) * (width + 5) + 2];
        }
        byte[] tmpBuf = new byte[width + 5];

        int[] p = new int[8];
        int s = 0;
        int t = 0;
        boolean allZERO = true;
        for (int y = 0; y < height / 8 + 1; y++)
        {
            t = 0;

            tmpBuf[t] = 27;
            tmpBuf[(++t)] = 42;


            tmpBuf[(++t)] = ((byte)multiple);
            tmpBuf[(++t)] = ((byte)(width % maxWidth));
            tmpBuf[(++t)] = ((byte)(width / maxWidth > 0 ? 1 : 0));

            allZERO = true;
            for (int x = 0; x < width; x++)
            {
                for (int m = 0; m < 8; m++) {
                    if ((y * 8 + m >= height) || (x < left)) {
                        p[m] = 0;
                    } else {
                        p[m] = (bitmap.getPixel(x - left, y * 8 + m) == -1 ? 0 : 1);
                    }
                }
                int value = p[0] * 128 + p[1] * 64 + p[2] * 32 + p[3] * 16 + p[4] * 8 + p[5] * 4 + p[6] * 2 + p[7];
                tmpBuf[(++t)] = ((byte)value);
                if (value != 0) {
                    allZERO = false;
                }
            }
            if (allZERO)
            {
                if (s == 0) {
                    imgBuf[s] = 27;
                } else {
                    imgBuf[(++s)] = 27;
                }
                imgBuf[(++s)] = 74;
                imgBuf[(++s)] = 8;
            }
            else
            {
                for (int i = 0; i < t + 1; i++) {
                    if ((i == 0) && (s == 0)) {
                        imgBuf[s] = tmpBuf[i];
                    } else {
                        imgBuf[(++s)] = tmpBuf[i];
                    }
                }
                if (need_0a) {
                    imgBuf[(++s)] = 10;
                }
            }
        }
        if (!need_0a)
        {
            imgBuf[(++s)] = 13;
            imgBuf[(++s)] = 10;
        }
        byte[] realBuf = new byte[s + 1];
        for (int i = 0; i < s + 1; i++) {
            realBuf[i] = imgBuf[i];
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < realBuf.length; i++)
        {
            String temp = Integer.toHexString(realBuf[i] & 0xFF);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            sb.append(temp + " ");
            if (((i != 0) && (i % 100 == 0)) || (i == realBuf.length - 1))
            {
                Log.e("12345", sb.toString());
                sb = new StringBuffer();
            }
        }
        return realBuf;
    }

    public static int getStringCharacterLength(String line)
    {
        int length = 0;
        for (int j = 0; j < line.length(); j++) {
            if (line.charAt(j) > 'Ā') {
                length += 2;
            } else {
                length++;
            }
        }
        return length;
    }

    public static int getSubLength(String line, int width)
    {
        int length = 0;
        for (int j = 0; j < line.length(); j++)
        {
            if (line.charAt(j) > 'Ā') {
                length += 2;
            } else {
                length++;
            }
            if (length > width)
            {
                int temp = line.substring(0, j - 1).lastIndexOf(" ");
                if (temp != -1) {
                    return temp;
                }
                return j - 1 == 0 ? 1 : j - 1;
            }
        }
        return line.length();
    }

    public static boolean isNum(byte temp)
    {
        return (temp >= 48) && (temp <= 57);
    }

    public static void Log(String tag, String msg)
    {
        if (PrinterInstance.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static boolean saveBtConnInfo(Context context, String mac)
    {
        try
        {
            File file = new File(context.getFilesDir(), "btinfo.properties");

            FileOutputStream fos = new FileOutputStream(file);

            Properties pro = new Properties();

            pro.setProperty("mac", mac);

            pro.store(fos, "btinfo.properties");

            fos.close();
            Log.v("utils", "save-success!");
            return true;
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    }

    public static Properties getBtConnInfo(Context context)
    {
        try
        {
            File file = new File(context.getFilesDir(), "btinfo.properties");

            FileInputStream fis = new FileInputStream(file);

            Properties pro = new Properties();

            pro.load(fis);
            Log.v("utils", "get-success!");

            fis.close();
            return pro;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


}
