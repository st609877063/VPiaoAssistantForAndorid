package cn.koolcloud.jni;

public class LedInterface {

	static {
		
		/* Driver implementation, so file shall put under /system/lib */
		System.loadLibrary("koolcloud_led");
	}

	/* led behaviour */ 
	public static final int LED_OFF = 0;
	public static final int LED_ON  = 1;
	public static final int LED_FLASH  = 2;	

	/* currently, support manually control camera led */
	public static final int CAMERA_LED  = 1;	
	//public static final int OTHER_LED  = 2;
	public static final int RED_LED = 2;
	public static final int GREEN_LED = 3;
	public static final int NFC_RED_LED = 4;
	public static final int NFC_GREEN_LED = 5;
	public static final int NFC_BLUE_LED = 6;
	public static final int NFC_YELLOW_LED = 7;
	
	/* native methods as following */	
	/** - STRONGLY RECOMMENDED: to implement resource control in Native Layer to avoid resource conflict between 
     *  two Java applications simultaneously invoke this device functionality. 
     */
 
	/**
	* - one preferred process in one application to avoid led resource conflict between two applications.
	* - open()-->set()-->close() 
	* - if there comes another process, please follow above process.
	*/	
	
	/**
	 * open the led driver manager
	 * - to avoid the resource conflict, please strictly follow the preferred process described in the front.
	 * @return 
	 *  - value >= 0 : success (suggest 0)
	 *  - value  < 0 : error code, -1 : device doesn't exist, -10 : unknown error
	 * @note  if no such led on the device, please also implement this API, value return -1.
	 */
	public native static int open();

	/**
	 * set the led on or off
	 * @param[in]  mode : LED_OFF or LED_ON，or LED_FLASH
	 * @param[in]  led  : CAMERA_LED or other led
	 * @return 
	 *  - value >= 0   : success (suggest 0)
	 *  - value == -1  : parameter error	 
	 *  - value == -10 : unknown error
	 */
	public native static int set(int mode,int led);
	
	/**
	 * close the led driver manager
	 * @return 
	 *  - value >= 0 : success (suggest 0)
	 *  - value  < 0 : error code
	 */
	public native static int close();
}
