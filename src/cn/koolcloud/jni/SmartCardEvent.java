package cn.koolcloud.jni;

public class SmartCardEvent 
{
	public static int SMART_CARD_EVENT_INSERT_CARD = 0;
	public static int SMART_CARD_EVENT_REMOVE_CARD	= 1;
	public static int SMART_CARD_EVENT_POWER_ON = 2;
	public static int SMART_CARD_EVENT_POWER_OFF = 3;
	
	public int nEventID;
	public int nSlotIndex;
	
	public SmartCardEvent()
	{
		nEventID = -1;
		nSlotIndex = -1;
	}
}
