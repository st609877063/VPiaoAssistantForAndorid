package cn.koolcloud.jni;

/** STRONGLY RECOMMENDED: to implement resource control in Native Layer to avoid resource conflict between 
 *  two Java applications simultaneously invoke this device functionality. 
 */
 
/** 
* Permission explicit declaration
* android.permission.KOOLCLOUD_PRINTER
*/

/**
* one preferred print process in one application to avoid printer resource conflict between two applications.
* open()-->begin()-->write()-->end()-->close() 
* if there comes another print process, please follow above process.
*/


public class PrinterInterface 
{

	static
	{
		/* Driver implementation, so file shall put under /system/lib */
		System.loadLibrary("koolcloudPos");
		System.loadLibrary("koolcloud_printer");
	}

	/* native methods as following */	
	
	/**
	 * open the device (all available printers), maybe only check the status, maybe allocate the resource.
	 * to avoid the resource conflict, please strictly follow the preferred print process described in the front.
	 * return value >= 0 : success, considering the compatibility, the Integer of default printer(1st printer)
     *                     represents 00000001(binary) or 00000000(binary), return value is 1 or 0. 
	 *                     the Integer of 2nd printer represents 00000010(binary), return value is 2, and so on.
	 *                     if both printers are opened, the Integer shall be 00000011(binary), return value is 3.
	 *                     supposedly the Integer of maximum printers represents 00001111. (total 4 printers available)
	 *               < 0 : error code, -1 : device doesn't exist, -10 : unknown error
	 * NOTES: 0 or 1 as default printer(1st printer), 2 as supplemental printer(2nd printer)
	 */
	public native static int open();
	
	/**
	 * close the device (all opened printers)
	 * NOTICE: when the application complete print request, the application MUST invoke close() to release the printers 
	 * for other application to allocate printer resource.
	 * return value == 0 : success
	 * 				 < 0 : error code, -1 : parameter mismatch, -2 : I/O error, -10 : unknown error
	 */
	public native static int close();

	/**
	 * set which printer to process current print task before calling begin(), write(), end();
	 * all opened printers are required to further check whether available in this process, 
	 * especially for external pluggable printers.
	 * if no call this method, current printer is the default printer(1st printer), it's recommended to call set(1) or set(0)
     * even if no supplemental printer exists. if using supplemental printer(2nd printer), explicitly call set(2). 
	 * after one print task end(), the value goes back to set(1) or set(0) as default.
	 * return value == 0 : success
	 * 			     < 0 : error code
	 */
	public native static int set(int printer);
	
	/**
	 * prepare to print (current setted printer)
	 * return value == 0 : success
	 * 			     < 0 : error code,  -1 : parameter mismatch, -2 : I/O error, -10 : unknown error
	 */
	public native static int begin();
	
	/** 
	 * end to print (current setted printer), clean all the settings of this print task process
	 * return value == 0 : success
	 * 				 < 0 : error code,  -1 : parameter mismatch, -2 : I/O error, -10 : unknown error
	 */
	public native static int end();
	
	/**
	 * write the data to the device(current setted printer), requiring to follow standard ESC/POS format
	 * @param arryData : data or control command
	 * @param nDataLength : length of data or control command
	 * return value ==0 : success
	 * 			    < 0 : error code
	 */
	public native static int write(byte arryData[], int nDataLength);

}
