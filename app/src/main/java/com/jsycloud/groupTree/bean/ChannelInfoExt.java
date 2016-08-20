package com.jsycloud.groupTree.bean;



public class ChannelInfoExt extends ChannelInfo {
    // 通道所属设备id
    private String deviceId;

    // 通道所属设备name
    private String deviceName;

    // 通道所在窗口id
    private int windowId;

    // 是否收藏
    private boolean isFavorite;

    // 通道码率类型
    private int streamType;

    // 通道类型
    private int type;

    // 是否在线
    private int state;

    // 搜索框是否选中
    private boolean isSelected = false;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getWindowId() {
        return windowId;
    }

    public void setWindowId(int windowId) {
        this.windowId = windowId;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getStreamType() {
        return streamType;
    }

    public void setStreamType(int streamType) {
        this.streamType = streamType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
