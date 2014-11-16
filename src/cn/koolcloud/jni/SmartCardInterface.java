package cn.koolcloud.jni;

/** STRONGLY RECOMMENDED: to implement resource control in Native Layer to avoid resource conflict between 
 *  two Java applications simultaneously invoke this device functionality. 
 */

/**
* Permission explicit declaration
* android.permission.KOOLCLOUD_SMARTCARD
*/

public class SmartCardInterface 
{

	static
	{
		/* Driver implementation, so file shall put under /system/lib */
		System.loadLibrary("koolcloudPos");
		System.loadLibrary("koolcloud_smartcard");
	}

	/** event defined in SmartCardEvent.java 
	 * public static int SMART_CARD_EVENT_INSERT_CARD = 0;
	 * public static int SMART_CARD_EVENT_REMOVE_CARD = 1;	
	 * public static int SMART_CARD_EVENT_POWER_ON = 2;
	 * public static int SMART_CARD_EVENT_POWER_OFF = 3;
	 */
	 
	/** slot info defined in SmartCardSlotInfo.java */ 
	
	/* native methods as following */	
	
	/**
	 * initialize the smart card reader
	 * return value >= 0 : success (suggest 0)
	 *               < 0 : error code, -101 : input error, -102 : I/O error, -100 : unknown error
	 */
	public native static int init();

	/**
	 * clean up the resource allocated by card reader.
	 * return value >= 0 : success (suggest 0)
	 *               < 0 : error code
	 */
	public native static int terminate();
	
	/**
	 * poll the smart card event
	 * @param nTimeout_MS :	time out in milliseconds.
	 * 					    if nTimeout_MS is less then zero, the searching process is infinite.
	 * @param event	: card event, defined in SmartCardEvent.java
	 * return value >= 0 : success (suggest 0)
	 *               < 0 : error code	
	 */
	public native static int pollEvent(int nTimeout_MS, SmartCardEvent event);

	/**
	 * cancel the poll event invoked before.
	 * return value >= 0 : success (suggest 0)
	 *               < 0 : error code	
	 */
	public native static int cancelPoll();

	/**
	 * query the maximum of the slot in this smart card reader
	 * return value < 0 : error code, -2 : I/O error, -10 : unknown error
	 * 			   == 0 : not defined
	 * 			    > 0 : number of slot
	 */
	public native static int queryMaxNumber();
	
	/**
	 * query whether the smart card is not existent
	 * @param nSlotIndex : slot index, from 0 to (maximum slot - 1)
	 * return value < 0 : error code, -101 : input error, -102 : I/O error, -100 : unknown error
	 * 			   == 0 : not existent
	 * 			    > 0 : be existent
	 */
	public native static int queryPresence(int nSlotIndex);
	
	/**
	 * open the specified card
	 * @param nSlotIndex : slot index, from 0 to (maximum slot - 1).
	 * return value < 0 : error code, -101 : input error, -102 : I/O error, -100 : unknown error
	 * 		       >= 0 : success, return value is a handle. This handle will be employed bye other API as an input parameter
	 */
	public native static int open(int nSlotIndex);
	
	/**
	 * close the smart card reader
	 * @param handle : return from method of open
	 * return value >= 0 : success (suggest 0)
     *	             < 0 : error code, -101 : input error, -102 : I/O error, -100 : unknown error
	 */
	public native static int close(int handle);
	
	/**
	 * power on the smart card reader
	 * @param Handle : return from method of open
	 * @param byteArrayATR : ATR buffer
	 * @param info : SmartCardSlotInfo Object
     * Notice: some MC implementation need emphasize "reserved" in SmartCardSlotInfo struct to 
	 *         distinguish card type. if no specified, the reserved[0] value is 0 as default.
	 *         the value definition of reserved[0] as following:
	 *             reserved[0] == 0 : EMV card mode
	 *             reserved[0] == 1 : S10 memory card mode
	 *             reserved[0] == 2 : S9 memory card mode
	 *             reserved[0] == 3 : other memory card mode
	 * return value >= 0 : ATR length
	 * 		         < 0 : error code
	 */
	public native static int powerOn(int Handle, byte byteArrayATR[], SmartCardSlotInfo info);
	
	/**
	 * power off the smart card reader
	 * @param handle : return from method of open
	 * return value >= 0 : success (suggest 0)
	 *               < 0 : error code, -101 : input error, -102 : I/O error, -100 : unknown error
	 */
	public native static int powerOff(int Handle);
	
	/**
	 * set the slot control information
	 * @param Handle : return from method of open
	 * @param info : SmartCardSlotInfo Object
	 * return value >= 0 : success (suggest 0)
	 *               < 0 : error code, -101 : input error, -102 : I/O error, -100 : unknown error
	 */
	public native static int setSlotInfo(int Handle, SmartCardSlotInfo info);
	
	/**
	 * transmit APDU command
	 * @param Handle : return from method of open
	 * @param byteArrayAPDU	: command of APDU
	 * @param nAPDULength : length of command of APDU
	 * @param byteArrayResponse	: response of command of APDU
	 * return value >= 0 : response data length
	 * 			     < 0 : error code, -1 : parameter mismatch, -2 : I/O error, -10 : unknown error
	 */
	public native static int transmit(int Handle, byte byteArrayAPDU[], int nAPDULength, byte byteArrayResponse[]);
	
	/**
	 * read data from memory card
	 * @param Handle : return from method of open
	 * @param nAreaType area type :
	 * 								0 : main memory,
	 * 								1 : protected memory
	 * 								2 : security memory
	 * @param pDataBuffer : data buffer
	 * @param nDataLength : data length of expecting reading
	 * @param nStartAddress : starting address
	 * return value  >= 0 : data length
	 *                < 0 : error code	 
	 */
	public native static int readMC(int Handle, int nAreaType, byte[] byteArryData, int nDataLength, int nStartAddress);
	
	/**
	 * write data to memory card
	 * @param Handle : return from method of open
	 * @param nAreaType area type :
	 * 								0 : main memory,
	 * 								1 : protected memory
	 * 								2 : security memory
	 * @param pDataBuffer : data buffer
	 * @param nDataLength : data length of expecting writing
	 * @param nStartAddress : starting address
	 * return value  >= 0 : success (suggest 0)
	 *                < 0 : error code
	 */
	
	public native static int writeMC(int Handle, int nAreaType, byte[] byteArryData, int nDataLength, int nStartAddress);
	
	/**
	 * verificate data in memory card
	 * @param Handle : return from method of open
	 * @param byteArrayAPDU : data buffer, especially as for S10 card, memory card password has 3bytes
	 * @param nAPDULength : data length, especially as for S10 card, nAPDULength is always 0x3
	 * return value >= 0 : success (suggest 0)
	 * 			     < 0 : error code				  
	 */
	public native static int verifyMC(int Handle, byte byteArrayAPDU[], int nAPDULength);	
}
