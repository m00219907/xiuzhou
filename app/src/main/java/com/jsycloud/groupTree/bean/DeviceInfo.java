package com.jsycloud.groupTree.bean;

public class DeviceInfo {

    private String deviceId; // 设备ID

    private String deviceName; // 设备名称

    private String deviceIp; // 设备IP/域名/SN

    private int devicePort; // 设备端口

    private String userName; // 用户名

    private String passWord; // 密码

    private int channelCount; // 通道数

    private int factory; // 设备厂商类型 0：未定义 1：大华 2：海康

    private int status; // 是否在线 0:unkonw 1:在线 2：离线
    
    private int deviceType;  //是否是报警主机
    
    public int getdeviceType() {
		return deviceType;
    }

    public void setdeviceType(int deviceType)  {
		this.deviceType = deviceType;
    }
    
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public int getDevicePort() {
        return devicePort;
    }

    public void setDevicePort(int devicePort) {
        this.devicePort = devicePort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getChannelCount() {
        return channelCount;
    }

    public void setChannelCount(int channelCount) {
        this.channelCount = channelCount;
    }

    public int getFactory() {
        return factory;
    }

    public void setFactory(int factory) {
        this.factory = factory;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
