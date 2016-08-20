package com.jsycloud.entity;

public class NotifyEntity {

	private String CallerID;
	private String GroupID;
	private String TransID;
	private String method;
	private int status;
	public String getCallerID() {
		return CallerID;
	}
	public void setCallerID(String callerID) {
		CallerID = callerID;
	}
	public String getGroupID() {
		return GroupID;
	}
	public void setGroupID(String groupID) {
		GroupID = groupID;
	}
	public String getTransID() {
		return TransID;
	}
	public void setTransID(String transID) {
		TransID = transID;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
