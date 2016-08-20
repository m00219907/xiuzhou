package com.jsycloud.bean;

public class AlarmItem {
	
	public String deviceName;
	public String channelName;
	public String event;
	public String type;
	public String date;
	
	public AlarmItem(String deviceName, String channelName, String event, String type, String date){
		this.deviceName = deviceName;
		this.channelName = channelName;
		this.event = event;
		this.type = type;
		this.date = date;
	}
 
}
