package com.vpiao.utils;

import android.content.res.Resources;
import com.example.VpiaoAssistant.R;
import com.vpiao.print.sdk.PrinterConstants;
import com.vpiao.print.sdk.PrinterInstance;
import com.vpiao.print.sdk.Table;

/**
 * 打印机帮助类
 * Created by suntao on 2014/11/17.
 */
public final class PrintHelper {
    /**
     * 打印小票
     * @param resources
     * @param mPrinter
     * @param is58mm
     */
    public static void printNote(Resources resources, PrinterInstance mPrinter,
                                 boolean is58mm) {
        mPrinter.init();

        mPrinter.setFont(0, 0, 0, 0);
        mPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
        mPrinter.print(resources.getString(R.string.str_note));
        mPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, 2);


        StringBuffer sb = new StringBuffer();

        mPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
        mPrinter.setCharacterMultiple(1, 1);
        mPrinter.print(resources.getString(R.string.shop_company_title)
                + "\n");

        mPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
        // 字号使用默认
        mPrinter.setCharacterMultiple(0, 0);
        sb.append(resources.getString(R.string.shop_num) + "574001\n");
        sb.append(resources.getString(R.string.shop_receipt_num)
                + "S00003169\n");
        sb.append(resources.getString(R.string.shop_cashier_num)
                + "s004_s004\n");

        sb.append(resources.getString(R.string.shop_receipt_date)
                + "2012-06-17\n");
        sb.append(resources.getString(R.string.shop_print_time)
                + "2012-06-17 13:37:24\n");
        mPrinter.print(sb.toString()); // 打印

        printTable1(resources, mPrinter, is58mm); // 打印表格

        sb = new StringBuffer();
        if (is58mm) {
            sb.append(resources.getString(R.string.shop_goods_number)
                    + "                6.00\n");
            sb.append(resources.getString(R.string.shop_goods_total_price)
                    + "                35.00\n");
            sb.append(resources.getString(R.string.shop_payment)
                    + "                100.00\n");
            sb.append(resources.getString(R.string.shop_change)
                    + "                65.00\n");
        } else {
            sb.append(resources.getString(R.string.shop_goods_number)
                    + "                                6.00\n");
            sb.append(resources.getString(R.string.shop_goods_total_price)
                    + "                                35.00\n");
            sb.append(resources.getString(R.string.shop_payment)
                    + "                                100.00\n");
            sb.append(resources.getString(R.string.shop_change)
                    + "                                65.00\n");
        }

        sb.append(resources.getString(R.string.shop_company_name) + "\n");
        sb.append(resources.getString(R.string.shop_company_site)
                + "www.jiangsuxxxx.com\n");
        sb.append(resources.getString(R.string.shop_company_address) + "\n");
        sb.append(resources.getString(R.string.shop_company_tel)
                + "0574-12345678\n");
        sb.append(resources.getString(R.string.shop_Service_Line)
                + "4008-123-456 \n");
        if (is58mm) {
            sb.append("==============================\n");
        } else {
            sb.append("==============================================\n");
        }
        mPrinter.print(sb.toString());

        mPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER);
        mPrinter.setCharacterMultiple(0, 1);
        mPrinter.print(resources.getString(R.string.shop_thanks) + "\n");
        mPrinter.print(resources.getString(R.string.shop_demo) + "\n\n\n");

        mPrinter.setFont(0, 0, 0, 0);
        mPrinter.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_LEFT);
        mPrinter.setPrinter(PrinterConstants.Command.PRINT_AND_WAKE_PAPER_BY_LINE, 3);

    }

    public static void printTable1(Resources resources,
                                   PrinterInstance mPrinter, boolean is58mm) {
        mPrinter.init();
        String column = resources.getString(R.string.note_title);
        Table table;
        if (is58mm) {
            table = new Table(column, ";", new int[] { 14, 6, 6, 6 });
        } else {
            table = new Table(column, ";", new int[] { 18, 10, 10, 12 });
        }
        table.addRow("" + resources.getString(R.string.bags) + ";10.00;1;10.00");
        table.addRow("" + resources.getString(R.string.hook) + ";5.00;2;10.00");
        table.addRow("" + resources.getString(R.string.umbrella)
                + ";5.00;3;15.00");
        mPrinter.print(table);
    }
}
